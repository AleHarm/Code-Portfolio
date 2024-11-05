use std::net::TcpStream;
use std::io::{Read, Write};
use rand::Rng;
use rsa::RsaPublicKey;
use rsa::pkcs1v15::{Signature, VerifyingKey};
use rsa::pkcs8::DecodePublicKey;
use std::convert::TryFrom;
use sha2::Sha256;
use rsa::signature::Verifier;
use rsa::Pkcs1v15Encrypt;
use aes_gcm::{
    aead::{Aead, AeadCore, KeyInit, OsRng},
    Aes256Gcm, Nonce, Key
};

mod messages;
use messages::{HelloMessage, EncryptedMessage,ServerResponse};

fn main() {
    // CONNECT TO SERVER
    let mut stream = match TcpStream::connect("127.0.0.1:2222") {
        Ok(stream) => stream,
        Err(_e) => {
            println!("Could not connect to server. Check that it is running");
            return;
        }
    };
    println!("Connected to server");

    // CREATE HELLO MESSAGE
    let nonce = generate_nonce();
    let hello = HelloMessage {
        signed_message: vec![],
        pub_key: "".to_string(),
        nonce,
    };

    let json = hello.to_json();

    // SEND HELLO MESSAGE
    stream.write_all(json.expect("Failed to serialize hello message").as_bytes())
        .expect("Failed to send data");


    // RECEIVE SERVER RESPONSE
    const BUFFER_SIZE: usize = 4096;
    let mut buffer = [0; BUFFER_SIZE];
    let bytes_read = stream.read(&mut buffer).expect("Failed to read from stream");

    let server_hello_json = std::str::from_utf8(&buffer[..bytes_read])
        .expect("Server hello not in UTF8")
        .trim();

    // PARSE SERVER RESPONSE
    let server_hello_message: Vec<u8>;
    let server_public_key: String;
    let server_hello_nonce: [u8; 32];
    
    match HelloMessage::from_json(server_hello_json.to_string()) {
        Ok(server_hello_response) => {
            
            server_hello_nonce = server_hello_response.nonce;
            server_hello_message = server_hello_response.signed_message.clone();
            server_public_key = server_hello_response.pub_key;
        },
        Err(e) => {
            eprintln!("Failed to parse server response: {}\nResponse: {}", e, server_hello_json);
            std::process::exit(1);
        }
    }

    // VERIFY KEY
    let verifying_key = VerifyingKey::<Sha256>::from_public_key_pem(&server_public_key);
    let signature = Signature::try_from(server_hello_message.as_ref()).expect("Could not convert signature");
    match verifying_key.expect("REASON").verify(&server_hello_nonce, &signature) {
        Ok(_) => {
            println!("Hello Exchanged");
        },
        Err(_e) => {
            println!("Could not verify signature!");
            std::process::exit(1);
        }
    };    

    // LOOP TILL EXIT
    loop {

        // GET USER INPUT
        println!("\nEnter message: ");
        let mut user_input = String::new(); std::io::stdin().read_line(&mut user_input).expect("Failed to read line");
        user_input = (&user_input.trim()).to_string();

        if user_input == "exit" {
            println!("Exiting!");
            std::process::exit(0);
        }

        // MAKE SYMMETRIC KEY
        let key = Aes256Gcm::generate_key(OsRng);
        let key: &[u8; 32] = &[42; 32];
        let key: &Key<Aes256Gcm> = key.into();
        let key: &[u8] = &[42; 32];
        let key: [u8; 32] = key.try_into().expect("REASON");
        let key = Key::<Aes256Gcm>::from_slice(&key);
        let sym_key = Aes256Gcm::new(&key);
        let nonce = Aes256Gcm::generate_nonce(&mut OsRng);

        // ENCRYPT MESSAGE
        let ciphertext = sym_key.encrypt(&nonce, user_input.as_ref()).expect("REASON");

        // ENCRYPT SYM_KEY WITH PUB_KEY
        let sym_key_bytes = key.as_slice();
        let mut rng = rand::thread_rng();
        let pub_key = match RsaPublicKey::from_public_key_pem(&server_public_key) {
            Ok(key) => key,
            Err(error) => panic!("Could not convert public key from PEM {}", error),
        };
        let encrypted_sym_key = pub_key
            .encrypt(&mut rng, Pkcs1v15Encrypt, sym_key_bytes)
            .expect("failed to encrypt");

        // MAKE ENCRYPTED MESSAGE
        let message = EncryptedMessage {
            encrypted_key: encrypted_sym_key.clone(),
            nonce_bytes: nonce.to_vec(),
            ciphertext: ciphertext.clone(),
        };

        let message_json = message.to_json();

        // SEND ENCRYPTED MESSAGE
        stream.write_all(message_json.expect("Failed to serialize message").as_bytes())
            .expect("Failed to send data");

        //RECEIEVE MESSAGE
        const BUFFER_SIZE: usize = 4096;
        let mut buffer = [0; BUFFER_SIZE];
        let bytes_read = stream.read(&mut buffer).expect("Failed to read from stream");

        let server_response_json = std::str::from_utf8(&buffer[..bytes_read])
            .expect("Server hello not in UTF8")
            .trim();

        // PARSE SERVER RESPONSE
        let encrypted_message: Vec<u8>;
        let nonce_bytes: Vec<u8>;
        
        match ServerResponse::from_json(server_response_json.to_string()) {
            Ok(server_response) => {
                
                encrypted_message = server_response.encrypted_message;
                nonce_bytes = server_response.nonce_bytes.clone();
            },
            Err(e) => {
                eprintln!("Failed to parse server response: {}\nResponse: {}", e, server_response_json);
                std::process::exit(1);
            }
        }
        

        //CONFIRM PROPER NONCE
        let new_nonce = Nonce::from_slice(&nonce_bytes);

        let ascii_message = sym_key.decrypt(&new_nonce, encrypted_message.as_ref())
            .expect("REASON");

        let plaintext: String = ascii_message.iter()
            .map(|&ascii| ascii as char)
            .collect();
    

        println!("Received: {:?}", plaintext);
    }
}

fn generate_nonce() -> [u8; 32] {
    let mut rng = rand::thread_rng();
    let mut nonce = [0u8; 32];
    rng.fill(&mut nonce);
    nonce
}
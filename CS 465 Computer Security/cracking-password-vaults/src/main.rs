use std::env;
use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;
use base16ct::lower::decode;
use pbkdf2::{pbkdf2_hmac, pbkdf2_hmac_array};
use sha2::Sha256;
use aes_gcm::{
    aead::{Aead, KeyInit},
    Aes256Gcm, Key, Nonce
};

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where
    P: AsRef<Path>,
{
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

fn main() -> io::Result<()> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        eprintln!("Usage: {} <folder>", args[0]);
        std::process::exit(1);
    }
    let folder = &args[1];
    let vaults_file = format!("{}/vaults", folder);
    let passwords_file = format!("{}/passwords", folder);

    let mut passwords: Vec<String> = Vec::new();

    if let Ok(lines) = read_lines(&passwords_file) {
        for line in lines {
            if let Ok(password) = line {
                passwords.push(password);
            }
        }
    }

    println!("\nReading lines from {}:", vaults_file);
    if let Ok(lines) = read_lines(&vaults_file) {
        for line in lines {
            if let Ok(content) = line {
                let parts: Vec<&str> = content.split(':').collect();
                if parts.len() == 5 {
                    let username = parts[0];
                    let hex_salt = parts[1];
                    let hex_hash = parts[2];
                    let hex_nonce = parts[3];
                    let hex_vault = parts[4];

                    let mut salt = vec![0u8; 12];
                    let mut nonce = vec![0u8; 12];
                    let mut vault = vec![0u8; hex_vault.len() / 2];

                    decode(hex_salt.as_bytes(), &mut salt).expect("Invalid hex salt");
                    decode(hex_nonce.as_bytes(), &mut nonce).expect("Invalid hex nonce");
                    decode(hex_vault.as_bytes(), &mut vault).expect("Invalid hex vault");

                    for password in &passwords {
                        println!("Trying to crack {}'s vault using password \"{}\"...", username, password);
                        let did_password_work_tuple = try_password(password, &salt, hex_hash);
                        if did_password_work_tuple.0 {
                            let key = did_password_work_tuple.1;
                            
                            match decrypt_vault(&key, &nonce, &vault) {
                                Ok(decrypted_vault) => {
                                    println!("Username: {}", username);
                                    println!("Vault contents: {}", decrypted_vault);
                                },
                                Err(e) => println!("Failed to decrypt vault: {}", e),
                            }
                            
                            break;
                        }
                    }
                }
            }
        }
    }

    Ok(())
}

fn try_password(password: &str, salt: &Vec<u8>, expected_hash: &str) -> (bool, Vec<u8>) {
    let hash_n = 100_101;
    let key_n = 100_100;

    let mut hash = vec![0u8; 32];
    pbkdf2_hmac::<Sha256>(password.as_bytes(), salt, hash_n, &mut hash);
    let hex_hash = hex::encode(hash);

    if hex_hash == expected_hash {
        let key = pbkdf2_hmac_array::<Sha256, 32>(password.as_bytes(), salt, key_n);
        println!("Match!");
        (true, key.to_vec())
    } else {
        (false, Vec::new())
    }
}

fn decrypt_vault(key: &[u8], nonce: &[u8], vault: &[u8]) -> Result<String, String> {
    let key = Key::<Aes256Gcm>::from_slice(key);
    let nonce = Nonce::from_slice(nonce);
    let cipher = Aes256Gcm::new(key);
    
    cipher.decrypt(nonce, vault.as_ref())
        .map_err(|e| format!("Decryption error: {:?}", e))
        .and_then(|plaintext| String::from_utf8(plaintext).map_err(|e| format!("UTF-8 error: {}", e)))
}
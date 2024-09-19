#include <asm-generic/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <pthread.h>
#include "sbuf.h"
#include "sbuf.c"


/* Recommended max object size */
#define MAX_OBJECT_SIZE 102400

//Threads
#define NTHREADS  8
#define SBUFSIZE  5

static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0";
sbuf_t sbuf;

int complete_request_received(char *);
int parse_request(char *, char *, char *, char *, char *, char *);
void test_parser();
void print_bytes(unsigned char *, int);

//Part 1 Methods
char* findMethod(char *, char *, char *);
char* findURL(char *, char *, char *);
void findHeaders(char *, char *, char *);
void parseURL(char *, char *, char *, char *);
char* findPort(char *, char *, char *, char *);

//Part 2 Methods
int open_sfd(int);
void handle_client(int);
char* createRequest(char *, char *, char *, char *, char *);
int sendToServer(char *, char *, char *);
void recvFromClient(int, char *);
int recvFromServer(int, char *);

//Part 3 Methods
void *threadMain(void *);


int main(int argc, char *argv[]){

	sbuf_init(&sbuf, SBUFSIZE);
	pthread_t tid;
	for (int i = 0; i < NTHREADS; i++) {
		
		pthread_create(&tid, NULL, threadMain, NULL);
	}

	//test_parser();
	int sfd = open_sfd(atoi(argv[1]));

	//LISTEN FOR NEW CLIENTS
	listen(sfd, 0);

	while(1){

		int newSFD = accept(sfd, NULL, NULL);
		sbuf_insert(&sbuf, newSFD);

	}
	//printf("%s\n", user_agent_hdr);
	return 0;
}

//
//REQUEST PARSING
//

int complete_request_received(char *request) {
	char* needle = "\r\n\r\n";
	if(strstr(request, needle) != NULL){

		return 1;
	}

	return 0;
}

int parse_request(char *request, char *method, char *hostname, char *port, char *path, char *url) {

	char *beginning_of_thing = request;
	char *end_of_thing = NULL;
	char headers[MAX_OBJECT_SIZE];
	beginning_of_thing = findMethod(beginning_of_thing, end_of_thing, method);
	beginning_of_thing = findURL(beginning_of_thing, end_of_thing, url);
	findHeaders(beginning_of_thing, end_of_thing, headers);
	parseURL(port, hostname, path, url);

	return complete_request_received(request);
}

char* findMethod(char *beginning_of_thing, char *end_of_thing, char *method){

	end_of_thing = strstr(beginning_of_thing, " ");
	memcpy(method, beginning_of_thing, end_of_thing - beginning_of_thing);
	method[end_of_thing - beginning_of_thing] = 0x0;
	beginning_of_thing = end_of_thing + 1;
	return beginning_of_thing;
}

char* findURL(char *beginning_of_thing, char *end_of_thing, char *url){
	end_of_thing = strstr(beginning_of_thing, " ");
	strncpy(url, beginning_of_thing, end_of_thing - beginning_of_thing);
	url[end_of_thing - beginning_of_thing] = 0x0;
	beginning_of_thing = end_of_thing + 1;
	return beginning_of_thing;
}

void findHeaders(char *beginning_of_thing, char *end_of_thing, char *headers){

	beginning_of_thing = strstr(beginning_of_thing, "\r\n") + 2;
	end_of_thing = strstr(beginning_of_thing, "\r\n\r\n");
	if(strlen(beginning_of_thing) != 0){

		memcpy(headers, beginning_of_thing, end_of_thing - beginning_of_thing);
		headers[end_of_thing - beginning_of_thing] = 0x0;
	}
}

void parseURL(char *port, char *hostname, char *path, char *url){

	char *beginning_of_thing = strstr(url, "://") + 3;
	char *end_of_thing = NULL;
	end_of_thing = findPort(port, path, beginning_of_thing, end_of_thing);

	if(end_of_thing == NULL){

		port = "80";
		end_of_thing = strstr(beginning_of_thing, "/");
		strncpy(hostname, beginning_of_thing, end_of_thing - beginning_of_thing);
		hostname[end_of_thing - beginning_of_thing] = 0x0;
		memcpy(path, end_of_thing, strlen(end_of_thing));
		path[strlen(end_of_thing)] = 0x0;
	}else{

		strncpy(hostname, beginning_of_thing, end_of_thing - beginning_of_thing);
		hostname[end_of_thing - beginning_of_thing] = 0x0;
	}
}

char* findPort(char *port, char *path, char *beginning_of_thing, char *end_of_thing){

	end_of_thing = strstr(beginning_of_thing, ":");

	if(end_of_thing != NULL){

		char *portStart = end_of_thing + 1;
		char *portEnd = strstr(portStart, "/");
		strncpy(port, portStart, portEnd - portStart);
		port[portEnd - portStart] = 0x0;
		memcpy(path, portEnd, strlen(portEnd));
		path[strlen(portEnd)] = 0x0;
		return end_of_thing;
	}else{
		return NULL;
	}
}

void print_bytes(unsigned char *bytes, int byteslen) {
	int i, j, byteslen_adjusted;

	if (byteslen % 8) {
		byteslen_adjusted = ((byteslen / 8) + 1) * 8;
	} else {
		byteslen_adjusted = byteslen;
	}
	for (i = 0; i < byteslen_adjusted + 1; i++) {
		if (!(i % 8)) {
			if (i > 0) {
				for (j = i - 8; j < i; j++) {
					if (j >= byteslen_adjusted) {
						printf("  ");
					} else if (j >= byteslen) {
						printf("  ");
					} else if (bytes[j] >= '!' && bytes[j] <= '~') {
						printf(" %c", bytes[j]);
					} else {
						printf(" .");
					}
				}
			}
			if (i < byteslen_adjusted) {
				printf("\n%02X: ", i);
			}
		} else if (!(i % 4)) {
			printf(" ");
		}
		if (i >= byteslen_adjusted) {
			continue;
		} else if (i >= byteslen) {
			printf("   ");
		} else {
			printf("%02X ", bytes[i]);
		}
	}
	printf("\n");
}


//
//REQUEST HANDLING
//

int open_sfd(int port){

	//SET UP SOCKET
	int sfd = socket(AF_INET ,SOCK_STREAM, 0);

	int optval = 1;
	setsockopt(sfd, SOL_SOCKET, SO_REUSEPORT, &optval, sizeof(optval));
	struct sockaddr_in local_addr_in;
	//struct sockaddr *local_addr = (struct sockaddr *)&local_addr_in;
	int addrLen = sizeof(local_addr_in);
	local_addr_in.sin_port = htons(port);
	local_addr_in.sin_addr.s_addr = 0;
	local_addr_in.sin_family = AF_INET;

	//CALL BIND ON SOCKET
	bind(sfd, (struct sockaddr *)&local_addr_in, addrLen);

	return sfd;
}

void handle_client(int sfd){

	char buf[MAX_OBJECT_SIZE];
	recvFromClient(sfd, buf);

	char method[16], host[64], port[8], path[64], url[MAX_OBJECT_SIZE];
	memset(port, 0, 8);
	if(parse_request(buf, method, host, port, path, url)){

		printf("Request info:\n");
		printf("METHOD: %s\n", method);
		printf("%s\n", method);
		printf("HOSTNAME: %s\n", host);
		printf("PORT: %s\n", port);
		printf("PATH: %s\n", path);

		char bytesToSend[MAX_OBJECT_SIZE];
		memset(bytesToSend, 0, MAX_OBJECT_SIZE);
		createRequest(bytesToSend, method, path, host, port);
		printf("Bytes:\n %s\n", bytesToSend);

		int serverSocket = sendToServer(port, host, bytesToSend);
		char bytesReceived[MAX_OBJECT_SIZE];
		int numBytesRecvdFromServer = recvFromServer(serverSocket, bytesReceived);
		send(sfd, bytesReceived, numBytesRecvdFromServer, 0);

	}else{

		//printf("Incomplete request\n");
	}

	close(sfd);
}

char* createRequest(char *bytesToSend, char *method, char *path, char *hostname, char *port){

	strcat(bytesToSend, method);
	strcat(bytesToSend, " ");
	strcat(bytesToSend, path);
	strcat(bytesToSend, " HTTP/1.0\r\n");
	strcat(bytesToSend, "HOST: ");
	strcat(bytesToSend, hostname);

	if(strcmp(port, "") != 0){
		strcat(bytesToSend, ":");
	}

	strcat(bytesToSend, port);
	strcat(bytesToSend, "\r\n");
	strcat(bytesToSend, user_agent_hdr);
	strcat(bytesToSend, "\r\n");
	strcat(bytesToSend, "Connection: close\r\n");
	strcat(bytesToSend, "Proxy-Connection: close\r\n\r\n");

	return bytesToSend;
}

int sendToServer(char *port, char *host, char *bytesToSend){

	if(strcmp(port, "") == 0){

		port = "80";
	}

	int serverSocket = open_sfd(atoi(port));
	struct addrinfo *result;
	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = 0;
	hints.ai_protocol = 0;
	getaddrinfo(host, port, &hints, &result);

	struct sockaddr_in remote_addr_in;

	remote_addr_in = *(struct sockaddr_in *)result->ai_addr;
	socklen_t addr_len = result->ai_addrlen;

	if (connect(serverSocket, (struct sockaddr *)&remote_addr_in, addr_len) == -1){
		//printf("Failure\n");
		return -1;
	}

	send(serverSocket, bytesToSend, strlen(bytesToSend), 0);

	return serverSocket;
}

void recvFromClient(int sfd, char *buf){

	//printf("In recv\n");
	memset(buf, 0, MAX_OBJECT_SIZE);
	int num_read = -1;
	int total_read = 0;
	while(complete_request_received(buf) == 0){

		num_read = recv(sfd, buf + total_read, MAX_OBJECT_SIZE, 0);

		if(num_read == -1){
			//printf("Something has gone horribly wrong in the receive\n");
			break;
		}
		total_read += num_read;
	}
	
	//print_bytes((unsigned char *)buf, total_read);
	buf[total_read] = 0x0;
}

int recvFromServer(int sfd, char *buf){

	//printf("In recv\n");
	memset(buf, 0, MAX_OBJECT_SIZE);
	int num_read = -1;
	int total_read = 0;
	while(num_read != 0){

		num_read = recv(sfd, buf + total_read, MAX_OBJECT_SIZE, 0);

		if(num_read == -1){
			//printf("Something has gone horribly wrong in the receive\n");
			break;
		}
		total_read += num_read;
	}
	
	//print_bytes((unsigned char *)buf, total_read);
	buf[total_read] = 0x0;
	return total_read;
}

//
//THREADS
//

void *threadMain(void *vargp){

	pthread_detach(pthread_self());
	while (1) {
		int sfd = sbuf_remove(&sbuf); /* Remove connfd from buffer */
		handle_client(sfd);              /* Service client */
		close(sfd);
	}
}
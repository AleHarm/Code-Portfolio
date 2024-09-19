// Replace PUT_USERID_HERE with your actual BYU CS user id, which you can find
// by running `id -u` on a CS lab machine.
#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <string.h>
#include <strings.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>


#define USERID 1823700384
#define SEND_SIZE 8
#define RECV_SIZE 256


int verbose = 0;

void print_bytes(unsigned char *bytes, int byteslen);

int main(int argc, char *argv[])
{

	char treasure[1024];
	bzero(treasure, 1024);


	char *server = argv[1];
	char *port = argv[2];
	int level = atoi(argv[3]);
	int seed = htons(atoi(argv[4]));
	int userID = htonl(USERID);
	
	unsigned char sendVal[SEND_SIZE];
	bzero(sendVal, SEND_SIZE);
	sendVal[0] = 0;
	sendVal[1] = level;
	memcpy(&sendVal[2], &userID, 4);
	memcpy(&sendVal[6], &seed, 2);	

	int addr_fam = AF_INET;
	socklen_t addr_len;

	//Local setup
	struct sockaddr_in local_addr_in;
	struct sockaddr *local_addr = NULL;

	int sfd = socket(addr_fam, SOCK_DGRAM, 0);
	//int sockNameReturnVal = getsockname(sfd, local_addr, &addr_len);
	getsockname(sfd, local_addr, &addr_len);

	local_addr_in = *((struct sockaddr_in*)local_addr);
	local_addr = (struct sockaddr*)&local_addr_in;


	//Remote setup

	struct sockaddr *remote_addr;
	struct sockaddr_in remote_addr_in;
	struct addrinfo *result;

	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = addr_fam;			 /* Allow IPv4, IPv6, or both, depending on
							what was specified on the command line. */
	hints.ai_socktype = SOCK_STREAM; /* Datagram socket */
	hints.ai_flags = 0;
	hints.ai_protocol = 0; /* Any protocol */

	getaddrinfo(server, port, &hints, &result);

	addr_len = result->ai_addrlen;
	remote_addr_in = *(struct sockaddr_in *)result->ai_addr;
	remote_addr = (struct sockaddr *)&remote_addr_in;
	

	//Send data

	//int numBytesSent = sendto(sfd, sendVal, SEND_SIZE, 0, (struct sockaddr *)&remote_addr_in, addr_len);
	sendto(sfd, sendVal, SEND_SIZE, 0, remote_addr, addr_len);

	// printf("sockNameReturnVal: %d\n", sockNameReturnVal);
	// printf("sfd: %d\n", sfd);
	// perror("Result of sendTo was");
	// printf("Number of bytes sent: %d\n", numBytesSent);
	// print_bytes(sendVal, SEND_SIZE);


	//Receive data

	unsigned char firstBytes[256];
	//int numBytesRecvd = recvfrom(sfd, firstBytes, RECV_SIZE, 0, (struct sockaddr *)&remote_addr_in, &addr_len);
	recvfrom(sfd, firstBytes, RECV_SIZE, 0, remote_addr, &addr_len);

	// perror("\n\nResult of recvfrom was");
	// printf("Number of bytes recvd: %d\n", numBytesRecvd);
	// print_bytes(firstBytes, RECV_SIZE);


	//Process data

	unsigned char chunkLength = firstBytes[0];
	unsigned char chunk[chunkLength + 1];
	unsigned char opCode = 0;
	unsigned short opParam;
	unsigned int nonce;
	memcpy(&chunk, &firstBytes[1], chunkLength);
	chunk[chunkLength] = 0x00;
	memcpy(&opCode, &firstBytes[chunkLength + 1], 1);
	memcpy(&opParam, &firstBytes[chunkLength + 2], 2);
	memcpy(&nonce, &firstBytes[chunkLength + 4], 4);

	nonce = ntohl(nonce);
	unsigned int noncePlus = nonce + 1;
	noncePlus = htonl(noncePlus);

	// printf("chunkLength is %d\n", chunkLength);
	// printf("chunk is %s\n", chunk);
	// printf("opCode is %d\n", opCode);
	// printf("opParam is %x\n", opParam);
	// printf("nonce is %x\n", nonce);
	// printf("noncePlus is %x\n", noncePlus);

	strcat(treasure, (char*)chunk);


	while(chunkLength > 0 && chunkLength < 127){
		
		// printf("\n\nInside loop\n");
		
		
		switch(opCode){
			case 0:
				break;
			case 1:
				//printf("opParam = %d\n", ntohl(opParam));
				remote_addr_in.sin_port = opParam;
				// printf("Case 1\n");
				break;
			case 2:
				local_addr_in.sin_port = opParam;
			case 3:
				//Figure out later
				break;
			case 4:
				//Not doing it
				break;
		}

		//numBytesSent = sendto(sfd, &noncePlus, 4, 0, (struct sockaddr *)&remote_addr_in, addr_len);
		sendto(sfd, &noncePlus, 4, 0, remote_addr, addr_len);

		// perror("Result of sendTo was");
		// printf("Number of bytes sent: %d\n", numBytesSent);

		//Receive

		unsigned char recvBytes[256];
		//numBytesRecvd = recvfrom(sfd, recvBytes, RECV_SIZE, 0, (struct sockaddr *)&remote_addr_in, &addr_len);
		recvfrom(sfd, recvBytes, RECV_SIZE, 0, remote_addr, &addr_len);
		// printf("number of bytes received: %d\n", numBytesRecvd);

		chunkLength = recvBytes[0];
		unsigned char nextChunk[chunkLength + 1];
		memcpy(&nextChunk, &recvBytes[1], chunkLength);
		nextChunk[chunkLength] = 0x00;
		memcpy(&opCode, &recvBytes[chunkLength + 1], 1);
		memcpy(&opParam, &recvBytes[chunkLength + 2], 2);
		memcpy(&nonce, &recvBytes[chunkLength + 4], 4);

		opCode = opCode;
		nonce = ntohl(nonce);
		noncePlus = nonce + 1;
		noncePlus = htonl(noncePlus);


		// printf("chunkLength is %d\n", chunkLength);
		// printf("chunk is %s\n", nextChunk);
		// printf("opCode is %d\n", opCode);
		// printf("opParam is %x\n", opParam);
		// printf("nonce is %x\n", nonce);
		// printf("noncePlus is %x\n\n\n\n\n", noncePlus);

		strcat(treasure, (char*)nextChunk);
	}

	if(chunkLength > 127){

		// printf("We've got an error\n");
		// printf("Error is: %d\n", chunkLength);
	}else{

		// printf("We've done it!\n");
		// printf("Treasure: %s\n", treasure);
		printf("%s\n", treasure);
		// print_bytes((unsigned char*)treasure, sizeof(treasure));
	}

}

void print_bytes(unsigned char *bytes, int byteslen)
{
	int i, j, byteslen_adjusted;

	if (byteslen % 8)
	{
		byteslen_adjusted = ((byteslen / 8) + 1) * 8;
	}
	else
	{
		byteslen_adjusted = byteslen;
	}
	for (i = 0; i < byteslen_adjusted + 1; i++)
	{
		if (!(i % 8))
		{
			if (i > 0)
			{
				for (j = i - 8; j < i; j++)
				{
					if (j >= byteslen_adjusted)
					{
						printf("  ");
					}
					else if (j >= byteslen)
					{
						printf("  ");
					}
					else if (bytes[j] >= '!' && bytes[j] <= '~')
					{
						printf(" %c", bytes[j]);
					}
					else
					{
						printf(" .");
					}
				}
			}
			if (i < byteslen_adjusted)
			{
				printf("\n%02X: ", i);
			}
		}
		else if (!(i % 4))
		{
			printf(" ");
		}
		if (i >= byteslen_adjusted)
		{
			continue;
		}
		else if (i >= byteslen)
		{
			printf("   ");
		}
		else
		{
			printf("%02X ", bytes[i]);
		}
	}
	printf("\n");
}

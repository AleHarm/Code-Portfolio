#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <fcntl.h>

#define BUFSIZE 30





//
//
//
//
//
//
//I'M SO SORRY ABOUT HOW TERRIBLE MY CODE LOOKS, I DIDN'T KNOW I'D BE TURNING IT IN
//SO I DIDN'T FILL OUT A LOT OF QUESTIONS IN THE RIGHT PLACES TO AVOID DUPLICATING CODE
//
//PLEASE FORGIVE ME, I PROMISE I DID IT ALL MYSELF AND MOST OF IT I DID WRONG, SO THAT SHOULD SHOW I WASN'T CHEATING
//
//IF YOU HAVE TO DOCK MY GRADE BECAUSE THE FILE DOESN'T MATCH MY ANSWERS, PLZ LMK AND I'LL REDO THE CODE TO MATCH
//
//
//
//






void memprint(char *, char *, int);

void intro();
void part1();
void part2();
void part3();
void part4();
void part5(char *);
void part6();

int main(int argc, char *argv[]) {
	if (argc != 2) {
		fprintf(stderr, "usage: %s <filename>\n", argv[0]);
		exit(1);
	}
	intro();
	part1();
	part2();
	part3();
	part4();
	part5(argv[1]);
	part6();
}

void memprint(char *s, char *fmt, int len) {
	// iterate through each byte/character of s, and print each out
	int i;
	char fmt_with_space[8];

	sprintf(fmt_with_space, "%s ", fmt);
	for (i = 0; i < len; i++) {
		printf(fmt_with_space, s[i]);
	}
	printf("\n");
}

void intro() {
	printf("===== Intro =====\n");

	// Note: STDOUT_FILENO is defined in /usr/include/unistd.h:
	// #define	STDOUT_FILENO	1

	char s1[] = { 0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x0a };
	write(STDOUT_FILENO, s1, 6);

	char s2[] = { 0xe5, 0x8f, 0xb0, 0xe7, 0x81, 0xa3, 0x0a };
	write(STDOUT_FILENO, s2, 7);

	char s3[] = { 0xf0, 0x9f, 0x98, 0x82, 0x0a };
	write(STDOUT_FILENO, s3, 5);
}

void part1() {

	//printf("%ld\n", sizeof(s1));
	char s1[] = "hello";
	int s1_len;

	printf("%d\n", s1[5]);

	printf("===== Question 3 (no code changes) =====\n");

	printf("===== Question 4 (no code changes) =====\n");

	printf("===== Question 5 =====\n");
	char s2[10];
	int s2_len;

	printf("===== Question 6 =====\n");
	char *s3 = s1;
	int s3_len;
	printf("===== Question 7 =====\n");
	char *s4 = malloc(1024 * sizeof(char));
	int s4_len;
	printf("%ld\n", sizeof(s4));

	free(s4);
	printf("===== Question 8 (no code changes) =====\n");

	printf("===== Question 9 =====\n");

}

void part2() {
	char s1[] = "hello";
	char s2[1024];
	char *s3 = s1;

	// Copy sizeof(s1) bytes of s1 to s2.
	memcpy(s2, s1, sizeof(s1));

	printf("%lu, %lu, %lu\n", (long unsigned int)&s1, (long unsigned int)&s2, (long unsigned int)&s3);
	printf("%lu, %lu, %lu\n", (long unsigned int)*s1, (long unsigned int)*s2, (long unsigned int)s3);



	printf("===== Question 11 =====\n");

	printf("===== Question 12 (no code changes) =====\n");

	printf("===== Question 13 =====\n");
	printf("%s\n%s\n%s\n", s1, s2, s3);
	printf("===== Question 14 =====\n");
	if(s1 == s2){

		printf("s1 == s2\n");
	}
	if(s1 == s3){

		printf("s1 == s3\n");
	}
	if(s2 == s3){

		printf("s2 == s3\n");
	}
	printf("===== Question 15 =====\n");
	if(strcmp(s1, s2)){

                printf("s1 == s2\n");
        }
        if(strcmp(s1, s3)){

                printf("s1 == s3\n");
        }
        if(strcmp(s2, s3)){

		printf("s2 == s3\n");
        }
	printf("===== Question 16 =====\n");
	s1[1] = 'u';
	printf("%s\n%s\n%s\n", s1, s2, s3);
	printf("===== Question 17 =====\n");

	 if(s1 == s2){

                printf("s1 == s2\n");
        }
        if(s1 == s3){

                printf("s1 == s3\n");
        }
        if(s2 == s3){

                printf("s2 == s3\n");
        }

	printf("===== Question 18 =====\n");

	if(strcmp(s1, s2)){

                printf("s1 == s2\n");
        }
        if(strcmp(s1, s3)){

                printf("s1 == s3\n");
        }
        if(strcmp(s2, s3)){

                printf("s2 == s3\n");
        }

}

void part3() {
	char s1[] = { 'a', 'b', 'c', 'd', 'e', 'f' };
	char s2[] = { 97, 98, 99, 100, 101, 102 };
	char s3[] = { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66 };

	printf("===== Question 19 =====\n");


	if(memcmp(s1, s2, strlen(s1))){

		printf("s1 == s2\n");
	}
	if(memcmp(s1, s3, strlen(s1))){

		printf("s1 == s3\n");
	}
	if(memcmp(s2, s3, strlen(s2))){

		printf("s2 == s3\n");
	}
}

void part4() {
	char s1[] = { 'a', 'b', 'c', '\0', 'd', 'e', 'f', '\0' };
	char s2[] = { 'a', 'b', 'c', '\0', 'x', 'y', 'z', '\0' };

	printf("===== Question 20 =====\n");
	if(memcmp(s1, s2, strlen(s1))){

                printf("s1 == s2\n");
        }

	printf("===== Question 21 =====\n");
	if(strcmp(s1, s2) == 0){

                printf("s1 == s2\n");
        }

	printf("===== Question 22 =====\n");
	char s3[16];
	char s4[16];

	memset(s3, 'z', strlen(s3));
	memprint(s3, "%02x", strlen(s3));
	memset(s4, 'z', strlen(s4));
	memprint(s4, "%02x", strlen(s4));

	printf("===== Question 23 =====\n");
	strcpy(s3, s1);
	memprint(s3, "%02x", strlen(s3));
	printf("===== Question 24 =====\n");
	int myval = 42;
	sprintf(s4, "%s%d\n", s1, myval);
	memprint(s4, "%02x", strlen(s4));
	printf("===== Question 25 =====\n");
	char *s5;
	char *s6 = NULL;
	char *s7 = s4;
	//memprint(s5, "%02x", strlen(s5));

}

void part5(char *filename) {
	printf("===== Question 26 =====\n");

	printf("===== Question 27 =====\n");

	char buf[BUFSIZE];

	memset(buf, 'z', strlen(buf));
	memprint(buf, "%02x", strlen(buf));

	printf("===== Question 28 =====\n");

	printf("%s", buf);
	write(1, buf, BUFSIZE);

	fprintf(stderr, "===== Question 29 =====\n");

	//fprintf(stderr, buf);
	printf("\n");
	write(2, buf, BUFSIZE);
	printf("\n");

	printf("===== Question 30 (no code changes) =====\n");

	printf("===== Question 31 =====\n");
	int fd1, fd2;

	fd1 = open(filename, O_RDONLY);
	fd2 = fd1;
	printf("%d\n", fd1);
	printf("%d\n", fd2);

	printf("===== Question 32 =====\n");
	size_t nread = 0;
	size_t totread = 0;

	nread = read(fd1, buf + totread, BUFSIZE);
	totread += nread;

	printf("%ld\n", nread);
	printf("%ld\n", totread);
	memprint(buf, "%02x", BUFSIZE);
	printf("%s", buf);

	printf("===== Question 33 (no code changes) =====\n");

	printf("===== Question 34 (no code changes) =====\n");

	printf("===== Question 35 =====\n");

	printf("===== Question 36 (no code changes) =====\n");

	printf("===== Question 37 (no code changes) =====\n");

	printf("===== Question 38 =====\n");

	printf("===== Question 39 (no code changes) =====\n");

	printf("===== Question 40 (no code changes) =====\n");

	printf("===== Question 41 (no code changes) =====\n");

	printf("===== Question 42 =====\n");

	printf("===== Question 43 =====\n");

	buf[totread] = 0;
	printf("%s", buf);

	printf("===== Question 44 =====\n");

	printf("%d\n", close(fd1));

	printf("===== Question 45 =====\n");
	int ret = 0;

	printf("%d\n", close(fd2));

	printf("===== Question 46 =====\n");

	fprintf(stdout, "abc");
	fflush(stdout);
	fprintf(stderr, "def");
	fprintf(stdout, "ghi\n");
	write(1, "abc", 3);
	fflush(stdout);
	write(2, "def", 3);
	write(1, "ghi\n", 4);

	printf("===== Question 47 =====\n");

	printf("===== Question 48 =====\n");

}

void part6() {
	printf("===== Question 49 =====\n");
	char *s1;

	s1 = getenv("CS324_VAR");
	if(s1){
		printf("CS324_VAR is %s\n", s1);
	}else{
		printf("CS324_VAR not found\n");
	}
	printf("===== Question 50 (no code changes) =====\n");
}

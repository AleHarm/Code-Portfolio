#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
	
    int content_length = atoi(getenv("CONTENT_LENGTH"));
    char* query_string = getenv("QUERY_STRING");

    char content[content_length];
    read(0, content, content_length);
    content[content_length] = 0x0;

    printf("Content-Type: text/plain\r\n");
    printf("Content-Length: 108\r\n\r\n");
    printf("Hello CS324\n");
    printf("Query string: %s\n", query_string);
    printf("Request body: %s\n", content);

    return 0;
}

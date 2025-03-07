#ifndef _MTRACK_04_H_
    #define _MTRACK_04_H_
#include <stdio.h>
#define malloc(size) _clone_malloc(__FILE__,__func__,__LINE__, size)
#define calloc(number, size) _clone_calloc(__FILE__,__func__,__LINE__, number, size)
#define realloc(adr, size) _clone_realloc(__FILE__,__func__,__LINE__, adr, size)
#define free(ptr) _clone_free(__FILE__,__func__,__LINE__,ptr)
void* _clone_malloc( char*file, const char* funct, int line, size_t size );
void* _clone_calloc( char*file, const char* funct, int line, int number, size_t size );
void* _clone_realloc( char*file, const char* funct, int line, void* adr, size_t size );
void _clone_free(char*file, const char* funct, int line, void* adr);
#endif
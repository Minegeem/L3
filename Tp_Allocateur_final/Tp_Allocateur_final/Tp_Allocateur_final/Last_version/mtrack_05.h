#ifndef _MTRACK_04_H_
    #define _MTRACK_04_H_
#include <stdio.h>
#define malloc(size) _clone_malloc(__FILE__,__func__,__LINE__, size)
#define free(ptr) _clone_free(__FILE__,__func__,__LINE__,ptr)
void* _clone_malloc( char*file, const char* funct, int line, size_t size );
void _clone_free(char*file, const char* funct, int line, void* adr);
#endif
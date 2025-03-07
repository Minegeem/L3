#ifndef _MTRACK_04_H_
    #define _MTRACK_04_H_
#include <stdio.h>
#define malloc(size) _clone_malloc(size)
#define free(ptr) _clone_free(ptr)
void* _clone_malloc(size_t size);
void _clone_free(void* adr);
#endif
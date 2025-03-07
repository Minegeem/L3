#include <stdio.h>

#include "mtrack_03.h"

int main(void) {
    char *a=_clone_malloc(1);
    char *b=_clone_malloc(1);
    _clone_free(a);
    b++;
    _clone_free(b);
    return 0;
}
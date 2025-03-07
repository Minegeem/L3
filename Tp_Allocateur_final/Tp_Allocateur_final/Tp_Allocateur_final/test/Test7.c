#include <stdlib.h>

void main() {
    int *arr = (int *)malloc(0);
    if (arr) free(arr);
}

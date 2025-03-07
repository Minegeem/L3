#include <stdlib.h>

void main() {
    int *arr = (int *)calloc(0, sizeof(int));
    if (arr) free(arr);
}

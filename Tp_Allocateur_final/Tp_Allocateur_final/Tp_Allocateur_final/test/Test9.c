#include <stdlib.h>

void main() {
    size_t large_size = (size_t)1 << 30; // 1 GB
    int *arr = (int *)malloc(large_size);
    if (arr) free(arr);
}

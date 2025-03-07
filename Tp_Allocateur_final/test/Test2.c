#include <stdlib.h>

void main() {
    int *arr = (int *)calloc(5, sizeof(int));
    if (!arr) return;
    free(arr);
}

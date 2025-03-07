#include <stdlib.h>

void main() {
    int *arr = (int *)malloc(5 * sizeof(int));
    if (!arr) return;
    free(arr);
}

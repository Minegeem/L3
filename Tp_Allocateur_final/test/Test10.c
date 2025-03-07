#include <stdlib.h>

void main() {
    int *arr = (int *)malloc(10 * sizeof(int));
    if (!arr) return;
    for (int i = 0; i < 10; i++) arr[i] = i;
    int *sub_arr = arr + 5;
    free(arr);
}

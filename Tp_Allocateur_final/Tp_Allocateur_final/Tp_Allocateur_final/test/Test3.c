#include <stdlib.h>

void main() {
    int *arr = (int *)malloc(3 * sizeof(int));
    if (!arr) return;
    for (int i = 0; i < 3; i++) arr[i] = i + 1;
    arr = (int *)realloc(arr, 5 * sizeof(int));
    if (!arr) return;
    for (int i = 3; i < 5; i++) arr[i] = i + 1;
    free(arr);
}

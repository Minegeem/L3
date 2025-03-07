#ifndef _COMPRESSION_H_
#define _COMPRESSION_H_
#include "Qtree.h"

int nb_bit_compression(Qtree *qtree, int i);
double taux_compression(Qtree *qtree, int size);
void compression(Qtree qtree, int pow, int size, char* name_file, double alpha);
void markChildren(Cell *arr, long int size);
#endif
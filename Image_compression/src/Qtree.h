#ifndef _QTREE_H_
#define _QTREE_H_
#include "Pixmap.h"

typedef struct cell {
    octet pixel; // niveau de gris dans le pixel x entre 0 et 255
    octet eps;   // reste de la division s'un pixel entre 0 et 4
    octet u;     // 1 si fils uniform 0 sinon
    double v; 
}Cell;

typedef struct qtree 
{
    Cell *array;   // liste de cellule
    long int size; // taille tout les noeuds (racine au feuilles)
} Qtree;

void initQtree(Qtree *qtree, int size);
int construire_qtree(Pixmap pixmap, int x, int y, int size, Cell* qtree, int i);
void printqtree(Qtree* tree);
int power(int number);
int intPow(int a, int b) ;
#endif
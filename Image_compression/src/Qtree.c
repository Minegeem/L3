#include "Qtree.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int power(int number) { // permet de savoir a quelle puissance est notre taille d'image longueur ou largeur
    int powercount = 0;
    while (number > 1) {
        number >>= 1;
        powercount++;
    }
    return powercount;
}

int intPow(int a, int b) { //permet de faire les puissances je n'ai pas utiliser pow de math.h car c'est avec des double
    int res = 1;
    for(int i = 0; i < b; i++) {
        res *= a;
    }
    return res;
}

long int sizeoflist(int n) { // taille de la liste avec comme focntion mathématique Somme(4^k) avec k = 0 --> k <= n
    long int result = 0;
    for(int i = 0; i <= n; i++) {
        result += intPow(4, i);
    }
    return result;
}

void initQtree(Qtree *qtree, int size) { // initialise qtree
    int po = power(size);
    qtree->size = sizeoflist(po);
    qtree->array = (Cell *)calloc(qtree->size, sizeof(Cell));
    if(!qtree->array) exit(1);
}

void exchange(Cell* tab, int a, int b) { // echange 2 cellule d'un tableau
    Cell tmp = tab[a];
    tab[a] = tab[b];
    tab[b] = tmp;
}

int u(Cell* qtree, int top_left, int top_right, int bottom_right, int bottom_left, int i){
    return qtree[top_left].pixel == qtree[top_right].pixel &&
                         qtree[top_left].pixel == qtree[bottom_right].pixel &&
                         qtree[top_left].pixel == qtree[bottom_left].pixel &&
                         qtree[top_left].u == 1 &&
                         qtree[top_right].u == 1 &&
                         qtree[bottom_right].u == 1 &&
                         qtree[bottom_left].u == 1 && qtree[i].eps == 0;
}

int construire_qtree(Pixmap pixmap, int x, int y, int size, Cell* cell, int i) {
    if (size == 1) {
        cell[i].pixel = pixmap.array[y * pixmap.height + x];
        cell[i].eps = 0;
        cell[i].u = 1;
        cell[i].v = 0.;
        return i;
    }

    int demi_size = size / 2;
    
    int iplus1 = 4 * i + 1;
    int iplus2 = 4 * i + 2;
    int iplus3 = 4 * i + 3;
    int iplus4 = 4 * i + 4;

    int top_left = construire_qtree(pixmap, x, y, demi_size, cell, iplus1);
    int top_right = construire_qtree(pixmap, x + demi_size, y, demi_size, cell, iplus2);
    int bottom_right = construire_qtree(pixmap, x + demi_size, y + demi_size, demi_size, cell, iplus3);
    int bottom_left = construire_qtree(pixmap, x, y + demi_size, demi_size, cell, iplus4);

    int sum = cell[top_left].pixel + cell[top_right].pixel + cell[bottom_right].pixel + cell[bottom_left].pixel;
    cell[i].pixel = sum / 4;
    cell[i].eps = sum % 4;
    cell[i].u = u(cell, top_left, top_right, bottom_right, bottom_left, i);
    double result = 0.;
    for(int j = 1; j < 5; j++) {
        Cell fils = cell[4 * i + j] ;
        result += (fils.v * fils.v) + ((cell[i].pixel - fils.pixel) * (cell[i].pixel - fils.pixel));
    }
    cell[i].v = sqrt(result) / 4.;
    return i;
}

void printqtree(Qtree* tree) { //print tree
    for(int i = 0; i < tree->size; i++) {
        printf("pix : %d, eps :%d, u: %d\n", tree->array[i].pixel, tree->array[i].eps, tree->array[i].u);
    }
}
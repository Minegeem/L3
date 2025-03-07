#ifndef _QTCMAP_H_
#define _QTCMAP_H_


#include "Qtree.h"


typedef struct  qtcmap{
    octet *array;
    int height;         //Parceque on a toujours carre.
}Qtcmap;

void readQTC(const char* filename, Qtree* tree,Qtcmap* map);

void printqtcmap(Qtcmap* tree);


#endif
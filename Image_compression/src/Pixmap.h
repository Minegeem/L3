#ifndef _PIXMAP_H_
#define _PIXMAP_H_
    
typedef unsigned char  octet; //crée car unsigned char trop long a écrire

typedef struct pixmap {
    octet *array; // liste de unsigned char (octet) permettant juste de stocker l'image apres lecture
    int width;    // largeur image
    int height;   // longueur image
} Pixmap;

void readPGM(const char *filename, Pixmap* map);
void printpixmap(Pixmap* tree);
#endif
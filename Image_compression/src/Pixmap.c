#include <stdio.h>
#include <stdlib.h>
#include "Pixmap.h"

void skipComments(FILE *file) {
    char c;
    while (fscanf(file, "%c", &c) == 1) {
        if (c == '#') {
            while (fscanf(file, "%c", &c) == 1 && c != '\n'); 
        } else if (c != ' ' && c != '\t' && c != '\n') {
            ungetc(c, file);
            break;
        }
    }
}

void readPGM(const char *filename, Pixmap* map) { // lis le pgm et le transforme en tableau selon si c'est un p5 et p2
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erreur lors de l'ouverture du fichier");
        return ;
    }
    char format[3];
    if (fscanf(file, "%2s", format) != 1) {
        printf("Erreur : impossible de lire le format de fichier.\n");
        fclose(file);
        return;
    }
    if (format[0] != 'P' || (format[1] != '2' && format[1] != '5')) {
        printf("Erreur : format PGM non pris en charge (seul P2 et P5 sont accepté).\n");
        fclose(file);
        return;
    }
    skipComments(file);
    int maxVal;
    fscanf(file, "%d %d %d", &(map->width), &(map->height), &maxVal);
    map->array = (octet *)calloc(map->width * map->height, sizeof(octet));
    if(! map->array) exit(1);
    if (format[1] == '5') { // Lecture du format P5
        for (int i = 0; i < map->width * map->height + 1; i++) {
            int byte = fgetc(file); 
            if(!i) continue;
            if (byte == EOF) {
                printf("Erreur : fin de fichier atteinte prématurément.\n");
                free(map->array);
                fclose(file);
                return;
            }
            map->array[i - 1] = (octet)byte; 
        }
    }
    printf("Image PGM lue avec succès : %dx%d pixels.\n", map->width, map->height);
    fclose(file);
    return ;
}

void printpixmap(Pixmap* tree) { //print tree
    for(int i = 0; i < tree->width *tree->width ; i++) {
        printf("%d ,",tree->array[i]);
    }
    printf("\n");
}




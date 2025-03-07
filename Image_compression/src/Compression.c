#include <stdio.h>
#include <stdlib.h>
#include "Qtree.h"
#include <time.h>
#include <math.h>

int nb_bit_compression(Qtree *qtree, int i) {
    int res = 0, c1 = 0, c2 = 0, c3 = 0,c4 = 0;
    int flag = 0;
    if(i >= qtree->size) return 0;
    if(i % 4 == 0 && i != 0) {
        if ((4 * i) + 4 >= qtree->size) {        
            res += 0; 
        }
        else if(qtree->array[i].eps) {
            res += 2;
        }
        else if(qtree->array[i].eps == 0 && qtree->array[i].u == 1) {
            res += 3;
            flag = 1;
        }
        else if(qtree->array[i].eps == 0) {
            res += 3;
        }
    }
    else{
        if ((4 * i) + 4 >= qtree->size) {        
            res += 8; 
        }
        else if(qtree->array[i].eps) {
            res += 10;
        }
        else if(qtree->array[i].eps == 0 && qtree->array[i].u == 1) {
            res += 11;
            flag = 1;
        }
        else if(qtree->array[i].eps == 0 && qtree->array[i].u == 0) {
            res += 11;
        }
    }
    if(!flag) {
        c1 = nb_bit_compression(qtree, (4 * i) + 1);
        c2 = nb_bit_compression(qtree, (4 * i) + 2);
        c3 = nb_bit_compression(qtree, (4 * i) + 3);
        c4 = nb_bit_compression(qtree, (4 * i) + 4);
    }
    return res + c1 + c2 + c3 + c4;
}

void printqtc(octet * qtc){
    for(int i = 0; i < 15; i++) {
        fprintf(stderr, "%u ",qtc[i]);
    }
    fprintf(stderr,"\n\n");
}

octet reverse_bits(octet byte) {
    octet reversed = 0;
    for (int i = 0; i < 8; i++) {
        reversed |= ((byte >> i) & 1) << (7 - i);
    }
    return reversed;
}

void write_bit(octet *buffer, int *bit_pos, octet bit, FILE* file) {
    *buffer <<= 1;
    if(bit & 1) {
        *buffer |= 1;
    }
    (*bit_pos)++;
    if (*bit_pos == 8) {
        fprintf(file, "%c", *buffer);
        *bit_pos = 0;
        *buffer = 0;
    }
}

void read_bit(octet *buffer, int *bit_pos, octet bit, FILE* file, int nb_tour){
    if(nb_tour == 8 ){
        bit = reverse_bits(bit);
    }
    if(nb_tour == 2) {
        switch(bit){
            case 1: bit = 2; break;
            case 2: bit = 1; break;
        };
    }
    for(int i = 0; i < nb_tour; i++) {
        write_bit(buffer, bit_pos, bit, file);
        bit = bit >> 1;
    }   
}

void qtc_compression(Qtree *qtree, octet* buffer, int* currentbyte, FILE* file) {
    for (int i = 0; i < qtree->size; i++)
    {
        if(qtree->array[i].eps && qtree->array[i].u) continue;
        if(i >= qtree->size) continue;
        if(i % 4 == 0 && i != 0) {
            if ((4 * i) + 4 >= qtree->size) {
            }
            else if(qtree->array[i].eps) { // 2
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
            }
            else if(qtree->array[i].eps == 0 && qtree->array[i].u == 1) { // 3
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
                read_bit(buffer, currentbyte, qtree->array[i].u, file, 1);
            }
            else if(qtree->array[i].eps == 0) { // 3
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
                read_bit(buffer, currentbyte, qtree->array[i].u, file, 1);
            }
        }
        else{
            if ((4 * i) + 4 >= qtree->size) { //8
                read_bit(buffer, currentbyte, qtree->array[i].pixel, file, 8);
            }
            else if(qtree->array[i].eps) { //10
                read_bit(buffer, currentbyte, qtree->array[i].pixel, file, 8);
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
            }
            else if(qtree->array[i].eps == 0 && qtree->array[i].u == 1) {//11
                read_bit(buffer, currentbyte, qtree->array[i].pixel, file, 8);
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
                read_bit(buffer, currentbyte, qtree->array[i].u, file, 1);
            }
            else if(qtree->array[i].eps == 0 && qtree->array[i].u == 0) {//11
                read_bit(buffer, currentbyte, qtree->array[i].pixel, file, 8);
                read_bit(buffer, currentbyte, qtree->array[i].eps, file, 2);
                read_bit(buffer, currentbyte, qtree->array[i].u, file, 1);
            }
        }
    }
}

void markChildren(Cell *arr, long int size) {
    // Parcourt le tableau
    for (int i = 0; i < size; i++) {
        if (arr[i].u == 1) {
            // Marque les "enfants" à 4i + [1, 2, 3, 4]
            for (int j = 1; j <= 4; j++) {
                int childIndex = 4 * i + j;
                if (childIndex < size) {
                    arr[childIndex].eps = 1; // Met eps à une valeur non nulle
                    arr[childIndex].u = 1;   // Met u à une valeur non nulle
                }
                else return;
            }
        }
    }
}

double moyenneVarriance(Qtree qtree) {
    double result = 0.f;
    int count = 0;
    for(int i = 0; i < qtree.size; i++) {
        if(4 * i + 1 < qtree.size) {
            result += qtree.array[i].v;
            count++;
        }
    }
    return result / (double)count;
}

double maximumVariance(Qtree qtree) {
    double result = 0.f;
    for(int i = 0; i < qtree.size; i++) {
        if(qtree.array[i].v > result) {
            result = qtree.array[i].v;
        }
    }
    return result;
}

int filtrage(Qtree *tree, int index, float sigma, float alpha) {
    if (tree->array[index].u == 1) {
        return 1; 
    }
    if (4 * index + 1 >= tree->size) {
        return 1;
    }
    int s = 0;
    for (int i = 1; i <= 4; i++) {
        s += filtrage(tree, 4 * index + i, sigma * alpha, alpha);
    }
    if (s < 4 || tree->array[index].v > sigma) {
        return 0;
    }
    tree->array[index].eps = 0;
    tree->array[index].u = 1;
    return 1;
}

void compression(Qtree qtree, int pow, int size, char* filename, double alpha) { 
    double medvar = moyenneVarriance(qtree);
    double maxvar = maximumVariance(qtree);
    filtrage(&qtree, 0, medvar / maxvar, alpha);
    
    FILE *file = fopen(filename, "w");
    time_t t;
    struct tm *tm_info;
    char buffer[100];
    time(&t);
    tm_info = localtime(&t);
    strftime(buffer, sizeof(buffer), "# %a %b %d %H:%M:%S %Y", tm_info);
    fprintf(file, "Q1\n");
    fprintf(file, "%s\n", buffer);
    int nb_bit = nb_bit_compression(&qtree,0);
    int nb_bit_before = nb_bit;
    while(nb_bit % 8) {
        nb_bit++;
    }
    fprintf(file, "# compression rate %.2lf%c\n", 100. * (double)nb_bit / (double)(size * 8),'%');
    fprintf(file, "%c", pow);
    markChildren(qtree.array, qtree.size);
    int current_byte = 0;
    octet buffer_bit = 0;
    qtc_compression(&qtree, &buffer_bit, &current_byte, file);
    buffer_bit = buffer_bit << (nb_bit - nb_bit_before);
    if(nb_bit != nb_bit_before) {
        fprintf(file, "%c",buffer_bit);
    }
    fclose(file);
}
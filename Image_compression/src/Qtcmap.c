#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Qtcmap.h"


void skipWhitespaceAndComments(FILE *file) {
    octet c;

    //sauter tous les charactere nul avant #
    while (fread(&c, 1, 1, file) == 1) {
        if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            continue;
        }
        if (c == '#') {
            break;
        }
        //si on rencontre un charactere utile, on met la pointeur avent lui.
        fseek(file, -1, SEEK_CUR); 
        return;
    }

    //skipper tous les lignes commence par #
    while (c == '#') {
        while (fread(&c, 1, 1, file) == 1 && c != '\n' && c != '\r');

        // verifier la prochaine charactere.
        long int pos = ftell(file);
        if (fread(&c, 1, 1, file) != 1) {
            break;
        }
        if (c != '#') {
            fseek(file, pos, SEEK_SET);
            break;
        }
    }
}
octet readBits(FILE *file, int numBits, int* end_of_file) {
    static octet buffer = 0;  //octet actuel
    static int nombre_rest = 8;   //  le nombre de bits restants dans l'octet actuel
    octet result = 0;   

    for (int i = 0; i < numBits; i++) {
        if (nombre_rest == 8) { // on a fini cette octet
            if (fread(&buffer, 1, 1, file) != 1) {
                if(feof(file)){ // si c'est la fin de fichier
                    *end_of_file = 1;
                    break;
                }else{
                    //on ne peut pas continuer a lire
                    exit(EXIT_FAILURE);
                }
            }
            nombre_rest = 0;
        }

        //chaque fois on met la bit a la fin de result
        result = (result << 1) | ((buffer >> (7 - nombre_rest)) & 1);
        nombre_rest++;
    }

    return result;
}

int getParentIndex(int i) { // on obtient la position de parent dans la qtree
    if (i == 0) {
        return -1; 
    }
    return (i - 1) / 4;
}


int power1(int number) { // permet de savoir a quelle puissance est notre taille d'image longueur ou largeur
    int powercount = 0;
    while (number > 1) {
        number >>= 1;
        powercount++;
    }
    return powercount;
}

long int intPow1(int a, int b) { //permet de faire les puissances je n'ai pas utiliser pow de math.h car c'est avec des double
    long int res = 1;
    for(int i = 0; i < b; i++) {
        res *= a;
    }
    return res;
}

void makeTree_last_line(FILE* file, Qtree* tree,int i,Qtcmap *map){ // traiter les nœuds feuilles de l'arbre
    int end_of_file = 0; // la fin de fichier
    int count_3 = 1; // Après chaque trois itérations, récupérer la valeur du quatrième nœud depuis le nœud parent.
    octet m; // la octet pour pixel
    int lastlineIndex = 0; // Cela doit être stocké dans une Map.
    while(1){ //oh
        if(i>=tree->size){
            break;
        }
        if(tree->array[getParentIndex(i)].u == 1){ //Si le nœud parent a un u égal à 1, alors tous ses nœuds enfants sont identiques
            tree->array[i].pixel = tree->array[ getParentIndex(i)].pixel;
            map->array[lastlineIndex]  =tree->array[i].pixel;
            tree->array[i].eps = 0;
            tree->array[i].u = 1;
            lastlineIndex++;
            i++;
            continue;
        }
        if((count_3 % 4) == 0){//S'il s'agit de celui omis.
            
            
            m = tree->array[getParentIndex(i)].pixel * 4 + tree->array[getParentIndex(i)].eps - tree->array[i-1].pixel - tree->array[i-2].pixel - tree->array[i - 3].pixel;

            
        }else{
        m = readBits(file,8,&end_of_file);}
        tree->array[i].pixel = m;
        map->array[lastlineIndex] = m;
        lastlineIndex++;
        tree->array[i].eps = 0;
        tree->array[i].u = 1;
        

        if(end_of_file == 1){
            break;
        }
        i++;
        count_3++;

    }
    
    

}
void initQtcmap(Qtcmap *map, int height) {
    if (!map) {
        fprintf(stderr, "Error: Qtcmap pointer is NULL.\n");
        return;
    }

    
    map->height = height;

    
    map->array = (octet *)calloc(height * height, sizeof(octet));
    if (!map->array) {
        fprintf(stderr, "Error: Memory allocation failed.\n");
        return;
    }
}

void makeTree(FILE* file, Qtree* tree,Qtcmap *map){
    int end_of_file = 0;
    int height = readBits(file, 8, &end_of_file);
    
    initQtree(tree,intPow1(2,height) );
    initQtcmap(map,intPow1(2,height));
  
    int parent_index = 0;
    octet m;
    octet e;
    octet u;
    int count_3 = 1;


    //Nous avons lu le nœud racine
    m = readBits(file,8,&end_of_file);
    
    e = readBits(file,2,&end_of_file);
    tree->array[0].pixel = m;
    tree->array[0].eps = e;
    if(e == 0){
            u = readBits(file,1,&end_of_file);
        }
    tree->array[0].u = 0;
    

    for(int i = 1; i < tree->size; i++){
        u = 0;
        

        if((4 * i )+ 1 >= tree->size){
            //Atteindre la dernière ligne.
           makeTree_last_line(file,tree,i,map);
            break;
        
        }
        parent_index = getParentIndex(i);
        if(tree->array[ parent_index].u == 1){
            
         
            tree->array[i].pixel = tree->array[ parent_index].pixel;
            tree->array[i].eps = 0;
            tree->array[i].u = 1;

        
            //count_3 ++;
            continue;
        }

        if((count_3 % 4) == 0){
            
            
            m = tree->array[parent_index].pixel * 4 + tree->array[parent_index].eps - tree->array[i-1].pixel - tree->array[i-2].pixel - tree->array[i - 3].pixel;

            
        }else{
        m = readBits(file,8,&end_of_file);
        }
        e = readBits(file,2,&end_of_file);
        if(i == 1){
        }

        if(e == 0){
            u = readBits(file,1,&end_of_file);
        }

        
        
        tree->array[i].pixel = m;
        tree->array[i].eps = e;
        tree->array[i].u = u;
        count_3 ++;


    }
}

void readQTC(const char*filename, Qtree* tree,Qtcmap* map){
    FILE *file = fopen(filename, "rb");
    if (!file) {
        perror("Erreur lors de l'ouverture du fichier");
        return ;
    }
    char format[2];
    if (fread(format, 1,2 ,file) != 2) {
        printf("Erreur : impossible de lire le format de fichier.\n");
        fclose(file);
        return;
    }
    if (format[0] != 'Q' || format[1] != '1') {
        printf("Erreur : format QTC non pris en charge (seul Q1 est accepté).\n");
        fclose(file);
        return;
    }
    

    
    skipWhitespaceAndComments(file);
    makeTree(file,tree,map);






}


   
void printqtcmap(Qtcmap* tree) { //print map
    for(int i = 0; i < tree->height *tree->height ; i++) {
        printf("%d ,",tree->array[i]);
    }
    printf("\n");
}





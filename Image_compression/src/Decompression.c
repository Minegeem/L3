#include "Decompression.h"


void printMatrix(int *matrix, int width, int height) {
    printf("Matrix (%d x %d):\n", height, width);
    for (int i = 0; i < height * height; i++) {        
       printf("%d ",matrix[i]);
       if(i%4 == 3){
        printf("\n"); 
       }
    }
}


bool allElementsEqual(octet *lst, int length) {
    octet firstElement = lst[0];
    for (int i = 1; i < length; i++) {
        if (lst[i] != firstElement) {
            return false; 
        }
    }
    return true; 
}
void trieArrayBlackAndWhite(int *outputMatrix, int index_start,int height, int taille,octet * lst,int length_lst){
    
    //Nous affichons une bordure grise autour des blocs identiques
    if(allElementsEqual(lst,length_lst)){
        for(int i = 0; i < taille;i++){
            outputMatrix[i+index_start] = 200;
        }
        for(int i = 0; i < taille;i++){
            outputMatrix[i*height+index_start] = 200;
        }
        for(int i = 0; i < taille;i++){
            outputMatrix[i+index_start+height*(taille-1)] = 200;
        }
        for(int i = 0; i < taille;i++){
            outputMatrix[i*height+index_start+taille - 1] = 200;
        }
        return;
    }
    if(length_lst == 4){
        outputMatrix[index_start] = 255;
        outputMatrix[index_start+1] = 255;
        outputMatrix[index_start+height+1] =255;
        outputMatrix[index_start + height] = 255;
 
        return;
    }
    trieArrayBlackAndWhite(outputMatrix, index_start, height,taille/2, lst, length_lst/4);
    trieArrayBlackAndWhite(outputMatrix, index_start+taille/2, height,taille/2, lst+length_lst/4, length_lst/4);
    trieArrayBlackAndWhite(outputMatrix, index_start + taille/2 *height + taille/2  , height,taille/2, lst + length_lst/2,length_lst/4);
    trieArrayBlackAndWhite(outputMatrix, index_start + taille/2 * height, height,taille/2, lst + (length_lst/4) * 3,length_lst/4);

}


void trieArray(int *outputMatrix, int index_start,int height, int taille,octet * lst,int length_lst){//Nous arrangeons dans l'ordre haut gauche, haut droit, bas droit, bas gauche.

// index_start :Nous savons que le point de départ ne changera pas de position.
// taille: La distance jusqu'au prochain point de départ
// lst: L'ensemble des données de ce bloc.

// main idea: À chaque récursion, divisez-le en quatre parties, puis organisez-les dans l'ordre
    if(length_lst == 4){
        outputMatrix[index_start] = lst[0];
        outputMatrix[index_start+1] = lst[1];
        outputMatrix[index_start+height+1] = lst[2];
        outputMatrix[index_start + height] = lst[3];
        return;
    }
    
    trieArray(outputMatrix, index_start, height,taille/2, lst, length_lst/4);
    trieArray(outputMatrix, index_start+taille/2, height,taille/2, lst+length_lst/4, length_lst/4);
    trieArray(outputMatrix, index_start + taille/2 *height + taille/2  , height,taille/2, lst + length_lst/2,length_lst/4);
    trieArray(outputMatrix, index_start + taille/2 * height, height,taille/2, lst + (length_lst/4) * 3,length_lst/4);

}

void writePGMBinary(const char *filename, Qtcmap map,int choice) {
    FILE *file = fopen(filename, "wb");
    if (file == NULL) {
        perror("Unable to open file");
        return;
    }


    fprintf(file, "P5\n");                  
    fprintf(file, "%d %d\n", map.height, map.height); 
    fprintf(file, "255\n");   
    
    //fprintf(file, "# Created By YANG Shuoliang version 1/1/2025\n" );

    
   
    int *outputMatrix = malloc(map.height * map.height * sizeof(int));
   
    if(choice){
        trieArray(outputMatrix,0,map.height,map.height,map.array,map.height*map.height);
    }else{
        trieArrayBlackAndWhite(outputMatrix,0,map.height,map.height,map.array,map.height*map.height);
    }

    //printMatrix(outputMatrix,map.height,map.height);
    octet *buffer = malloc(map.height * map.height * sizeof(octet));
    
    for (int i = 0; i < map.height * map.height; i++) {
        buffer[i] =(octet) outputMatrix[i]; 
    }
    fwrite(buffer, sizeof(octet), map.height * map.height, file);

    fclose(file);
    printf("Binary PGM file has been successfully written: %s\n", filename);
}
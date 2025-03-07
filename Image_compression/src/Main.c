#include "Compression.h"
#include <stdio.h>
#include <string.h>
#include "Decompression.h"
char* append_g(const char* outputfile){
    size_t length = strlen(outputfile);
    char* newPath = malloc(length + 3 + 1); 
   strncpy(newPath, outputfile, length - 4);
   newPath[length - 4] = '\0';
   strcat(newPath, "_g.pgm"); 
   return newPath;
}
// int main(int argc, char* argv[]) { // Pour compiler make && ./bin/projet
//     Pixmap imageIn;
//     Qtree qtreeIn;
//     Qtree qtreeOut;
//     Qtcmap map;
//     readPGM("PGM/ville.2048.pgm", &imageIn);
//     initQtree(&qtreeIn, imageIn.height);
//     construire_qtree(imageIn,0,0,imageIn.height,qtreeIn.array,0);
//     markChildren(qtreeIn.array, qtreeIn.size);
//     compression(qtreeIn, power(imageIn.width), imageIn.width * imageIn.height, "QTC_final/boat.512.qtc", 2);
//     readQTC("QTC_final/boat.512.qtc",&qtreeOut,&map);
//     //printqtree(&qtree2);
//     writePGMBinary("PGM_final/i.pgm",map,1);
// }
void printHelp() {
    printf("Usage: codec [options]\n");
    printf("Options:\n");
    printf("  -c                Encode input PGM file to QTC format\n");
    printf("  -u                Decode input QTC file to PGM format\n");
    printf("  -i <input_file>   Specify input file (PGM or QTC based on mode)\n");
    printf("  -o <output_file>  Specify output file (default: QTC/out.qtc or PGM/out.pgm)\n");
    printf("  -g                grille de segmentation\n");
    printf("  -v                mode bavard\n");
    printf("  -h                help\n");
    exit(0);
}

int main(int argc, char* argv[]) {
    int encode = 0, decode = 0, grille = 0, v = 0;
    char *inputFile = NULL, *outputFile = NULL;
    float alpha = 1.5;

    for (int i = 1; i < argc; i++) {
        if (strcmp(argv[i], "-c") == 0) {
            encode = 1;
        } else if (strcmp(argv[i], "-u") == 0) {
            decode = 1;
            if (i + 1 < argc) {
                inputFile = argv[++i];
            } else {
                fprintf(stderr, "Error: Missing input file after -u\n");
                return EXIT_FAILURE;
            }
        } else if (strcmp(argv[i], "-i") == 0) {
            if (i + 1 < argc) {
                inputFile = argv[++i];
            } else {
                fprintf(stderr, "Error: Missing input file after -i\n");
                return EXIT_FAILURE;
            }
        } else if (strcmp(argv[i], "-o") == 0) {
            if (i + 1 < argc) {
                outputFile = argv[++i];
            } else {
                fprintf(stderr, "Error: Missing output file after -o\n");
                return EXIT_FAILURE;
            }
        } else if (strcmp(argv[i], "-a") == 0) {
            if (i + 1 < argc) {
                alpha = atof(argv[++i]);  
                if (alpha <= 0) {
                    fprintf(stderr, "Error: Invalid alpha value. Must be greater than 0.\n");
                    return EXIT_FAILURE;
                }
            }else {
                fprintf(stderr, "Error: Missing alpha value after -a,using default 1.5\n");
            }
        }
        else if (strcmp(argv[i], "-g") == 0) {
            grille = 1;
        } else if (strcmp(argv[i], "-v") == 0) {
            v = 1;
        } else if (strcmp(argv[i], "-h") == 0) {
            printHelp();
        } else {
            fprintf(stderr, "Error: Unknown option %s\n", argv[i]);
            return EXIT_FAILURE;
        }
    }

    if (!inputFile) {
        fprintf(stderr, "Error: Input file must be specified with -i\n");
        return EXIT_FAILURE;
    }
    if (!outputFile) {
        outputFile = encode ? "QTC/out.qtc" : "PGM/out.pgm";
    }
    if(encode) {
       

        Pixmap imageIn;
        Qtree qtreeIn;
        Qtree qtreeOut;
        Qtcmap map;
        readPGM(inputFile, &imageIn);
        initQtree(&qtreeIn, imageIn.height);
        construire_qtree(imageIn, 0, 0, imageIn.height, qtreeIn.array, 0);
        markChildren(qtreeIn.array, qtreeIn.size);
        compression(qtreeIn, power(imageIn.width), imageIn.width * imageIn.height,outputFile, alpha);

        if(grille){
            readQTC(outputFile,&qtreeOut,&map);
            writePGMBinary(append_g(outputFile),map,0);
        }
        if(v) printf("Encode %s\nHeight: %d\nRate: %f\n",inputFile,imageIn.height,alpha);
    }else if (decode){
        if(v) printf("inputFile:%s,and you will see outputFile: %s\n",inputFile,outputFile);
        Qtree qtreeOut;
        Qtcmap map;
        readQTC(inputFile, &qtreeOut, &map);
        writePGMBinary(outputFile, map,1);
        if(grille){
            writePGMBinary(append_g(outputFile),map,0);
        }
        
    }

    return EXIT_SUCCESS;
}
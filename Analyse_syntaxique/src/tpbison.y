%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "tree.h"
int yylex(void);
void yyerror(const char*);
extern int column; 
extern int yylineno;  // Ligne courante
Node * racine;
int SI = 0, S2 = 0;
extern FILE *yyin;
%}


%union {
    Node* node;
    char byte;
    int num;
    char ident[64];
    char comp[3];
}

%precedence NOELSE
%precedence ELSE

%token <byte> BYTE CHARACTER ADDSUB DIVSTAR
%token <num> NUM
%token <ident> IDENT IF ELSE WHILE RETURN VOID TYPE STATIC AND OR
%token <comp> ORDER EQ COMP

%type <node> Prog DeclVars DeclVars2 Static Declarateurs DeclFoncts DeclFonct EnTeteFonct Parametres ListTypVar Corps SuiteInstr Instr Exp TB FB M E T F Arguments ListExp

%%
Prog:  DeclVars DeclFoncts { racine = $$ = makeNode(Prog); addChild($$, $1); addChild($$, $2);}
    ;
DeclVars:
       DeclVars TYPE Declarateurs ';' { Node *node = makeNode2(ident, '\0', 0, $2 , "\0"); if(!SI) { $$ = makeNode(DeclVars); addChild($$, $1);  SI = 1; } addChild($$, node); addChild(node, $3);}
    |                                 { $$ = NULL; if(SI) SI = 0;}
    ;
DeclVars2:
      DeclVars2 Static TYPE Declarateurs ';' {
          if ($1 == NULL) { $$ = makeNode(DeclVars2); } else { $$ = $1; } Node *typeNode = makeNode2(ident, '\0', 0, $3, "\0"); 
          if ($2) { addChild($2, typeNode); }
          if ($2) { addChild($$, $2); } else { addChild($$, typeNode); } addChild(typeNode, $4); }
    | { $$ = makeNode(DeclVars2); } 
    ;
Static:
      STATIC  { Node *nodetype = makeNode2(ident,'\0',0, $1 ,"\0"); $$ = nodetype; }
    |         { $$ = NULL; }
    ;
Declarateurs:
       Declarateurs ',' IDENT { Node *nodeident = makeNode2(ident,'\0',0, $3 ,"\0"); addSibling(nodeident, $1); $$ = nodeident; }
    |  IDENT                  { Node *nodeident2 = makeNode2(ident,'\0',0, $1 ,"\0"); $$ = nodeident2;}
    ;
DeclFoncts:
       DeclFonct { if (!S2) { $$ = makeNode(DeclFoncts); S2 = 1; } addChild($$, $1); }
    | DeclFoncts DeclFonct { if (!S2) { $$ = makeNode(DeclFoncts); S2 = 1; } addChild($$, $2); }
    ;
DeclFonct:
       EnTeteFonct Corps     { $$ = makeNode(DeclFonct); addChild($$, $1); addChild($$,$2); }
    ;
EnTeteFonct:
       TYPE IDENT '(' Parametres ')'   { $$ = makeNode(EnTeteFonct); Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild($$, node); addChild(node, makeNode2(ident,'\0',0, $2 ,"\0")); addChild($$, $4); }
    |  VOID IDENT '(' Parametres ')'   { $$ = makeNode(EnTeteFonct); Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild($$, node); addChild(node, makeNode2(ident,'\0',0, $2 ,"\0")); addChild($$, $4); }
    ;
Parametres:
       VOID         { $$ = makeNode(Parametres); addChild($$, makeNode2(ident,'\0',0, $1 ,"\0")); }
    |  ListTypVar   { $$ = makeNode(Parametres); addChild($$, $1); }
    ;
ListTypVar:
       ListTypVar ',' TYPE IDENT  { Node *nodeident = makeNode2(ident,'\0',0, $4 ,"\0"); Node *nodeident2 = makeNode2(ident,'\0',0, $3 ,"\0"); addChild(nodeident2, $1); addChild(nodeident2, nodeident); $$ = nodeident2; }
    |  TYPE IDENT                 { Node *nodeident = makeNode2(ident,'\0',0, $1 ,"\0"); Node *nodeident2 = makeNode2(ident,'\0',0, $2 ,"\0"); addChild(nodeident, nodeident2); $$ = nodeident2; }
    ;
Corps: '{' DeclVars2 SuiteInstr '}'  { $$ = makeNode(Corps);  addChild($$, $2); addChild($$, $3); }
    ;
SuiteInstr:
      SuiteInstr Instr  { if ($1 == NULL) { $$ = makeNode(SuiteInstr); } else { $$ = $1; } addChild($$, $2); }
    |                   { $$ = makeNode(SuiteInstr); }
    ;
Instr:
       IDENT '=' Exp ';'                 { Node *node = makeNode2(comp, '\0', 0,"\0", "="); addChild(node, makeNode2(ident, '\0', 0, $1, "\0")); addChild(node, $3); $$ = node; }
    |  IF '(' Exp ')' Instr %prec NOELSE { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild(node, $3); addChild(node, $5); $$ = node; }
    |  IF '(' Exp ')' Instr ELSE Instr   { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); Node *node2 = makeNode2(ident,'\0',0, $6 ,"\0"); addChild(node, $3); addChild(node, $5); addChild(node, node2); addChild(node, $7); $$ = node; }
    |  WHILE '(' Exp ')' Instr           { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild(node, $3); addChild(node, $5); $$ = node; }
    |  IDENT '(' Arguments  ')' ';'      { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild(node, $3); $$ = node; }
    |  RETURN Exp ';'                    { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild(node, $2); $$ = node; }
    |  RETURN ';'                        { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); $$ = node; }
    |  '{' SuiteInstr '}'                { $$ = $2; }
    |  ';'                               { $$ = NULL; }
    ;
Exp :  Exp OR TB    { Node *node = makeNode2(ident,'\0',0, $2 ,"\0"); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  TB           { $$ = $1; }
    ;
TB  :  TB AND FB    { Node *node =makeNode2(ident,'\0',0, $2 ,"\0"); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  FB           { $$ = $1; }
    ;
FB  :  FB EQ M      { Node *node = makeNode2(comp,'\0',0,"\0" ,$2); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  M            { $$ = $1; }
    ;
M   :  M ORDER E    { Node *node = makeNode2(comp,'\0',0,"\0" ,$2); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  E            { $$ = $1; }
    ;
E   :  E ADDSUB T   { Node *node = makeNode2(byte, $2, 0, "\0", "\0"); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  T            { $$ = $1; }
    ;    
T   :  T DIVSTAR F  { Node *node = makeNode2(byte, $2, 0, "\0", "\0"); addChild(node, $1); addChild(node, $3); $$ = node; }
    |  F            { $$ = $1; }
    ;
F   :  ADDSUB F     { Node *node = makeNode2(byte, $1, 0, "\0", "\0"); addChild(node, $2); $$ = node; }
    |  '!' F        { Node *node = makeNode2(comp, '\0', 0,"\0","!"); addChild(node, $2); $$ = node; }
    |  '(' Exp ')'  { $$ = $2; }
    |  NUM          { Node *node = makeNode2(num, '\0', $1, "\0", "\0"); $$ = node; }
    |  CHARACTER    { Node *node = makeNode2(byte, $1, 0, "\0", "\0"); $$ = node; }
    |  IDENT        { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); $$ = node; }
    |  IDENT '(' Arguments  ')'  { Node *node = makeNode2(ident,'\0',0, $1 ,"\0"); addChild(node, $3); $$ = node; }
    ;
Arguments:
       ListExp  { $$ = $1; }
    |           { $$ = NULL; }
    ;
ListExp:
       ListExp ',' Exp  { addChild($1, $3); $$ = $1; }
    |  Exp              { $$ = $1; }
    ;
%%

void yyerror(const char *s) {
    fprintf(stderr, "Erreur de syntaxe à la ligne %d, position %d : %s\n", yylineno, column, s);
}

void afficher_aide() {
    printf("Executer : tpcas [options] [fichier.tpc]\n");
    printf("Choisir une Option :\n");
    printf("  -h ou --help      Affiche cette aide et termine l'exécution.\n");
    printf("  -t ou --tree      Affiche l'arbre syntaxique.\n");
    printf("  [fichier.tpc]     Fichier source à analyser PAS IMPLEMENTE.\n");
}

int main(int argc, char *argv[] ) {
    int tree = 0;
    int file = 0;
    char* filechar = NULL;
        for (int i = 1; i < argc; i++) {
        int size = strlen(argv[i]);
        if (strcmp(argv[i], "-h") == 0 || strcmp(argv[i], "--help") == 0) {
            afficher_aide();
            return 0;
        }
        else if (strcmp(argv[i], "-t") == 0 || strcmp(argv[i], "--tree") == 0) {
            tree = 1;
        }
        else if (size >= 4 && strcmp(argv[i] + size - 4, ".tpc") == 0) {
            file = 1;
            filechar = (char *)malloc(size + 1);
            if (!filechar) {
                perror("Erreur d'allocation mémoire");
                exit(1);
            }
            strcpy(filechar, argv[i]);
        }
        else if (argv[i][0] == '-') {
            fprintf(stderr, "Problème au niveau de l'argument : %s %d\n", argv[i], i);
            return 2;  
        }
        else {
            fprintf(stderr, "Argument inconnu : %s\n", argv[i]);
            return 2; 
        }
    }
    if(file) {
        char *option = (tree) ? "-t" : " ";
        int len = strlen("./bin/tpcas < ") + strlen(filechar) + strlen(" ") + strlen(option) + 1;
        char cmd[len + 1];
        snprintf(cmd, len, "./bin/tpcas < %s %s", filechar, option);
        system(cmd);
        free(filechar);
    } else if (yyparse() == 0 && tree) { 
        printTree(racine); 
    } else {
        return 1;
    }
    return 0;
}
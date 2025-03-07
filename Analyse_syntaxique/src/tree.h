/* tree.h */
#include <stdbool.h>

typedef enum {
    Prog,         // Programme principal
    type_kw,
    Static,
    DeclVars,     // Déclarations de variables
    DeclVars2,    // Déclarations de variables avec STATIC
    static_kw,       // STATIC
    Declarateurs, // Déclarateurs
    DeclFoncts,   // Déclarations de fonctions
    DeclFonct,    // Déclaration de fonction
    EnTeteFonct,  // En-tête de fonction
    Parametres,   // Paramètres de fonction
    ListTypVar,   // Liste de types et variables
    Corps,        // Corps de la fonction
    SuiteInstr,   // Suite d'instructions
    Instr,        // Instruction
    Exp,          // Expression
    TB,           // Expression de type TB
    FB,           // Expression de type FB
    M,            // Expression de type M
    E,            // Expression de type E
    T,            // Expression de type T
    F,            // Expression de type F
    Arguments,    // Arguments de fonction
    ListExp,      // Liste d'expressions
    ident,        // Identifiant
    num,          // Nombre
    character,         // Caractère
    eq,           // Égalité
    order,        // Ordre
    addsub,       // Addition/Soustraction
    divstar,      // Multiplication/Division
    or,           // OU logique
    and,          // ET logique
    not,          // NOT logique
    void_kw,
    if_kw,
    else_kw,
    while_kw,
    return_kw,
    comp,
    byte
} label_t;

typedef struct Node {
  label_t label;
  char byte;
  int num;
  char ident[64];
  char comp[3];
  struct Node *firstChild, *nextSibling;
  int lineno;
} Node;

Node *makeNode(label_t label);
Node *makeNode2(label_t label, char byte, int num, char* ident, char* comp);
void addSibling(Node *node, Node *sibling);
void addChild(Node *parent, Node *child);
void deleteTree(Node*node);
void printTree(Node *node);

#define FIRSTCHILD(node) node->firstChild
#define SECONDCHILD(node) node->firstChild->nextSibling
#define THIRDCHILD(node) node->firstChild->nextSibling->nextSibling
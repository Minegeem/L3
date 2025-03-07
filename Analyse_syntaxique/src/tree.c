/* tree.c */
#include <string.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "tree.h"
extern int lineno;       /* from lexer */

static const char *StringFromLabel[] = {
  "Prog",         // Programme principal
  "type",
  "Static",
  "DeclVars",     // Déclarations de variables
  "DeclVars2",    // Déclarations de variables avec STATIC
  "static_kw",       // STATIC
  "Declarateurs", // Déclarateurs
  "DeclFoncts",   // Déclarations de fonctions
  "DeclFonct",    // Déclaration de fonction
  "EnTeteFonct",  // En-tête de fonction
  "Parametres",   // Paramètres de fonction
  "ListTypVar",   // Liste de types et variables
  "Corps",        // Corps de la fonction
  "SuiteInstr",   // Suite d'instructions
  "Instr",        // Instruction
  "Exp",          // Expression
  "TB",           // Expression de type TB
  "FB",           // Expression de type FB
  "M",            // Expression de type M
  "E",            // Expression de type E
  "T",            // Expression de type T
  "F",            // Expression de type F
  "Arguments",    // Arguments de fonction
  "ListExp",      // Liste d'expressions
  "ident",        // Identifiant
  "num",          // Nombre
  "character",         // Caractère
  "eq",           // Égalité
  "order",        // Ordre
  "addsub",       // Addition/Soustraction
  "divstar",      // Multiplication/Division
  "or",           // OU logique
  "and",          // ET logique
  "not",          // NOT logique
  "void_kw",
  "if_kw",
  "else_kw",
  "while_kw",
  "return_kw",
  "comp",
  "byte"
};




Node *makeNode(label_t label) {
  Node *node = malloc(sizeof(Node));
  if (!node) {
    printf("Run out of memory\n");
    exit(1);
  }
  node->label = label;
  node->byte = '\0';
  node->num = 0;
  strcpy(node->ident, "\0");
  strcpy(node->comp, "\0");
  node-> firstChild = node->nextSibling = NULL;
  node->lineno=lineno;
  return node;
}

Node *makeNode2(label_t label, char byte, int num, char* ident, char* comp) {
  Node *node = malloc(sizeof(Node));
  if (!node) {
    printf("Run out of memory\n");
    exit(1);
  }
  
  node->label = label;
  node->byte = byte;
  node->num = num;
  strcpy(node->ident, ident);
  strcpy(node->comp, comp);
  node-> firstChild = node->nextSibling = NULL;
  node->lineno=lineno;
  return node;
}

void addSibling(Node *node, Node *sibling) {
  Node *curr = node;
  while (curr->nextSibling != NULL) {
    curr = curr->nextSibling;
  }
  curr->nextSibling = sibling;
}

void addChild(Node *parent, Node *child) {
  if (parent->firstChild == NULL) {
    parent->firstChild = child;
  }
  else {
    addSibling(parent->firstChild, child);
  }
}

void deleteTree(Node *node) {
  if (node->firstChild) {
    deleteTree(node->firstChild);
  }
  if (node->nextSibling) {
    deleteTree(node->nextSibling);
  }
  free(node);
}

void printTree(Node *node) {
  static bool rightmost[128]; // tells if node is rightmost sibling
  static int depth = 0;       // depth of current node
  for (int i = 1; i < depth; i++) { // 2502 = vertical line
    printf(rightmost[i] ? "    " : "\u2502   ");
  }
  if (depth > 0) { // 2514 = L form; 2500 = horizontal line; 251c = vertical line and right horiz 
    printf(rightmost[depth] ? "\u2514\u2500\u2500 " : "\u251c\u2500\u2500 ");
  }
  if(strcmp(StringFromLabel[node->label], "num") == 0) {  printf("%d\n", node->num);  }
  else if(strcmp(StringFromLabel[node->label], "byte") == 0) { printf("%c\n", node->byte); }
  else if(strcmp(StringFromLabel[node->label], "comp") == 0) { printf("%s\n", node->comp); }
  else if(strcmp(StringFromLabel[node->label], "ident") == 0) { printf("%s\n", node->ident); }
  else { printf("%s\n", StringFromLabel[node->label]); };
  depth++;
  for (Node *child = node->firstChild; child != NULL; child = child->nextSibling) {
    rightmost[depth] = (child->nextSibling) ? false : true;
    printTree(child);
  }
  depth--;
}
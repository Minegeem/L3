%{
#include "tree.h"
#include "tpbison.tab.h"
void yyerror(const char *msg);
int yylex();
extern int column;
int column = 1;

int compteurLigne = 1;
int positionLigne = 1;
int lineno;
%}

%option nounput
%option noinput
%option yylineno
%x COMM

%%

"/*"        { BEGIN COMM;};
"//".*      ;                   //ignore les commentaire //

"if"        { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return IF; }
"else"      { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return ELSE; }
"return"    { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return RETURN; }
"while"     { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return WHILE; }
"static"    { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return STATIC; }


"void"      { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return VOID; }
"int"       { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return TYPE; }
"char"      { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return TYPE; }


[0-9]+      { yylval.num = atoi(yytext); column += yyleng; return NUM; }
'([^\\]|\\[nt\'])'    { yylval.byte = yytext[1]; column += yyleng; return CHARACTER; } // reconnait 'a' '\n' '\t' '\''

[ \t]+      { column += yyleng; };  // ignorer les espaces et les tab
\n          { column = 1; compteurLigne++; positionLigne = 1; }


"=="        { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return EQ; }
"!="        { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return EQ; }

"<="        { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return ORDER; }
">="        { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return ORDER; }
"<"         { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return ORDER; }
">"         { strncpy(yylval.comp, yytext, sizeof(yylval.comp) - 1); column += yyleng; return ORDER; }

"+"         { yylval.byte = yytext[0]; column++; return ADDSUB; }
"-"         { yylval.byte = yytext[0]; column++; return ADDSUB; }

"*"         { yylval.byte = yytext[0]; column++; return DIVSTAR; }
"/"         { yylval.byte = yytext[0]; column++; return DIVSTAR; }
"%"         { yylval.byte = yytext[0]; column++; return DIVSTAR; }

"||"         { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return OR; }
"&&"         { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return AND; }

";"         { column++; return ';'; }
","         { column++; return ','; }
"!"         { column++; return '!'; }
"("         { column++; return '('; }
")"         { column++; return ')'; }
"{"         { column++; return '{'; }
"}"         { column++; return '}'; }
"="         { column++; return '='; }


[a-zA-Z_][a-zA-Z0-9_]* { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1); column += yyleng; return IDENT; }


<COMM>"*/"  { BEGIN(INITIAL); }  // Fin du commentaire 
<COMM>.     ;                    // Ignore tous dans les commentaires
<COMM>\n    { compteurLigne++; } // Compte les nouvelles lignes dans les commentaires


.           { fprintf(stderr, "Erreur lexicale caractère non reconnu '%s' à la ligne %d, position %d\n", yytext, compteurLigne, positionLigne); exit(1);
            }



%%

//erreur lexicale -> lex -> i en trop dans un mot
//erreur syntaxique ->bison -> if feryel == 1 erreur car char == int faux
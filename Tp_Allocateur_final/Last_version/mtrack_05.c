#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
//#include "mtrack_04.h"


typedef struct _track_cell{
    void* adresse;
    size_t size;
    int actif;
    struct _track_cell* next;
} _track_cell;

typedef struct {
    _track_cell* head;
    unsigned int true_free_compteur;
    unsigned int false_free_compteur;
    unsigned int true_malloc_compteur;
    unsigned int total_memory_alloc;
    unsigned int total_memory_free;
} _track_table;



bool TRACEUR = false;
_track_table _track_TABLE;

_track_cell* _init_cell_base(void* adresse, size_t size, int actif) {
    _track_cell* cell = (_track_cell*)malloc(sizeof(_track_cell));
    if(!cell) exit(1);
    cell->actif = actif;
    cell->adresse = adresse;
    cell->size = size;
    cell->next = NULL; 
    return cell;
}

void _add_cell_(_track_cell* adr) {
    _track_cell* tmp = _track_TABLE.head;
    if(!(tmp)) {
    	_track_TABLE.head = adr;
    	return;
    }
    while(tmp->next) {
    	tmp = tmp->next;
    }
    tmp->next = adr;
}

void _init_cell_table() { 
    _track_TABLE.head = NULL; 
    _track_TABLE.true_free_compteur = 0; 
    _track_TABLE.false_free_compteur = 0; 
    _track_TABLE.true_malloc_compteur = 0; 
    _track_TABLE.total_memory_alloc = 0; 
    _track_TABLE.total_memory_free = 0;
}

void _cell_bilan(void) {
    printf("–––––––––––––––––––––––\n");
    printf("BILAN FINAL\n");
    printf("total mémoire allouée : %d octet%c\n", _track_TABLE.total_memory_alloc, ((_track_TABLE.total_memory_alloc) > 1)?'s':' ');
    printf("total mémoire libérée : %d octet%c\n", _track_TABLE.total_memory_free, ((_track_TABLE.total_memory_free) > 1)?'s':' ');
    printf("ratio                 : %2.f %c\n", ((float)_track_TABLE.total_memory_free/(float)_track_TABLE.total_memory_alloc)*100, '%');
    printf("<malloc>              : %d appel%c\n", _track_TABLE.true_malloc_compteur, ((_track_TABLE.true_malloc_compteur) > 1)?'s':' ');
    printf("<free>                : %d appel%scorrect\n", _track_TABLE.true_free_compteur, ((_track_TABLE.true_free_compteur) > 1)?"s ":" ");
    printf("<free>                : %d appel%sincorrect\n", _track_TABLE.false_free_compteur, ((_track_TABLE.false_free_compteur) > 1)?"s ":" ");
    printf("–––––––––––––––––––––––\n");
}

void _clean_(void) {
    _track_cell* cell = _track_TABLE.head;
    _track_cell* tmp;
    while(cell) {
        tmp = cell;
        cell = cell->next;
        free(tmp);
    }
}

void activer_traceur(void) {
    if (!TRACEUR) {
        TRACEUR = true;
        fprintf(stderr,"-activate tracker- [VERS.5] -\n");
        fprintf(stderr,"---------------------------------------------------------------------\n");
        atexit(_cell_bilan);
        atexit(_clean_);
    }
}

void* _clone_malloc(char* fich, const char* fonc, int line, size_t size) {
  	if(!TRACEUR) activer_traceur();
    void* adr =  malloc(size);
    if(!adr) exit(1);
    _track_cell* adr_actuelle = _track_TABLE.head;
    while(adr_actuelle && (adr_actuelle->adresse != adr)) {adr_actuelle = adr_actuelle->next;}

    if(!(adr_actuelle)) {
        adr_actuelle = _init_cell_base(adr, size, 1);
    	_add_cell_(adr_actuelle);
	}

    adr_actuelle->actif = 1;
    adr_actuelle->size = size;
    _track_TABLE.true_malloc_compteur++;
    _track_TABLE.total_memory_alloc++;
    fprintf(stderr, "in file <%s> function <%s> line <0%d> – (call#0%d) malloc(%ld)->%p\n", fich, fonc, line, _track_TABLE.true_malloc_compteur, adr_actuelle->size, adr);
    return adr;
} 

void _clone_free(char*file, char* fonc, int line, void* adr) {
  	if(!TRACEUR) activer_traceur();
  	_track_cell* tmp = _track_TABLE.head;
    while(tmp && (tmp->adresse) != adr) {
    	tmp = tmp->next;
    }
    if(!tmp || !(tmp->actif)) {
      _track_TABLE.false_free_compteur++;
      fprintf(stderr, "in file <%s> function <%s> line <0%d> – (call#0%d) free(%p) - ERREUR : adresse illégale ->ignoré\n",file, fonc, line, _track_TABLE.true_free_compteur+_track_TABLE.false_free_compteur, adr);
      return;
    }
    tmp->actif = 0;
    _track_TABLE.total_memory_free++;
    _track_TABLE.true_free_compteur++;
    fprintf(stderr, "in file <%s> function <%s> line <0%d> – (call#0%d) free(%p)\n",file, fonc, line, _track_TABLE.true_free_compteur, tmp->adresse);
}


#include <stdio.h>
#include <stdlib.h>
#include "track_01.h"

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

void _init_cell_table() { 
    _track_TABLE.head = NULL; 
    _track_TABLE.true_free_compteur = 0; 
    _track_TABLE.false_free_compteur = 0; 
    _track_TABLE.true_malloc_compteur = 0; 
    _track_TABLE.total_memory_alloc = 0; 
    _track_TABLE.total_memory_free = 0;
}

void _cell_bilan() {
    printf("–––––––––––––––––––––––\n");
    printf("BILAN FINAL\n");
    printf("total mémoire allouée : %d octet%c\n", _track_TABLE.total_memory_alloc, ((_track_TABLE.total_memory_alloc) > 1)?'s':' ');
    printf("total mémoire libérée : %d octet%c\n", _track_TABLE.total_memory_free, ((_track_TABLE.total_memory_free) > 1)?'s':' ');
    printf("ratio                 : %d %c\n", _track_TABLE.total_memory_alloc/_track_TABLE.total_memory_free, '%');
    printf("<malloc>              : %d appel%c\n", _track_TABLE.true_malloc_compteur, ((_track_TABLE.true_malloc_compteur) > 1)?'s':' ');
    printf("<free>                : %d appel%scorrect\n", _track_TABLE.true_free_compteur, ((_track_TABLE.true_free_compteur) > 1)?"s ":" ");
    printf("<free>                : %d appel%sincorrect\n", _track_TABLE.false_free_compteur, ((_track_TABLE.false_free_compteur) > 1)?"s ":" ");
    printf("–––––––––––––––––––––––\n");
}

void _clean_() {
    _track_cell* cell = _track_TABLE.head;
    _track_cell* tmp;
    while(cell) {
        tmp = cell;
        cell = cell->next;
        free(tmp);
    }
}
void _clone_malloc(int size) {
    void* adr =  malloc(size);
    if(!adr) exit(1);
    _track_cell* tmp = _track_TABLE.head;
    while(tmp && tmp->adresse == adr) { 
        tmp = tmp->next;
    }
} 

int main(){
    return 0;
}
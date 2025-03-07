#ifndef _MTRACK_03_H_
    #define _MTRACK_03_H_
#include <stdio.h>
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

void* _clone_malloc(size_t size);
void _clone_free(void* adr);
void activer_traceur(void);
void _clean_(void);
void _cell_bilan(void);
void _init_cell_table();
void _add_cell_(_track_cell* adr);
_track_cell* _init_cell_base(void* adresse, size_t size, int actif);
#endif
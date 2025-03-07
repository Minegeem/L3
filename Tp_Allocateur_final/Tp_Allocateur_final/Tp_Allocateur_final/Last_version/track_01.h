#ifndef _TRACK_01_H_
    #define _TRACK_01_H_

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

#endif
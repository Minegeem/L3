#Traqueur d'Allocation en C

Ce projet est un traqueur d'allocation mémoire implémenté en langage C. 
Il permet de surveiller les allocations et les libérations de mémoire afin de détecter des fuites mémoire et de garantir une gestion efficace de la mémoire dans vos programmes.

##Fonctionnalités

Surveillance des allocations : Traque les appels à malloc, calloc et realloc.

Surveillance des libérations : Traque les appels à free.

Rapport sur les fuites : Génère un rapport des blocs mémoire non libérés à la fin de l'exécution.

##Utilisation

Écrivez votre code normalement avec les calloc malloc realloc et free habituels il les changeras de lui même

Exécutez votre programme normalement. Si des fuites mémoire existent, elles seront rapportées à la fin de l'exécution.

##Structure des fichiers

mtrack_final.h : Déclarations des fonctions et macros.

mtrack_final.c : Implémentation des fonctions de suivi.

track final.c : Exemple d'utilisation du traqueur.

##Rapport sur les fuites

À la fin de l'exécution, le traqueur génère un rapport indiquant :

Les blocs mémoire non libérés.

Les emplacements des allocations (fichier et ligne).

Exemple de sortie :

-activate tracker- [VERS.5] -
---------------------------------------------------------------------
in file <src/track_final.c> function <main> line <02> – (call#01) malloc(1)->0x7fffd62622a0
in file <src/track_final.c> function <main> line <03> – (call#02) malloc(1)->0x7fffd62622f0
in file <src/track_final.c> function <main> line <04> – (call#01) free(0x7fffd62622a0)
in file <src/track_final.c> function <main> line <06> – (call#02) free(0x7fffd62622f1) - ERREUR : adresse illégale ->ignoré
–––––––––––––––––––––––
BILAN FINAL
total mémoire allouée : 2 octets
total mémoire libérée : 1 octet
ratio                 : 50 %
<malloc>              : 2 appels
<calloc>              : 0 appel
<realloc>             : 0 appel
<free>                : 1 appel correct
<free>                : 1 appel incorrect
–––––––––––––––––––––––

##Limitations

Le traqueur ne surveille pas les allocations effectuées hors des fonctions surveillées (comme strdup).

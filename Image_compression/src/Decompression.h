#ifndef _DECOMPRESSION_H_
#define _DECOMPRESSION_H_


#include "Qtcmap.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

void writePGMBinary(const char *filename, Qtcmap map,int choice);
#endif
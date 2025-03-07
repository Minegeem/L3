EXEC = bin/tpcas
OBJECTS = obj/lexer.o obj/parser.o obj/tree.o
CFLAGS = -Wall
LDLIBS = -ll

SRC_DIR = src
OBJ_DIR = obj
BIN_DIR = bin

LEX_FILE = $(SRC_DIR)/project.lex
YACC_FILE = $(SRC_DIR)/tpbison.y
C_FILES = $(SRC_DIR)/lexer.c $(BISON_SOURCE) $(SRC_DIR)/tree.c

BISON_HEADER = $(SRC_DIR)/tpbison.tab.h
BISON_SOURCE = $(SRC_DIR)/tpbison.tab.c
LEX_SOURCE = $(SRC_DIR)/lexer.c

all: $(EXEC)

$(EXEC): $(OBJECTS)
	gcc -o $@ $(OBJECTS) $(LDLIBS)

$(LEX_SOURCE): $(LEX_FILE) $(BISON_HEADER)
	flex -o $@ $<

$(BISON_HEADER) $(BISON_SOURCE): $(YACC_FILE)
	bison -d -rall -Wcex -o $(BISON_SOURCE) $<

$(OBJ_DIR)/lexer.o: $(SRC_DIR)/lexer.c $(SRC_DIR)/tree.h
	gcc -c $(CFLAGS) -o $@ $<

$(OBJ_DIR)/parser.o: $(BISON_SOURCE) $(SRC_DIR)/tree.h
	gcc -c $(CFLAGS) -o $@ $<

$(OBJ_DIR)/tree.o: $(SRC_DIR)/tree.c $(SRC_DIR)/tree.h
	gcc -c $(CFLAGS) -o $@ $<

clean:
	rm -f $(OBJ_DIR)/*.o $(SRC_DIR)/lexer.c $(BISON_SOURCE) $(BISON_HEADER) $(SRC_DIR)/tpbison.tab.c $(SRC_DIR)/tpbison.tab.h $(EXEC)

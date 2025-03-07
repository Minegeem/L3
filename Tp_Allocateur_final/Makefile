CC = gcc
CFLAGS = -Wall -fPIC
OBJECT = obj/track_final.o obj/mtrack_final.o
ARG = -include src/mtrack_final.h
LFLAGS := -Wl,-rpath,lib/ -Llib -lstack

all: lib/libstack.so bin/main 

obj/track_final.o : src/track_final.c
	$(CC) $(CFLAGS) $(ARG) -c src/track_final.c -o obj/track_final.o 

obj/mtrack_final.o : src/mtrack_final.c
	$(CC) $(CFLAGS) -c src/mtrack_final.c -o obj/mtrack_final.o 

lib/libstack.so : $(OBJECT)
	$(CC) -shared -o lib/libstack.so $(OBJECT)

bin/main : $(OBJECT) lib/libstack.so
	$(CC) $(CFLAGS) $(LFLAGS) -o bin/main $(OBJECT)

clean :
	rm -f $(OBJECT) bin/main lib/libstack.so

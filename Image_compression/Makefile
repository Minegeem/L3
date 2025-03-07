CC = gcc
CFLAGS = -Wall
LDFLAGS = -Llib -lprojet -lm  

SRC = src/Main.c
TARGET = bin/codec

all: $(TARGET)



$(TARGET): $(SRC)
	mkdir -p bin
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

clean:
	rm -f $(TARGET)
	rm -rf bin

CC = gcc
LIBS = `pkg-config --libs --cflags glib-2.0`
FLAGS= -w -Wall -Wextra -ansi -std=c11 -O2 -D_GNU_SOURCE
INCLUDES=include
SRC=src
OBJ=obj

program:
	mkdir obj
	make produtos
	make clientes
	make faturas
	make filiais
	make auxiliar
	make interface
	make prog
	make allprog
	rm obj/*.o
	rmdir obj
	./program
produtos: $(SRC)/catProdutos.c $(INCLUDES)/catProdutos.h
	$(CC) $(FLAGS) -o $(OBJ)/catProdutos.o $(LIBS) -c $(SRC)/catProdutos.c

clientes: $(SRC)/catClientes.c $(INCLUDES)/catClientes.h
	$(CC) $(FLAGS) -o $(OBJ)/catClientes.o $(LIBS) -c $(SRC)/catClientes.c

faturas: $(SRC)/catFaturas.c $(INCLUDES)/catFaturas.h
	$(CC) $(FLAGS) -o $(OBJ)/catFaturas.o $(LIBS) -c $(SRC)/catFaturas.c

filiais: $(SRC)/catFiliais.c $(INCLUDES)/catFiliais.h
	$(CC) $(FLAGS) -o $(OBJ)/catFiliais.o $(LIBS) -c $(SRC)/catFiliais.c

auxiliar: $(SRC)/auxFunc.c $(INCLUDES)/auxFunc.h
	$(CC) $(FLAGS) -o $(OBJ)/auxFunc.o $(LIBS) -c $(SRC)/auxFunc.c

interface: $(SRC)/interface.c $(INCLUDES)/interface.h
	$(CC) $(FLAGS) -o $(OBJ)/interface.o $(LIBS) -c $(SRC)/interface.c

prog: $(SRC)/main.c $(INCLUDES)/catClientes.h $(INCLUDES)/catProdutos.h $(INCLUDES)/catFaturas.h $(INCLUDES)/catFiliais.h $(INCLUDES)/auxFunc.h $(INCLUDES)/interface.h 
	$(CC) $(FLAGS) -o $(OBJ)/main.o $(LIBS) -c $(SRC)/main.c

allprog: $(OBJ)/main.o $(OBJ)/catProdutos.o $(OBJ)/catClientes.o
	$(CC) $(FLAGS) -o program $(OBJ)/main.o $(OBJ)/catProdutos.o $(OBJ)/catClientes.o $(OBJ)/catFaturas.o $(OBJ)/catFiliais.o  $(OBJ)/auxFunc.o $(OBJ)/interface.o $(LIBS)

memcheck:
	valgrind --leak-check=yes ./programa


clean:
	rm -rf obj
	rm program

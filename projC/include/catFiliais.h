#ifndef catFilial_h
#define catFilial_h
#include <glib.h>

#define Cliente gchar *
#define Produto gchar *

struct compra
{
	Produto prod;
	Cliente cli;
	double preco;
	char *tipo;
	gint quant;
	gint mes;
};

typedef struct compra *COMPRA;

struct filial
{
	GHashTable *cliente[3];
	GHashTable *produto[3];
};

typedef struct filial *Cat_Filial;
Cat_Filial inicializaCatFilial();
void insereCompraCliente(Cat_Filial filiais, COMPRA c, int id);
void insereCompraProduto(Cat_Filial filiais, COMPRA c, int id);
COMPRA makeCompra(char *venda);
char *getTipo(COMPRA c);
Cliente getCliente(COMPRA c);

#endif

#include "catClientes.h"
#include "catProdutos.h"
#ifndef catFaturas_h
#define catFaturas_h

#include <glib.h>

struct fat
{
	gint id;
	Produto prod;
	Cliente cli;
	int mes;
	char tipo;
	int quant;
	double preco;
	int filial;
};

typedef struct fat *Fatura;

struct faturas
{
	GTree *fat;
};

typedef struct faturas *Cat_Faturas;

Cat_Faturas inicializaCatFats();
Fatura criaFatura(char *venda, int id);
Cat_Faturas insereFatura(Cat_Faturas faturas, Fatura fat);
void destroyf(Cat_Faturas faturas);
void printFatura(Fatura f);
Fatura *getFatura(Cat_Faturas faturas, int id);
void destroyFatura(Fatura f);
double totalFaturado(Cat_Faturas faturas);
double getPreco(Fatura fatura);
static gboolean soma(gpointer key, gpointer value, gpointer data);
int quantidade(Cat_Faturas faturas);
static gboolean getQuantTotal(gpointer key, gpointer value, gpointer data);
int getMes(Fatura fatura);

#endif

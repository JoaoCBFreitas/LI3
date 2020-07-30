#ifndef auxiliar_h
#define auxiliar_h
#include "catClientes.h"
#include "catProdutos.h"
#include "catFiliais.h"
#include "catFaturas.h"
#include <glib.h>
struct sistemagestao
{
    char *fileprodutos;
    char *fileclientes;
    char *filevendas;
    Cat_Produtos produtos;
    Cat_Clientes clientes;
    Cat_Faturas faturas;
    Cat_Filial filiais;
};
typedef struct sistemagestao SGV;

struct answer
{
    SGV sis;
    int id;
    int id2;
    int soma;
    GArray *res;
    double total;
    char *str;
    double *doub;
};
typedef struct answer *ANSWER;
struct topprod
{
    Produto prod;
    int ncli;
    int nuni;
};
typedef struct topprod *TOPPROD;
void printMenu();
void leVendas(Cat_Clientes cli, Cat_Produtos prod, Cat_Faturas faturas, Cat_Filial filiais, char *file);
int validaVendas(char *linhaVenda, Cat_Clientes clientes, Cat_Produtos produtos);
ANSWER initAns(SGV sgv, int id, int id2);
int getLength(GArray *st);
void printArray(GArray *a);
int clientesSemCompras(SGV sistema);
int produtosSemCompras(SGV sistema);
void clientsBoughtP(GArray *compras, GArray *res);
void clientsBoughtN(GArray *compras, GArray *res);
static int existePEmFil(gpointer key, gpointer value, gpointer data);
static int existeCEmFil(gpointer key, gpointer value, gpointer data);
int getComprasMes(SGV sistema, int mes, int f, char *cliente);
GArray *addToArray(GArray *a, gpointer value);
GArray *mergeThree(GArray *a, GArray *b, GArray *c);
static int fillTotalArray(gpointer key, gpointer value, gpointer data);
void printN(GArray *a, int n);
int sortQuant(gconstpointer *a, gconstpointer *b);
void addQuantProd(GArray *c1, COMPRA c);

#endif

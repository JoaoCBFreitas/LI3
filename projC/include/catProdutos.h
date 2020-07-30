#ifndef catProdutos_h
#define catProdutos_h

#define Produto gchar *
#include <glib.h>

struct produtos
{
	GTree *prods[26];
};

typedef struct produtos *Cat_Produtos;

Cat_Produtos insere_CodProd(Cat_Produtos produtos, Produto codProd);
int validaProdutos(Produto produto);
int existeProduto(Produto produto, Cat_Produtos produtos);
Cat_Produtos leProdutos(Cat_Produtos produtos, char *file);
Cat_Produtos inicializaCatProds();
void destroyP(Cat_Produtos produtos);
void printProdChar(Cat_Produtos produtos, char letter);
gint getTotalProdutos(Cat_Produtos produtos);


#endif

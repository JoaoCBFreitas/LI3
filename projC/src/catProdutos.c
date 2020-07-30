#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "../include/catProdutos.h"

#define MAXBUFPROD 10
/** 
Inicializa catálogo de produtos
 */
Cat_Produtos inicializaCatProds()
{
	Cat_Produtos produtos = (Cat_Produtos)malloc(sizeof(struct produtos));
	for (int i = 0; i < 26; i++)
	{
		produtos->prods[i] = NULL;
		produtos->prods[i] = g_tree_new(strcmp);
	}
	return produtos;
}
/** 
Verifica se um Produto dado é válido
@param Produto produto
 */
int validaProdutos(Produto produto)
{
	if (produto[0] >= 'A' && produto[0] <= 'Z' && produto[1] >= 'A' && produto[1] <= 'Z')
	{
		int num = atoi(produto + 2);
		if (num >= 1000 && num <= 9999)
			return 1;
		else
			return 0;
	}
	else
	{
		return 0;
	}
}
/** 
Verifica se um Produto já existe no catálogo
@param Produto produto
@param Cat_Produtos produtos
 */
int existeProduto(Produto produto, Cat_Produtos produtos)
{
	gint key = produto[0] - 65;
	if (g_tree_lookup(produtos->prods[key], produto) == NULL)
	{
		return 0;
	}
	return 1;
}
/** 
Preenche o catalogo de produtos através de um ficheiro Input
@param Cat_Produto produtos
@param char* ficheiro
 */
Cat_Produtos leProdutos(Cat_Produtos produtos, char *file)
{
	FILE *fp;
	gchar str[10];
	char *prod;
	gint key;

	fp = fopen(file, "r");
	if (fp == NULL)
	{
		printf("I/O error");
		exit(1);
	}
	gint conta = 0;
	while (fgets(str, MAXBUFPROD, fp))
	{
		prod = strtok(str, "\n\r");
		if (validaProdutos(prod))
		{
			key = prod[0] - 65;
			g_tree_insert(produtos->prods[key], (gpointer *)g_strdup(prod), prod);
			conta++;
		}
	}

	printf("Numero de produtos válidos %d\n", conta);
	fclose(fp);
	return produtos;
}
/** 
Destroi o catalogo de produtos
@param Cat_Produto produtos
 */
void destroyP(Cat_Produtos produtos)
{
	for (int i = 0; i < 26; i++)
		g_tree_destroy(produtos->prods[i]);
}
/** 
Obtem o número total de produtos
@param Cat_Produto produtos
 */
gint getTotalProdutos(Cat_Produtos produtos)
{
	gint soma = 0;
	for (int i = 0; i < 26; i++)
	{
		soma += g_tree_nnodes(produtos->prods[i]);
	}
	return soma;
}
/** 
Imprime um produto e incrementa o total
@param gpointer chave
@param gpointer valor
@param gpointer data
 */
static gboolean imprimir(gpointer key, gpointer value, gpointer data)
{
	int *total = ((int *)data);
	(*(total))++;
	printf("%s\n", key);
	return FALSE;
}
/** 
Imprime e conta todos os produtos começados por uma certa letra 
@param Cat_Produto produtos
@param char letra
 */
void printProdChar(Cat_Produtos produtos, char letter)
{
	int key = letter - 65;
	int total = 0;
	g_tree_foreach(produtos->prods[key], imprimir, &total);
	printf("Total de produtos começados por %c: %d\n", letter, total);
}

#include "../include/catFaturas.h"
#include "../include/catFiliais.h"
#include "../include/interface.h"
#include <stdio.h>
#include <stdlib.h>
#define _GNU_SOURCE
#include <string.h>
#include <time.h>
/**
Inicializa o catalogo de filiais
 */
Cat_Filial inicializaCatFilial()
{
	Cat_Filial filial = (Cat_Filial)malloc(sizeof(struct filial));
	for (int i = 0; i < 3; i++)
	{
		filial->cliente[i] = g_hash_table_new(g_str_hash, g_str_equal);
		filial->produto[i] = g_hash_table_new(g_str_hash, g_str_equal);
	}
	return filial;
}
/**
Adiciona uma compra e o cliente que fez a compra ao catalogo de filiais
@param Cat_Filial filiais
@param COMPRA c
@param int id
 */
void insereCompraCliente(Cat_Filial filiais, COMPRA c, int id)
{
	GArray *compras;
	if ((compras = g_hash_table_lookup(filiais->cliente[id], c->cli)) == NULL)
	{
		GArray *array = g_array_new(FALSE, FALSE, sizeof(COMPRA));
		addToArray(array, c);
		g_hash_table_insert(filiais->cliente[id], c->cli, array);
	}
	else
	{
		addToArray(compras, c);
	}
}
/**
Adiciona uma compra e o produto pertencente á compra ao catalogo de filiais
@param Cat_Filial filiais
@param COMPRA c
@param int id
 */
void insereCompraProduto(Cat_Filial filiais, COMPRA c, int id)
{
	GArray *compras;
	if ((compras = g_hash_table_lookup(filiais->produto[id], c->prod)) == NULL)
	{
		GArray *array = g_array_new(FALSE, FALSE, sizeof(COMPRA));
		addToArray(array, c);
		g_hash_table_insert(filiais->produto[id], c->prod, array);
	}
	else
	{
		addToArray(compras, c);
	}
}
/**
Obtem o tipo de compra
@param COMPRA c
 */
char *getTipo(COMPRA c)
{
	return strdup(c->tipo);
}
/**
Através de uma linha de vendas cria uma COMPRA
@param char* venda
 */
COMPRA makeCompra(char *venda)
{
	COMPRA c = (COMPRA)malloc(sizeof(struct compra));
	char *campos[7];
	int index = 0;
	char *linhaAux = strdup(venda);
	char *token = strtok(linhaAux, " ");
	while (token != NULL)
	{
		campos[index] = strdup(token);
		token = strtok(NULL, " ");
		index++;
	}

	int mes = atoi(campos[5]);
	Cliente cliente = campos[4];
	char *tipo = campos[3];
	int quantidade = atoi(campos[2]);
	double preco = atof(campos[1]);
	Produto produto = campos[0];

	c->prod = produto;
	c->cli = cliente;
	c->mes = mes;
	c->tipo = tipo;
	c->quant = quantidade;
	c->preco = preco;
	return c;
}
/**
Destroi o catalogo das filiais
@param Cat_Filial filiais
 */
void destroyFil(Cat_Filial filiais)
{
	for (int i = 0; i < 3; i++)
	{
		g_hash_table_destroy(filiais->cliente[i]);
		g_hash_table_destroy(filiais->produto[i]);
	}
}

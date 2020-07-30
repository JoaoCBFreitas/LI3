#include "../include/catFaturas.h"
#include "../include/catFiliais.h"
#include <stdio.h>
#include <stdlib.h>
#define _GNU_SOURCE
#include <string.h>
#include <time.h>
/** 
Compara dois inteiros
@param int i
@param int j
 */
int myCompare(int i, int j)
{
	if (i > j)
		return 1;
	if (i == j)
		return 0;
	if (i < j)
		return -1;
}
/** 
Obtem mês em que fatura foi emitida
@param Fatura f
 */
int getMes(Fatura f)
{
	return f->mes;
}
/** 
Inicializa catalogo de faturas
 */
Cat_Faturas inicializaCatFats()
{
	Cat_Faturas faturas = (Cat_Faturas)malloc(sizeof(struct faturas));
	faturas->fat = g_tree_new_full(myCompare, NULL, NULL, destroyFatura);
	return faturas;
}
/** 
Cria uma fatura através de um String venda e um Identificador
@param char* venda
@param int id
 */
Fatura criaFatura(char *venda, int id)
{
	Fatura fat = (Fatura)malloc(sizeof(struct fat));
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

	int filial = atoi(campos[6]);
	int mes = atoi(campos[5]);
	Cliente cliente = campos[4];
	char tipo = campos[3][0];
	int quantidade = atoi(campos[2]);
	double preco = atof(campos[1]);
	Produto produto = campos[0];
	fat->id = id;
	fat->prod = produto;
	fat->cli = cliente;
	fat->mes = mes;
	fat->tipo = tipo;
	fat->quant = quantidade;
	fat->preco = preco;
	fat->filial = filial;
	return fat;
}
/** 
Adiciona uma fatura ao catalogo de faturas
@param Cat_Fatura faturas
@param Fatura fat
 */
Cat_Faturas insereFatura(Cat_Faturas faturas, Fatura fat)
{
	g_tree_insert(faturas->fat, (gpointer *)(fat->id), fat);
}
/** 
Destroi catalogo de faturas
@param Cat_Fatura faturas
 */
void destroyF(Cat_Faturas faturas)
{
	g_tree_destroy(faturas->fat);
}
/** 
Imprime os dados de uma fatura
@param Fatura fat
 */
void printFatura(Fatura fat)
{
	printf("ID: %d\n", fat->id);
	printf("Produto: %s\n", fat->prod);
	printf("Cliente: %s\n", fat->cli);
	printf("Mes: %d\n", fat->mes);
	printf("Tipo: %c\n", fat->tipo);
	printf("Quantidade: %d\n", fat->quant);
	printf("Preço: %.2f\n", fat->preco);
	printf("Filial: %d\n", fat->filial);
}
/** 
Obtem uma fatura através do seu id e do catalgo de faturas
@param Cat_Fatura faturas
@param gint id
 */
Fatura *getFatura(Cat_Faturas fats, gint id)
{
	Fatura *f = g_tree_lookup(fats, id);
	return f;
}
/** 
Destroi uma fatura
@param Fatura fat
 */
void destroyFatura(Fatura fat)
{
	free(fat->prod);
	free(fat->cli);
	free(fat);
}
/** 
Obtem preço numa fatura
@param Fatura fat
 */
double getPreco(Fatura f)
{
	return f->preco;
}
/** 
Incrementa o total faturado e imprime o preço individual por fatura
@param gpointer chave
@param gpointer valor
@param gpointer data
 */
static gboolean soma(gpointer key, gpointer value, gpointer data)
{
	double *total = ((double *)data);
	Fatura f = ((Fatura)value);
	double preco = getPreco(f);
	*(total) += preco;
	printf("Preço: %lf\n", f->preco);
	return FALSE;
}
/** 
Obtem o total faturado no catalogo de faturas
@param Cat_Faturas faturas
 */
double totalFaturado(Cat_Faturas faturas)
{
	double total = 0;
	g_tree_foreach(faturas->fat, soma, &total);
	return total;
}
/** 
Incrementa a quantidade total o e imprime a quantidade por fatura
@param gpointer chave
@param gpointer valor
@param gpointer data
 */
static gboolean getQuantTotal(gpointer key, gpointer value, gpointer data)
{
	int *total = ((int *)data);
	Fatura f = ((Fatura)value);
	*(total) += f->quant;
	printf("Quantidade: %d\n", f->quant);
	return FALSE;
}
/** 
Obtem a quantidade total no catalogo de faturas
@param Cat_Faturas faturas
 */
int quantidade(Cat_Faturas faturas)
{
	int quant = 0;
	g_tree_foreach(faturas->fat, getQuantTotal, &quant);
	return quant;
}
/** 
Obtem o cliente que fez uma compra
@param COMPRA c
 */
Cliente getCliente(COMPRA c)
{
	return c->cli;
}

#include "../include/catClientes.h"
#include <stdio.h>
#include <stdlib.h>
#define _GNU_SOURCE
#include <string.h>
#include <time.h>
/** 
Inicializa catálogo de clientes
 */
Cat_Clientes inicializaCatClis()
{
	Cat_Clientes clientes = (Cat_Clientes)malloc(sizeof(struct clientes));
	for (int i = 0; i < 26; i++)
	{
		clientes->clis[i] = NULL;
		clientes->clis[i] = g_tree_new(strcmp);
	}
	return clientes;
}
/** 
Verifica se o Cliente dado é válido
@param Cliente cliente
 */
int validaClientes(Cliente cliente)
{
	if (cliente[0] >= 'A' && cliente[0] <= 'Z')
	{
		int num = atoi(cliente + 1);
		if (num >= 1000 && num <= 5000)
			return 1;
		else
			return 0;
	}
	return 0;
}
/** 
Verifica se o cliente existe
@param Cliente cliente
@param AVL Catalogo Clientes
 */
int existeCliente(Cliente cliente, Cat_Clientes clientes)
{
	gint key = cliente[0] - 65;
	if (g_tree_lookup(clientes->clis[key], cliente) == NULL)
	{
		return 0;
	}
	return 1;
}
/** 
Através do ficheiro Input dos clientes preenche o catalogo de clientes
@param char* nome do ficheiro
@param AVL Catalogo Clientes
 */
Cat_Clientes leClientes(Cat_Clientes clientes, char *file)
{
	FILE *fp;
	gchar str[10];
	char *cliente;
	gint key;

	fp = fopen(file, "r");
	if (fp == NULL)
	{
		printf("I/O error");
		exit(1);
	}
	gint conta = 0;
	while (fgets(str, MAXBUFCLI, fp))
	{
		cliente = strtok(str, "\n\r");
		if (validaClientes(cliente))
		{
			key = cliente[0] - 65;
			g_tree_insert(clientes->clis[key], (gpointer *)g_strdup(cliente), cliente);
			conta++;
		}
	}
	printf("Numero de clientes válidos %d\n", conta);
	fclose(fp);
	return clientes;
}
/** 
Destroi o catálogo de clientes
@param AVL Catalogo Clientes
 */
void destroyC(Cat_Clientes clientes)
{
	for (int i = 0; i < 26; i++)
		g_tree_destroy(clientes->clis[i]);
}
/** 
Obtem o numero total de clientes
@param AVL Catalogo Clientes
 */
gint getTotalClientes(Cat_Clientes clientes)
{
	gint soma = 0;
	for (int i = 0; i < 26; i++)
	{
		soma += g_tree_nnodes(clientes->clis[i]);
	}
	return soma;
}

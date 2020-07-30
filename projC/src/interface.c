#include "../include/auxFunc.h"
#include "../include/interface.h"
#include "../include/catClientes.h"
#include "../include/catProdutos.h"
#include "../include/catFiliais.h"
#include "../include/catFaturas.h"
#include <stdio.h>
#include <stdlib.h>
#define _GNU_SOURCE
#include <string.h>
#include <time.h>
/**
Função que dado um GArray de compras soma a quantidade de vezes que se vendeu um produto
@param GArray *a
 */
int somaQuant(GArray *a)
{
	int total = 0;
	for (int i = 0; i < a->len; i++)
	{
		COMPRA c = g_array_index(a, COMPRA, i);
		total += c->quant;
	}
	return total;
}
/**
Função que dado um GArray de compras conta os clientes distintos que tem nesse GArray
@param GArray *a
 */
int quantClientes(GArray *a)
{
	int num = a->len;
	for (int i = 0; i < a->len; i++)
	{
		for (int j = i + 1; j < a->len; j++)
		{
			COMPRA c1 = g_array_index(a, COMPRA, i);
			COMPRA c2 = g_array_index(a, COMPRA, j);
			if (strcmp(c1->cli, c2->cli) == 0)
				num--;
		}
	}
	return num;
}
/**
GHFunc para interpretar os dados e preencher a estrutura TOPPROD
@param gpointer key
@param gpointer value
@param gpointer data
 */
static int interpreta(gpointer key, gpointer value, gpointer data)
{
	GArray *c = ((GArray *)value);
	Produto prod = ((Produto)key);
	GArray *tp = ((GArray *)data);
	TOPPROD top;
	top->prod = strdup(prod);
	int total = somaQuant(c);
	int clientes = quantClientes(c);
	top->ncli = clientes;
	top->nuni = total;
	g_array_append_val(tp, top);
	free(prod);
	return FALSE;
}

/**
 * Obtem um fatura num espaço contido entre dois meses
 @param gpointer key
 @param gpointer value
 @param gpointer data
 * */
static gboolean checkMes(gpointer key, gpointer value, gpointer data)
{
	ANSWER a = ((ANSWER *)data);
	Fatura f = ((Fatura)value);
	if (getMes(f) >= a->id && getMes(f) <= a->id2)
		a->soma++;
	a->total += getPreco(f);
	return FALSE;
}
/**
 * Verifica se o produto foi vendido numa filial
 @param gpointer key
 @param gpointer value
 @param gpointer data
 * */
static int checkProdFil(gpointer key, gpointer value, gpointer data)
{
	Produto p = ((Produto)key);
	char *prod = strdup(p);
	ANSWER a = ((ANSWER *)data);
	if (!g_hash_table_contains(a->sis.filiais->produto[a->id], prod))
	{
		a->soma++;
		addToArray(a->res, prod);
		printf("%s\n", prod);
	}
	free(prod);
	return FALSE;
}
/**
 * Verifica se um cliente está presente em todas as filiais
 @param gpointer key
 @param gpointer value
 @param gpointer data
 * */
static int comparaCliente(gpointer key, gpointer value, gpointer data)
{
	Cliente c = ((char *)key);
	char *cli = strdup(c);
	ANSWER a = ((ANSWER *)data);
	if (g_hash_table_contains(a->sis.filiais->cliente[1], cli) && g_hash_table_contains(a->sis.filiais->cliente[2], cli) && g_hash_table_contains(a->sis.filiais->cliente[0], cli))
	{
		addToArray(a->res, cli);
		a->soma++;
		return TRUE;
	}
	free(cli);
	return FALSE;
}
/**
 * Dependendo do tipo de fatura preenche o respetivo campo no numero total adquirido ou valor faturado.
 @param gpointer key
 @param gpointer valor
 @param gpointer data
 */
static int fillTotalArray(gpointer key, gpointer value, gpointer data)
{
	Fatura f = ((Fatura)value);
	int mes = getMes(f);
	ANSWER a = (ANSWER *)data;
	if (mes == a->id && (strcmp(f->prod, a->str) == 0))
	{
		printFatura(f);
		if (f->tipo == 'P')
		{
			printf("Compra em P\n");
			a->doub[f->filial - 1]++;
			a->doub[f->filial + 2] += f->preco;
		}
		if (f->tipo == 'N')
		{
			printf("Compra em N\n");
			a->doub[f->filial + 5]++;
			a->doub[f->filial + 8] += f->preco;
		}
	}
	return FALSE;
}

//--------------------------------------------------------------------
/**
Inicializa o sistema de gestão de vendas
 */
SGV initSGV()
{
	SGV *sistema = malloc(sizeof(struct sistemagestao));
	Cat_Clientes clientes;
	clientes = inicializaCatClis();
	Cat_Produtos produtos;
	produtos = inicializaCatProds();
	Cat_Faturas faturas;
	faturas = inicializaCatFats();
	Cat_Filial filiais;
	filiais = inicializaCatFilial();
	sistema->clientes = clientes;
	sistema->produtos = produtos;
	sistema->faturas = faturas;
	sistema->filiais = filiais;
	SGV res = *sistema;
	return res;
}
/**
Destroi o sistema de gestão de vendas
@param SGV sgv
 */
void destroySGV(SGV sgv)
{
	destroyP(sgv.produtos);
	destroyC(sgv.clientes);
	destroyF(sgv.faturas);
	destroyFil(sgv.filiais);
}
/**
Lê os ficheiros input e preenche os catalogos
@param SGV sgv
@param char* pathCliente
@param char* pathProduto
@param char* pathVendas
 */
SGV loadSGVFromFiles(SGV sgv, char *clientsFilePath, char *productsFilePath, char *salesFilePath)
{
	sgv.fileclientes = strdup(clientsFilePath);
	sgv.fileprodutos = strdup(productsFilePath);
	sgv.filevendas = strdup(salesFilePath);
	sgv.produtos = leProdutos(sgv.produtos, productsFilePath);
	sgv.clientes = leClientes(sgv.clientes, clientsFilePath);
	leVendas(sgv.clientes, sgv.produtos, sgv.faturas, sgv.filiais, salesFilePath);
	return sgv;
}
/**
Obtem o nome dos ficheiros usados como input
@param SGV sgv
 */
void getCurrentFileInfo(SGV sgv)
{
	printf("%s -> Linhas Validas: %d\n", sgv.fileclientes, getTotalClientes(sgv.clientes));
	printf("%s -> Linhas Validas: %d\n", sgv.fileprodutos, getTotalProdutos(sgv.produtos));
	printf("%s -> Linhas Validas: %d\n", sgv.filevendas, g_tree_nnodes(sgv.faturas->fat));
}
/**
Obtem os produtos começados por uma certa letra
@param SGV sgv
@param char letra
 */
void getProductsStartedByLetter(SGV sgv, char letter)
{
	int i = letter - 65;
	printProdChar(sgv.produtos, letter);
}
/**
Obtem o lucro por produto num certo mês, ou numa filial especifica ou no geral.
@param SGV sgv
@param char* produto
@param mês;
 */
void getProductSalesAndProfit(SGV sgv, char *productID, int month)
{
	int escolha = -1;
	while (escolha == -1)
	{
		printf("Indique a filial - 0 para geral\n");
		scanf("%d", &escolha);
		if (escolha < 0 || escolha > 3)
		{
			printf("Input inválido\n");
			escolha = -1;
		}
	}
	ANSWER a;
	a = initAns(sgv, month, 1);
	a->str = strdup(productID);
	double d[12];
	for (int i = 0; i < 12; i++)
	{
		d[i] = 0.0;
	}

	a->doub = d;
	g_tree_foreach(sgv.faturas->fat, fillTotalArray, (ANSWER *)a);
	if (escolha == 0)
	{
		printf("Total Faturado em cada Filial em P\n");
		printf("Filial 1: %f\n", a->doub[3]);
		printf("Filial 2: %f\n", a->doub[4]);
		printf("Filial 2: %f\n", a->doub[5]);
		printf("Total Faturado em cada Filial em N\n");
		printf("Filial 1: %f\n", a->doub[9]);
		printf("Filial 2: %f\n", a->doub[10]);
		printf("Filial 2: %f\n", a->doub[11]);
		printf("Total Vendido em cada Filial em P\n");
		printf("Filial 1: %f\n", a->doub[0]);
		printf("Filial 2: %f\n", a->doub[1]);
		printf("Filial 2: %f\n", a->doub[2]);
		printf("Total Vendido em cada Filial em N\n");
		printf("Filial 1: %f\n", a->doub[6]);
		printf("Filial 2: %f\n", a->doub[7]);
		printf("Filial 2: %f\n", a->doub[8]);
		printf("Total Vendido em N\n");
		printf("%f\n", a->doub[6] + a->doub[7] + a->doub[8]);
		printf("Total Vendido em P\n");
		printf("%f\n", a->doub[0] + a->doub[1] + a->doub[2]);
		printf("Total Faturado em N\n");
		printf("%f\n", a->doub[9] + a->doub[10] + a->doub[11]);
		printf("Total Faturado em P\n");
		printf("%f\n", a->doub[3] + a->doub[4] + a->doub[5]);
	}
	else
	{
		printf("Total Faturado na filial %d em P\n", escolha);
		printf("Filial %d: %f\n", escolha, a->doub[2 + escolha]);
		printf("Total Faturado na filial %d em N\n", escolha);
		printf("Filial %d: %f\n", escolha, a->doub[8 + escolha]);
		printf("Total Vendido na filial %d em P\n", escolha);
		printf("Filial %d: %f\n", escolha, a->doub[-1 + escolha]);
		printf("Total Vendido na filial %d em N\n", escolha);
		printf("Filial %d: %f\n", escolha, a->doub[5 + escolha]);
	}
}

/**
Obtem os produtos que nunca foram comprados numa certa filial
@param SGV sgv
@param int id
 */
void getProductsNeverBought(SGV sgv, int branchID)
{
	ANSWER a;
	ANSWER b;
	ANSWER c;
	GArray *res = g_array_new(FALSE, FALSE, sizeof(char *));
	if (branchID == 0)
	{
		a = initAns(sgv, 0, 1);
		b = initAns(sgv, 1, 1);
		c = initAns(sgv, 2, 1);
		for (int i = 0; i < 26; i++)
		{
			g_tree_foreach(sgv.produtos->prods[i], checkProdFil, (ANSWER *)a);
			g_tree_foreach(sgv.produtos->prods[i], checkProdFil, (ANSWER *)b);
			g_tree_foreach(sgv.produtos->prods[i], checkProdFil, (ANSWER *)c);
		}
		res = mergeThree(a->res, b->res, c->res);
		printArray(res);
		printf("Número total: %d\n", res->len);
	}
	else
	{
		a = initAns(sgv, branchID - 1, 1);
		a->soma = 0;
		for (int i = 0; i < 26; i++)
		{
			g_tree_foreach(sgv.produtos->prods[i], checkProdFil, (ANSWER *)a);
		}
		printf("Numero total = %d\n", a->soma);
	}
}
/**
Obtem os clientes que efetuaram compras em todas as filiais
@param SGV sgv
 */
void getClientsOfAllBranches(SGV sgv)
{
	ANSWER a;
	a = initAns(sgv, 1, 1);
	g_hash_table_foreach(sgv.filiais->cliente[0], comparaCliente, (ANSWER *)a);
	g_array_sort(a->res, sortQuant);
	printArray(a->res);
}

/**
 *Obtem os clientes que nunca compraram nenhum produto e produtos que nunca foram comprados por clientes
 @param SGV sgv
 */
void getClientsAndProductsNeverBoughtCount(SGV sgv)
{
	int c = clientesSemCompras(sgv);
	int p = produtosSemCompras(sgv);
	printf("Clientes sem compras: %d\nProdutos que nunca foram comprados: %d\n", c, p);
}

/**
Obtem os produtos comprados por um certo cliente
 @param SGV sgv
 @param char* id
 */
void getProductsBoughtByClient(SGV sgv, char *clientID)
{
	printf("+++++++++++++++++++++++++++++++++++++++++++++\n");
	printf("++M  --     F1    --     F2    --     F3   ++\n");
	printf("++1  --%10d --%10d --%10d++\n", getComprasMes(sgv, 1, 1, clientID), getComprasMes(sgv, 1, 2, clientID), getComprasMes(sgv, 1, 3, clientID));
	printf("++2  --%10d --%10d --%10d++\n", getComprasMes(sgv, 2, 1, clientID), getComprasMes(sgv, 2, 2, clientID), getComprasMes(sgv, 2, 3, clientID));
	printf("++3  --%10d --%10d --%10d++\n", getComprasMes(sgv, 3, 1, clientID), getComprasMes(sgv, 3, 2, clientID), getComprasMes(sgv, 3, 3, clientID));
	printf("++4  --%10d --%10d --%10d++\n", getComprasMes(sgv, 4, 1, clientID), getComprasMes(sgv, 4, 2, clientID), getComprasMes(sgv, 4, 3, clientID));
	printf("++5  --%10d --%10d --%10d++\n", getComprasMes(sgv, 5, 1, clientID), getComprasMes(sgv, 5, 2, clientID), getComprasMes(sgv, 5, 3, clientID));
	printf("++6  --%10d --%10d --%10d++\n", getComprasMes(sgv, 6, 1, clientID), getComprasMes(sgv, 6, 2, clientID), getComprasMes(sgv, 6, 3, clientID));
	printf("++7  --%10d --%10d --%10d++\n", getComprasMes(sgv, 7, 1, clientID), getComprasMes(sgv, 7, 2, clientID), getComprasMes(sgv, 7, 3, clientID));
	printf("++8  --%10d --%10d --%10d++\n", getComprasMes(sgv, 8, 1, clientID), getComprasMes(sgv, 8, 2, clientID), getComprasMes(sgv, 8, 3, clientID));
	printf("++9  --%10d --%10d --%10d++\n", getComprasMes(sgv, 9, 1, clientID), getComprasMes(sgv, 9, 2, clientID), getComprasMes(sgv, 9, 3, clientID));
	printf("++10 --%10d --%10d --%10d++\n", getComprasMes(sgv, 10, 1, clientID), getComprasMes(sgv, 10, 2, clientID), getComprasMes(sgv, 10, 3, clientID));
	printf("++11 --%10d --%10d --%10d++\n", getComprasMes(sgv, 11, 1, clientID), getComprasMes(sgv, 11, 2, clientID), getComprasMes(sgv, 11, 3, clientID));
	printf("++12 --%10d --%10d --%10d++\n", getComprasMes(sgv, 12, 1, clientID), getComprasMes(sgv, 12, 2, clientID), getComprasMes(sgv, 12, 3, clientID));
	printf("+++++++++++++++++++++++++++++++++++++++++++++\n");
}

/**
Obtem o número de vendas, em quantidade e lucro, num espaço contido entre dois meses
@param SGV sgv
@param int mes inicial
@param int mes final
 */
void getSalesAndProfit(SGV sgv, int minMonth, int maxMonth)
{
	ANSWER a = initAns(sgv, minMonth, maxMonth);
	g_tree_foreach(sgv.faturas->fat, checkMes, a);
	printf("Número de vendas: %d\nTotal de vendas: %lf\n", a->soma, a->total);
}

/**
Obtem os clientes que compraram um certo produto numa filial em P e em N
@param SGV sgv
@param char* id
@param int filial
 */
void getProductBuyers(SGV sgv, char *productID, int branch)
{
	GArray *res, *res2;
	res = g_array_new(FALSE, FALSE, sizeof(char *));
	res2 = g_array_new(FALSE, FALSE, sizeof(char *));
	printf("Clientes que compraram em P:\n");
	clientsBoughtP(g_hash_table_lookup(sgv.filiais->produto[branch], productID), res);
	printArray(res);
	res2 = g_array_new(FALSE, FALSE, sizeof(char *));
	printf("Clientes que compraram em N:\n");
	clientsBoughtN(g_hash_table_lookup(sgv.filiais->produto[branch], productID), res2);
	printArray(res2);
}

/**
Obtem os produtos mais comprados num mês por um certo cliente por quantidade em ordem decrescente
@param SGV sgv
@param char* clienteID
@param int mês
 */
int getClientFavoriteProducts(SGV sgv, char *clientID, int month)
{

	if (existeCliente(clientID, sgv.clientes) == 0 || validaClientes(clientID) == 0)
	{
		printf("Cliente inválido\n");
		return 0;
	}
	if (month <= 0 || month > 12)
	{
		printf("Mes inválido\n");
		return 0;
	}
	GArray *c1 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *c2 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *c3 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *res = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	c1 = g_hash_table_lookup(sgv.filiais->cliente[0], clientID);
	if (c1 == NULL)
	{
		c1 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	c2 = g_hash_table_lookup(sgv.filiais->cliente[1], clientID);
	if (c2 == NULL)
	{
		c2 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	c3 = g_hash_table_lookup(sgv.filiais->cliente[2], clientID);
	if (c3 == NULL)
	{
		c3 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	if (c1->len == 0 && c2->len == 0 && c3->len == 0)
	{
		printf("Este cliente não tem compras\n");
	}
	else
	{

		for (int i = 0; i < c1->len; i++)
		{
			COMPRA c = g_array_index(c1, COMPRA, i);
			if (c->mes == month)
				res = addToArray(res, c);
		}
		for (int i = 0; i < c2->len; i++)
		{
			COMPRA c = g_array_index(c2, COMPRA, i);
			if (c->mes == month)
				res = addToArray(res, c);
		}
		for (int i = 0; i < c3->len; i++)
		{
			COMPRA c = g_array_index(c3, COMPRA, i);
			if (c->mes == month)
				res = addToArray(res, c);
		}
		g_array_sort(res, sortQuant);
		printf("Produto->Quantidade\n");
		for (int i = 0; i < res->len; i++)
		{
			COMPRA c = g_array_index(res, COMPRA, i);
			printf("%s->%d\n", c->prod, c->quant);
		}
	}
	return 0;
}

/**
Obtem os produtos mais vendidos durante um ano indicando o num de clientes e unidade vendidas, filial a filial
@param SGV sgv
@param int limite
 */

void getTopSelledProducts(SGV sgv, int limit)
{
	GArray *c1 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *c2 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *c3 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	GArray *tp1 = g_array_new(FALSE, FALSE, sizeof(TOPPROD));
	GArray *tp2 = g_array_new(FALSE, FALSE, sizeof(TOPPROD));
	GArray *tp3 = g_array_new(FALSE, FALSE, sizeof(TOPPROD));

	//g_hash_table_foreach(sgv.filiais->produto[0], interpreta, tp1);
	//g_hash_table_foreach(sgv.filiais->produto[1], interpreta, tp2);
	//g_hash_table_foreach(sgv.filiais->produto[2], interpreta, tp3);
	printf("TODO :(\n");
}

/**
Obtem os produtos mais comprados por um cliente durante o ano
@param SGV sgv
@param char* clientID
@param int limite
 */
void getClientTopProfitProducts(SGV sgv, char *clientID, int limit)
{
	GArray *c1;
	GArray *c2;
	GArray *c3;
	COMPRA compra1, compra2;
	c1 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	c2 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	c3 = g_array_new(FALSE, FALSE, sizeof(COMPRA));

	c1 = g_hash_table_lookup(sgv.filiais->cliente[0], clientID);
	if (c1 == NULL)
	{
		c1 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	c2 = g_hash_table_lookup(sgv.filiais->cliente[1], clientID);
	if (c2 == NULL)
	{
		c2 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	c3 = g_hash_table_lookup(sgv.filiais->cliente[2], clientID);
	if (c3 == NULL)
	{
		c3 = g_array_new(FALSE, FALSE, sizeof(COMPRA));
	}
	if (c1->len == 0 && c2->len == 0 && c3->len == 0)
	{
		printf("Este cliente não tem compras\n");
	}
	else
	{
		for (int i = 0; i < c1->len; i++)
		{
			for (int j = 1; j < c1->len; j++)
			{
				compra1 = g_array_index(c1, COMPRA, i);
				compra2 = g_array_index(c1, COMPRA, j);
				if (strcmp(compra1->prod, compra2->prod) == 0)
				{
					compra1->quant += compra2->quant;
					g_array_remove_index(c1, j);
				}
			}
		}
		for (int i = 0; i < c2->len; i++)
		{
			compra1 = g_array_index(c2, COMPRA, i);
			addQuantProd(c1, compra1);
		}
		for (int i = 0; i < c3->len; i++)
		{
			compra1 = g_array_index(c3, COMPRA, i);
			addQuantProd(c1, compra1);
		}
		g_array_sort(c1, sortQuant);
		printN(c1, limit);
	}
}

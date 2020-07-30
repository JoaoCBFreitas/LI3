#include "../include/catClientes.h"
#include "../include/catProdutos.h"
#include "../include/catFiliais.h"
#include "../include/catFaturas.h"
#include "../include/auxFunc.h"
#include <stdio.h>
#include <stdlib.h>
#define _GNU_SOURCE
#include <string.h>
#include <time.h>
/**
 * Inicializa a struct answer
 @param SGV sgv
 @param int id1
 @param int id2
 * */
ANSWER initAns(SGV sgv, int id, int id2)
{
    ANSWER a = (ANSWER)malloc(sizeof(struct answer));
    a->sis = sgv;
    a->id = id;
    a->res = g_array_new(FALSE, FALSE, sizeof(char *));
    a->soma = 0;
    a->total = 0;
    a->id2 = id2;
    a->str = (char *)malloc(sizeof(char *));
    a->doub = (double *)malloc(12 * sizeof(double *));
    return a;
}
/**
 * Devolve produtos para os quais nao foram efetuadas compras
 @param SGV sgv
 * */
int produtosSemCompras(SGV sistema)
{
    ANSWER a;
    a = initAns(sistema, 1, 1);
    for (int i = 0; i < 26; i++)
    {
        g_tree_foreach(sistema.produtos->prods[i], existePEmFil, a);
    }
    return a->soma;
}
/**
 * Devolve os clientes que nao efetuaram compras
 @param SGV sgv
 * */
int clientesSemCompras(SGV sistema)
{
    ANSWER a;
    a = initAns(sistema, 1, 1);
    for (int i = 0; i < 26; i++)
    {
        g_tree_foreach(sistema.clientes->clis[i], existeCEmFil, a);
    }
    return a->soma;
}
/**
 * Devolve compras feitas em P
 @param GArray* compras
 @param GArray* res
 * */
void clientsBoughtP(GArray *compras, GArray *res)
{
    COMPRA c;
    if (compras != NULL)
    {
        for (int i = 0; i < compras->len; i++)
        {
            c = g_array_index(compras, COMPRA, i);
            if (strcmp(getTipo(c), "P") == 0)
            {
                addToArray(res, getCliente(c));
            }
        }
    }
}
/**
 * Devolve compras feitas em N
 @param GArray* compras
 @param GArray* res
 * */
void clientsBoughtN(GArray *compras, GArray *res)
{
    COMPRA c;
    if (compras != NULL)
    {
        for (int i = 0; i < compras->len; i++)
        {
            c = g_array_index(compras, COMPRA, i);
            if (strcmp(getTipo(c), "N") == 0)
            {
                addToArray(res, getCliente(c));
            }
        }
    }
}
/**
 * Imprime um array
 @param GArray* compras
 * */
void printArray(GArray *a)
{
    for (int i = 0; i < a->len; i++)
    {
        printf("%s\n", g_array_index(a, char *, i));
    }
}
/**
 * Verifica se um cliente fez compras em alguma filial
 @param gpointer key
 @param gpointer value
 @param gpointer data
 * */
static int existeCEmFil(gpointer key, gpointer value, gpointer data)
{
    Cliente c = ((Cliente)key);
    char *cli = strdup(c);
    ANSWER a = ((ANSWER *)data);
    if (!g_hash_table_contains(a->sis.filiais->cliente[0], cli) &&
        !g_hash_table_contains(a->sis.filiais->cliente[1], cli) &&
        !g_hash_table_contains(a->sis.filiais->cliente[2], cli))
    {
        a->soma++;
    }
    free(cli);
    return FALSE;
}
/**
 * Verifica se um produto foi vendido em alguma filial
 @param gpointer key
 @param gpointer value
 @param gpointer data
 * */
static int existePEmFil(gpointer key, gpointer value, gpointer data)
{
    Cliente c = ((Cliente)key);
    char *cli = strdup(c);
    ANSWER a = ((ANSWER *)data);
    if (!g_hash_table_contains(a->sis.filiais->produto[0], cli) &&
        !g_hash_table_contains(a->sis.filiais->produto[1], cli) &&
        !g_hash_table_contains(a->sis.filiais->produto[2], cli))
    {
        a->soma++;
    }
    free(cli);
    return FALSE;
}
/**
 * Adiciona um valor a um GArray
 @param GArray *a
 @param gpointer value
 * */
GArray *addToArray(GArray *a, gpointer value)
{
    g_array_append_val(a, value);
    return a;
}
/**
 * Junta 3 GArrays
 @param GArray *a
 @param GArray *b
 @param GArray *c
 * */
GArray *mergeThree(GArray *a, GArray *b, GArray *c)
{
    int j;
    int soma = 0;
    char *prod;
    GArray *res = g_array_new(FALSE, FALSE, sizeof(char *));
    for (int i = 0; i < a->len; i++)
    {
        prod = g_array_index(a, char *, i);
        if (g_array_binary_search(b, prod, strcmp, &j))
        {
            if (g_array_binary_search(c, prod, strcmp, &j))
            {
                g_array_append_val(res, prod);
            }
        }
    }
    return res;
}
/**
Imprime o menu
*/
void printMenu()
{
    printf("\nEscolha a Querie\n");
    printf("1:Determinar a lista e o nº total de produtos cujo código se inicia por uma dada letra (maiúscula)\n");
    printf("2:Dado um mês e um código de produto, ambos válidos, determinar e apresentar o número total de vendas e o total facturado com esse produto em tal mês\n");
    printf("3:Determinar a lista ordenada dos códigos dos produtos que ninguém  comprou\n");
    printf("4:Determinar a lista ordenada de códigos de clientes que realizaram compras em todas as filiais\n");
    printf("5:Determinar o número de clientes registados que não realizaram compras bem como o número de produtos que ninguém comprou.\n");
    printf("6:Dado um código de cliente, criar uma tabela com o número total de produtos comprados, mês a mês.\n");
    printf("7:Dado um intervalo fechado de meses determinar o total de vendas registadas nesse intervalo e o total faturado\n");
    printf("8:Dado um código de produto e uma filial, determinar os códigos dos clientes que o compraram, distinguindo entre compra N e compra P\n");
    printf("9:Dado um código de cliente e um  mês, determinar a lista de códigos de produtos que mais comprou por quantidade, por ordem descendente\n");
    printf("10:Criar uma lista dos N produtos mais vendidos em  todo o ano, indicando o número total de clientes e o número de unidades vendidas, filial a filial\n");
    printf("11:Dado um código de cliente determinar quais os códigos dos N produtos em que mais gastou dinheiro durante o ano\n");
    printf("12:Estatísticas de leitura\n");
    printf("13:Sair\n\n");
}
/**
Obtem o número de compras efetuados por um cliente num certo mês por filial
@param Cat_Filial filial
@param int mes
@param int id
@param char *cliente
*/
int getComprasMes(SGV sistema, int mes, int f, char *cliente)
{
    int res = 0;
    if (validaClientes(cliente) == 0)
        return 0;
    if (existeCliente(cliente, sistema.clientes) == 0)
        return 0;
    GArray *c1 = g_hash_table_lookup(sistema.filiais->cliente[f - 1], cliente);
    for (int i = 0; i < c1->len; i++)
    {
        COMPRA c = g_array_index(c1, COMPRA, i);
        if (c->mes == mes)
            res += c->quant;
    }
    return res;
}

/**
Verifica se uma linha de venda é válida
@param char* linha de venda
@param Cat_Clientes clientes
@param Cat_Produtos produtos
 */
int validaVendas(char *linhaVenda, Cat_Clientes clientes, Cat_Produtos produtos)
{
    char *campos[CAMPOSVENDA];
    int index = 0;
    char *linhaAux = strdup(linhaVenda);
    char *token = strtok(linhaAux, " ");
    while (token != NULL)
    {
        campos[index] = strdup(token);
        token = strtok(NULL, " ");
        index++;
    }
    if (validaClientes(campos[4]) && existeCliente(campos[4], clientes))
    {
        if (validaProdutos(campos[0]) && existeProduto(campos[0], produtos))
            return 1;
    }
    else
    {
        return 0;
    }
    return 0;
}
/**
Através de o ficheiro de input referente ás Vendas, o catalogo de clientes e catalogo de produtos preenche o catalogo de faturas e filiais
@param Cat_Clientes clientes
@param Cat_Produtos produtos
@param Cat_Faturas faturas
@param Cat_Filiais filiais
@param char* linha de venda
 */
void leVendas(Cat_Clientes cli, Cat_Produtos prod, Cat_Faturas faturas, Cat_Filial filiais, char *file)
{
    FILE *fp;
    char str[100];
    char *vendas;
    int total = 0;
    int a = 0;
    Fatura f;
    COMPRA c;
    int fil;
    fp = fopen(file, "r");
    if (fp == NULL)
    {
        printf("I/O error");
        exit(1);
    }
    while (fgets(str, MAXBUFVEN, fp))
    {
        vendas = strtok(str, "\n\r");
        a++;
        if (validaVendas(vendas, cli, prod))
        {
            f = criaFatura(vendas, total);
            c = makeCompra(vendas);
            fil = f->filial - 1;
            insereFatura(faturas, f);
            insereCompraCliente(filiais, c, fil);
            insereCompraProduto(filiais, c, fil);
            total++;
        }
    }
    printf("Total de vendas %d\n", a);
    printf("Vendas válidas %d\n", total);
    printf("Vendas inválidas %d\n", a - total);
    fclose(fp);
}
/**
 * Se c existir dentro de c1 incrementa a quantidade senao adiciona c
 @param GArray *c1
 @param COMPRA c
 * */
void addQuantProd(GArray *c1, COMPRA c)
{
    int flag = 0;
    COMPRA compra1;

    if (c1->len == 0)
    {
        addToArray(c1, c);
        flag = 1;
    }

    for (int i = 0; i < c1->len && flag == 0; i++)
    {
        compra1 = g_array_index(c1, COMPRA, i);
        if (strcmp(compra1->prod, c->prod) == 0)
        {
            compra1->quant += c->quant;
            flag = 1;
        }
    }
    if (flag == 0)
        addToArray(c1, c);
}
/**
 * Faz sort a um array de acordo com as quantidades
 * */
int sortQuant(gconstpointer *c1, gconstpointer *c2)
{
    COMPRA a = (COMPRA *)malloc(sizeof(COMPRA));
    a = (COMPRA)c1;
    COMPRA b = (COMPRA *)malloc(sizeof(COMPRA));
    b = (COMPRA)c2;
    return (a->quant - b->quant);
}
/**
 * Imprime n Compras do array a
 @param GArray *a
 @param int n
 * */
void printN(GArray *a, int n)
{
    COMPRA c;
    if (a->len < n)
    {
        n = a->len;
    }
    for (int i = 0; i < n; i++)
    {
        c = g_array_index(a, COMPRA, i);
        printf("%s\n", c->prod);
    }
}

/**
Devolve o tamanho de um GArray
@param GArray array
 */
int getLength(GArray *st)
{
    return st->len;
}

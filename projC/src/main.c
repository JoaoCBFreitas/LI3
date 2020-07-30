#include "../include/interface.h"
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>

#define _GNU_SOURCE

#define CAMPOSVENDA 7
#define MAXBUFVEN 100
#define MAXVENDAS 1000000

int main(int argc, char *argv[])
{
	gint nFats;
	//Mediçao Performance
	clock_t start, end;
	double cpu_time_used;
	//Inicialização e leitura
	SGV sistema = initSGV();
	start = clock();
	char clientes[13] = "Clientes.txt";
	char produtos[13] = "Produtos.txt";
	char vendas[14] = "Vendas_1M.txt";
	sistema = loadSGVFromFiles(sistema, clientes, produtos, vendas);
	//---------------------------------------------------
	end = clock();
	cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
	printf("Tempo de Leitura: %f\n", cpu_time_used);
	printf("---------------------------------------------\n");

	int escolha = 0;
	while (escolha >= 0 && escolha < 13)
	{
		printMenu();
		scanf("%d", &escolha);
		switch (escolha)
		{
		case 1:
			printf("-----------------------------------------\n");
			char letter;
			printf("Escolha a letra:");
			scanf(" %c", &letter);
			start = clock();
			getProductsStartedByLetter(sistema, letter);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 1: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 2:
			printf("-----------------------------------------\n");
			char productID[7];
			int month;
			printf("Inserir código de produto:\n");
			scanf("%s", &productID);
			printf("Inserir mes:\n");
			scanf(" %d", &month);
			start = clock();
			getProductSalesAndProfit(sistema, productID, month);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 2: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 3:
			printf("-----------------------------------------\n");
			int op = 0;
			printf("Selecione a filial que pretende ou 0 para geral.\n");
			scanf(" %d", &op);
			start = clock();
			getProductsNeverBought(sistema, op);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 3: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 4:
			printf("-----------------------------------------\n");
			start = clock();
			getClientsOfAllBranches(sistema);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 4: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 5:
			printf("-----------------------------------------\n");
			start = clock();
			getClientsAndProductsNeverBoughtCount(sistema);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 5: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 6:
			printf("-----------------------------------------\n");
			char cliente[5];
			printf("Introduza o código de cliente:\n");
			scanf(" %s", &cliente);
			start = clock();
			getProductsBoughtByClient(sistema, cliente);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 6: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 7:
			printf("-----------------------------------------\n");
			int mes1, mes2;
			printf("Introduzir primeiro mes:\n");
			scanf(" %d", &mes1);
			printf("Introduzir segundo mes:\n");
			scanf(" %d", &mes2);
			start = clock();
			getSalesAndProfit(sistema, mes1, mes2);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 7: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 8:
			printf("-----------------------------------------\n");
			char prod[6];
			int id;
			printf("Inserir código de produto:\n");
			scanf("%s", &prod);
			printf("Inserir id da Filial:\n");
			scanf(" %d", &id);
			id--;
			start = clock();
			getProductBuyers(sistema, prod, id);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 8: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 9:
			printf("-----------------------------------------\n");
			char cliente9[5];
			int mes9;
			printf("Introduza o cliente\n");
			scanf("%s", &cliente9);
			printf("Introduza o mês\n");
			scanf("%d", &mes9);
			start = clock();
			getClientFavoriteProducts(sistema, cliente9, mes9);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 9: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 10:
			printf("-----------------------------------------\n");
			int limit10;
			printf("Inserir limite:\n");
			scanf(" %d", &limit10);
			start = clock();
			getTopSelledProducts(sistema, limit10);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 10: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 11:
			printf("-----------------------------------------\n");
			char clientID[5];
			int limit;
			printf("Inserir código de cliente:\n");
			scanf("%s", &clientID);
			printf("Inserir limite:\n");
			scanf(" %d", &limit);
			start = clock();
			getClientTopProfitProducts(sistema, clientID, limit);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 11: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 12:
			printf("-----------------------------------------\n");
			start = clock();
			getCurrentFileInfo(sistema);
			end = clock();
			cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
			printf("Execução Query 12: %f\n", cpu_time_used);
			printf("-----------------------------------------\n");
			break;
		case 13:
			printf("\n\nSaiu\n");
			break;
		default:
			printf("-----------------------------------------\n");
			printf("Comando desconhecido\n");
			escolha = 0;
			printf("-----------------------------------------\n");
			break;
		}
	}
	start = clock();
	destroySGV(sistema);
	end = clock();
	cpu_time_used = ((double)(end - start)) / CLOCKS_PER_SEC;
	printf("Sistema de Gestão de vendas eliminado\n");
	printf("Tempo para eliminar: %f\n", cpu_time_used);
	return 0;
}

#ifndef queries_h
#define queries_h
#include "auxFunc.h"
#include "catClientes.h"
#include "catProdutos.h"
#include "catFiliais.h"
#include "catFaturas.h"
#include <glib.h>
static int comparaCliente(gpointer key, gpointer value, gpointer data);
static gboolean checkMes(gpointer key, gpointer value, gpointer data);
static int checkProdFil(gpointer key, gpointer value, gpointer data);
//--------------------------------
SGV initSGV();

void destroySGV(SGV sgv);

SGV loadSGVFromFiles(SGV sgv, char *clientsFilePath, char *productsFilePath, char *salesFilePath);

void getCurrentFileInfo(SGV sgv);

void getProductsStartedByLetter(SGV sgv, char letter);

void getProductSalesAndProfit(SGV sgv, char *productID, int month);

void getProductsNeverBought(SGV sgv, int branchID);

void getClientsOfAllBranches(SGV sgv);

void getClientsAndProductsNeverBoughtCount(SGV sgv);

void getProductsBoughtByClient(SGV sgv, char *clientID);

void getSalesAndProfit(SGV sgv, int minMonth, int maxMonth);

void getProductBuyers(SGV sgv, char *productID, int branch);

int getClientFavoriteProducts(SGV sgv, char *clientID, int month);

void getTopSelledProducts(SGV sgv, int limit);

void getClientTopProfitProducts(SGV sgv, char *clientID, int limit);

#endif

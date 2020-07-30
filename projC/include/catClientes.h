#ifndef catClientes_h
#define catClientes_h

#define Cliente gchar *

#define CAMPOSVENDA 7
#define MAXBUFCLI 10
#define MAXBUFVEN 100
#define MAXCLIENTES 20000
#define MAXVENDAS 1000000

#include <glib.h>
struct clientes
{
	GTree *clis[26];
};

typedef struct clientes *Cat_Clientes;

Cat_Clientes leClientes(Cat_Clientes clientes, char *file);
Cat_Clientes insere_CodCli(Cat_Clientes clientes, char *codCli);
int validaClientes(Cliente client);
int existeCliente(Cliente cliente, Cat_Clientes clientes);
Cat_Clientes inicializaCatClis();
void destroyC(Cat_Clientes clientes);
gint getTotalClientes(Cat_Clientes clientes);
static int fillArrayClientes(gpointer key, gpointer value, gpointer data, Cat_Clientes clientes);

#endif

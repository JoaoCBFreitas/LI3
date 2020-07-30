/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo24.projJava;

public class GestVendas {
    /**
    * Inicializa a classe principal
    * @param args Argumentos da funçao main
    */
    public static void main(String[] args){
        Interface qe=new Queries();
        new GestVendas(qe);
    }
    /**
    * Inicializa o processo de leitura de ficheiros e menu
    * @param qe Interface do programa
    */
    public GestVendas(Interface qe){
        SGV sgv=qe.carregaFicheiros("Clientes.txt","Produtos.txt","Vendas_1M.txt");
        qe.printMenu(sgv);
    }
    
   
}

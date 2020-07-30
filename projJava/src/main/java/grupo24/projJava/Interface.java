/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo24.projJava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author joaob
 */
public interface Interface {
        
    SGV carregaFicheiros(String clientes,String produtos,String vendas);
    void printMenu(SGV sgv);
    ArrayList<Cliente> readClientes(String fich);
    ArrayList<Produto> readProdutos(String fich);
    ArrayList<Venda> readVendas(String fich);
    
    SGV carrega();
    void guarda(SGV sgv);
    HashMap<Integer,Integer> comprasMes(SGV sgv);
    HashMap<Integer,Double> faturacaoMes(SGV sgv);
    HashMap<Integer,Integer> distClientes(int i,SGV sgv);
    ArrayList<Produto> prodNC(SGV sgv);
    HashMap<Integer,ArrayList<Cliente>> resultadoMensal(SGV sgv,int i);
    HashMap<Integer,ArrayList<Double>> comprasCliente(SGV sgv,String cliente);
    HashMap<Integer,ArrayList<Double>> comprasProduto(SGV sgv,String produto);
    Map<Produto,Integer> maisComprou(SGV sgv,String cliente);
    int[] somaQuantidadeProduto(SGV sgv, Produto p);
    
    Map<Produto, Integer> getQuantidadeProdutos(SGV sgv, HashMap<Produto, Integer> hashC);
    ArrayList<Cliente> melhoresClientes(SGV sgv, int fil);
    List<Cliente> clientesMaisCompraram(SGV sgv, Produto prod, HashMap<Cliente, Double> hashP);
    List<Pair<Cliente, Integer>> clientesMaisDiversos(SGV sgv);
    
    int produtosDiferentes(SGV sgv, Cliente c);
    ArrayList<Double> getFatTotalMes(SGV sgv);
    
    
}

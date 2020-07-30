package grupo24.projJava;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Filial implements Serializable{
    private int id;
    private HashMap<Produto, List<Venda>> produtosVendas;
    private HashMap<Cliente, List<Venda>> clientesVendas;
    
    /**
     * Construtor de uma Filial vazia
     * @param i Identificador da filial 
     */
    public Filial(int i) {
        this.id = i-1;
        this.clientesVendas = new HashMap<>();
        this.produtosVendas = new HashMap<>();
    }
    /**
     * Devolve id de uma filial
     * @return O id de uma filial
     */
    public int getId(){
        return this.id;
    }
    /**
     * Insere id numa filial
     * @param id Inteiro identificador de uma filial
     */
    public void setID(int id){
        this.id = id;
    }
    
    /**
     * Construtor de um Catalogo de filiais através de um outro catálogo de
     * filiais
     * 
     * @param f Catalogo de filiais
     */
    public Filial(Filial f) {
        this.id = f.id;
        this.produtosVendas = f.getProdutosVendas();
        this.clientesVendas = f.getClientesVendas();
    }
    /**
    * Devolve Produtos Vendas
    * @return HashMap com todas as vendas por produtos de uma filial
    */
    public HashMap getProdutosVendas(){
        return this.produtosVendas;
    }
    /**
     * Devolve Clientes Vendas
     * @return HashMap com todas as vendas por Clientes de uma filial
     */
    public HashMap getClientesVendas(){
        return this.clientesVendas;
    }
    /**
     * Adiciona uma venda
     * @param v Venda para ser adicionada ao uma filial
     */
    public void addVenda(Venda v){
        if(this.getProdutosVendas()==null){//verifica se há um hashmap
            HashMap<Produto,List<Venda>> prodVen=new HashMap<>();
            List<Venda> vendas=new ArrayList<>();
            vendas.add(v);
            prodVen.put(v.getProduto(),vendas);
            this.produtosVendas=prodVen;
        }else{
            HashMap<Produto,List<Venda>> prodVen=this.getProdutosVendas();
            List<Venda> vendas;
            if(prodVen.get(v.getProduto())==null){
                vendas=new ArrayList<>();
            }else{
                vendas=prodVen.get(v.getProduto()); 
            }
            vendas.add(v);  
            prodVen.put(v.getProduto(), vendas);
            this.produtosVendas=prodVen;
        }
        if(this.getClientesVendas()==null){
            HashMap<Cliente,List<Venda>> clienteVen=new HashMap<>();
            List<Venda> vendas=new ArrayList<>();
            vendas.add(v);
            clienteVen.put(v.getCliente(),vendas);
            this.clientesVendas=clienteVen;
        }else{
            HashMap<Cliente,List<Venda>> clienteVen=this.getClientesVendas();
            List<Venda> vendas;
            if(clienteVen.get(v.getCliente())==null){
                vendas=new ArrayList<>();
            }else{
                vendas=clienteVen.get(v.getCliente());
            }
            vendas.add(v);
            clienteVen.put(v.getCliente(), vendas);
            this.clientesVendas=clienteVen;
        }
    }
    /**
     * Devolve lista de vendas de um certo produto
     * @param p Produto para ver todas as vendas relacionadas com ele numa filial
     * @return Lista de vendas relacionadas com o produto indicado
     */
    public List<Venda> getListVendasP(Produto p){
        if(this.produtosVendas.containsKey(p))
            return this.produtosVendas.get(p);
        else{
            List<Venda> v = new ArrayList<Venda>();
            return v;
        }
    } 
    /**
     * Devolve lista de vendas de um certo cliente
     * @param c Cliente para ver todas as vendas relacionadas com ele numa filial
     * @return Lista de vendas relacionadas com o cliente indicado
     */
    public List<Venda> getListVendasC(Cliente c){
        if(this.clientesVendas.containsKey(c))
            return this.clientesVendas.get(c);
        else{
            List<Venda> v = new ArrayList<Venda>();
            return v;
        }
    } 
}
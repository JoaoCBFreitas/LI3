package grupo24.projJava;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;
import java.util.TreeSet;


public class Faturacao implements Serializable{
    
    private TreeSet<Fatura> faturas;
    /**
     * Construtor vazio da faturacao
     */
    public Faturacao() {
        this.faturas = new TreeSet<Fatura>(new FaturaComparador());
    }
    /**
     * Construtor parameterizado da faturação
     * @param fats Lista de Faturas 
     */
    public Faturacao(List<Fatura> fats) {
        this.faturas = new TreeSet<Fatura>(new FaturaComparador());
        this.faturas.addAll(fats);
    }
    /**
     * Devolve todas as faturas
     * @return TreeSet com todas as faturas
     */
    public TreeSet<Fatura> getFaturas(){
        return this.faturas;
    }
    /**
     * Devolve as faturas emitidas num mês
     * @param i Mes para ver a faturação
     * @return Lista com as faturas do mes dado
     */
    public ArrayList<Fatura> faturaMensal(int i){
        ArrayList<Fatura> res = new ArrayList<>(); 
        for(Fatura f:faturas){
            if(f.getMes()==i){
                res.add(f);
            }
        }
        return res;
    }
    /**
     * Devolve as faturas relacionadas com um cliente
     * @param c Cliente para ver a faturação do próprio
     * @return Lista com as faturas relacionadas com o cliente
     */
    public ArrayList<Fatura> faturaCliente(String c){
        ArrayList<Fatura> res = new ArrayList<>(); 
        Cliente cliente=new Cliente(c);
        for(Fatura f:faturas){
            if(f.getCliente().equals(cliente)){
                res.add(f);
            }
        }
        return res;
    }
    /**
     * Devolve as faturas relacionadas com um produto
     * @param p Produto para procurar na faturação
     * @return Lista com todas as faturas que tem o produto presente
     */
    public ArrayList<Fatura> faturaProduto(String p){
        ArrayList<Fatura> res=new ArrayList<>();
        Produto prod=new Produto(p);
        for(Fatura f:faturas){
            if(f.getProduto().equals(prod)){
                res.add(f);
            }
        }
        return res;
    }
}


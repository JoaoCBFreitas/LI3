/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo24.projJava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;



public class SGV implements Serializable{
    private CatProdutos  catProdutos;
    private CatClientes catClientes;
    private CatFilial filiais;
    private Faturacao faturacao;
    /**
     * Construtor vazio SGV
     */
    public SGV(){
        this.catClientes=new CatClientes();
        this.catProdutos=new CatProdutos();
    }
    /**
     * Construtor parameterizado SGV
     * @param catClientes Catalogo de Clientes
     * @param catProdutos Catalogo de Produtos
     * @param faturas Catalogo das faturas
     * @param filial Catalogo das filiais
     */
    public SGV(CatClientes catClientes,CatProdutos catProdutos,Faturacao faturas,CatFilial filial){
        this.catClientes=catClientes;
        this.catProdutos=catProdutos;
        this.faturacao=faturas;
        this.filiais=filial;
    }
    /**
     * Construtor cópia SGV
     * @param sgv Sistema de Gestão de Vendas
     */
    public SGV(SGV sgv) {
        this.catClientes = sgv.getCatClientes();
        this.catProdutos = sgv.getCatProdutos();
        this.faturacao = sgv.getFaturacao();
        this.filiais = sgv.getFiliais();
    }
    /**
     * Guarda o estado atual do programa num ficheiro
     * @throws IOException Excepção de Input/Output
     */
    public void saveState () throws IOException{
        FileOutputStream file = new FileOutputStream("gestVendas.dat");
        ObjectOutputStream oss = new ObjectOutputStream(file);
        oss.writeObject(this);
        oss.flush();
        oss.close();
        file.close();
    }
    /**
     * Carrega o estado do programa através de um ficheiro
     * @return Sistema de Gestão de Vendas
     * @throws FileNotFoundException Excepçao caso nao encontre o ficheiro
     * @throws IOException Excepção de Input/Output
     * @throws ClassNotFoundException Excepçao de Classe nao encontrada
     */
    public static SGV loadState() throws FileNotFoundException, IOException, ClassNotFoundException{
        SGV sgv=new SGV();
        try{
            FileInputStream file = new FileInputStream("gestVendas.dat");
            ObjectInputStream ois = new ObjectInputStream(file);
            sgv = (SGV) ois.readObject();
            ois.close();
            file.close();
        }catch(IOException | ClassNotFoundException e){
            return null;
        }
        return sgv;
    }
    /**
     * Devolve catalogoCLientes
     * @return Catalogo de Clientes
     */
    public CatClientes getCatClientes() {
        return this.catClientes;
    }
    /**
     * Devolve catálogoProdutos
     * @return Catalogo de Produtos
     */
    public CatProdutos getCatProdutos() {
        return this.catProdutos;
    }
    /**
     * Devolve faturação
     * @return Faturaçao
     */
    public Faturacao getFaturacao() {
        return this.faturacao;
    }
    /**
     * Devolve catalogo filiais
     * @return Catalogo de Filiais
     */
    public CatFilial getFiliais() {
        return this.filiais;
    }
    /**
     * Devolve produtos nunca comprados
     * @return ArrayList com os produtos nunca comprados
     */
    public ArrayList<Produto> prodNC(){
        ArrayList<Produto> res=catProdutos.prodNC(faturacao.getFaturas());
        return res;
    }
    /**
     * Devolve faturas emitidas num mês
     * @param mes mes 
     * @return ArrayList com todas as faturas de um mes
     */
    public ArrayList<Fatura> fatMensal(int mes){
        ArrayList<Fatura> res=faturacao.faturaMensal(mes);
        return res;
    }
    /**
     * Devolve faturas relacionadas com um cliente
     * @param cliente String identificadora do cliente
     * @return ArrayList com todas as faturas relacionadas com um cliente
     */
    public ArrayList<Fatura> faturaCliente(String cliente){
        ArrayList<Fatura> res=faturacao.faturaCliente(cliente);
        return res;
    }
    /**
     * Devolve faturas relacionadas com um produto
     * @param produto String identificadora do produto
     * @return ArrayList com todas as faturas relacionadas com um produto
     */
    public ArrayList<Fatura> faturaProduto(String produto){
        ArrayList<Fatura> res=faturacao.faturaProduto(produto);
        return res;
    }
}

package grupo24.projJava;

import java.io.Serializable;
import java.util.Objects;

public class Venda implements Serializable{
    private Produto produto;
    private double preco;
    private int quant;
    private char tipo;
    private Cliente cliente;
    private int mes;
    private int filial;
    /**
     * Construtor de Venda vazio
     */
    public Venda(){
        this.produto=null;
        this.preco=0;
        this.quant=0;
        this.tipo=' ';
        this.cliente=null;
        this.mes=0;
        this.filial=0;
    }
    /**
     * Construtor de venda parametrizador
     * @param produto Nome do Produto
     * @param preco Preco do Produto
     * @param quant Quantidade vendida
     * @param tipo Tipo de Venda
     * @param cliente Nome do cliente
     * @param mes Mes da venda
     * @param filial  Filial da venda
     */
    public Venda(Produto produto, double preco, int quant, char tipo, Cliente cliente, int mes, int filial) {
        this.produto = produto;
        this.preco = preco;
        this.quant = quant;
        this.tipo = tipo;
        this.cliente = cliente;
        this.mes = mes;
        this.filial = filial;
    }
    /**
     * Construtor de cópia
     * @param v Venda 
     */
    public Venda(Venda v){
        this.produto=v.getProduto();
        this.preco=v.getPreco();
        this.quant=v.getQuant();
        this.tipo=v.getTipo();
        this.cliente=v.getCliente();
        this.mes=v.getMes();
        this.filial=v.getFilial();
    }
    /**
     * Devolve o produto de uma venda
     * @return Produto 
     */
    public Produto getProduto() {
        return this.produto;
    }
    /**
     * Insere Produto numa venda
     * @param produto Produto a inserir
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    /**
     * Devolve preco de uma venda
     * @return Preco de uma Venda
     */
    public double getPreco() {
        return this.preco;
    }
    /**
     * Insere preco de uma venda
     * @param preco De uma venda
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }
    /**
     * Devolve quantidade de uma Venda
     * @return quantidade de uma venda
     */
    public int getQuant() {
        return this.quant;
    }
    /**
     * Insere quantidade numa venda
     * @param quant de uma Venda
     */
    public void setQuant(int quant) {
        this.quant = quant;
    }
    /**
     * Devolve tipo de uma venda
     * @return tipo de uma venda
     */
    public char getTipo() {
        return this.tipo;
    }
    /**
     * Insere tipo numa venda
     * @param tipo de uma venda
     */
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }
    /**
     * Devolve Cliente que efetuou uma venda
     * @return Cliente que efetuou uma venda
     */
    public Cliente getCliente() {
        return this.cliente;
    }
    /**
     * Insere Cliente numa venda
     * @param cliente de uma venda
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    /**
     * Devolve mes em que venda foi efetuada
     * @return mes em que a venda foi efetuada
     */
    public int getMes() {
        return this.mes;
    }
    /**
     * Insere mes em que uma venda foi efetuada
     * @param mes em que a venda foi efetuada
     */
    public void setMes(int mes) {
        this.mes = mes;
    }
    /**
     * Devolve filial em que uma venda foi efetuada
     * @return filial em que venda foi efetuada
     */
    public int getFilial() {
        return this.filial;
    }
    /**
     * Insere fiilial em que uma venda foi efetuada
     * @param filial em que venda foi efetuada
     */
    public void setFilial(int filial) {
        this.filial = filial;
    }
    /**
     * Cria cópia de uma venda
     * @return Venda
     */
    public Venda clone(){
        return new Venda(this);
    }
    /**
     * Cria um hashcode para a venda
     * @return Inteiro que representa o codigo hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.produto);
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.preco) ^ (Double.doubleToLongBits(this.preco) >>> 32));
        hash = 13 * hash + this.quant;
        hash = 13 * hash + this.tipo;
        hash = 13 * hash + Objects.hashCode(this.cliente);
        hash = 13 * hash + this.mes;
        hash = 13 * hash + this.filial;
        return hash;
    }

    /**
     * Compara Vendas
     * @param obj Objeto para comparar a uma venda
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Venda other = (Venda) obj;
        if (Double.doubleToLongBits(this.preco) != Double.doubleToLongBits(other.preco)) {
            return false;
        }
        if (this.quant != other.quant) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.filial != other.filial) {
            return false;
        }
        if (!Objects.equals(this.produto, other.produto)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        return true;
    }

    /**
     * Transforma venda numa String
     * @return String que indica os parametros de uma venda
     */
    @Override
    public String toString() {
        return "Venda{" + "produto=" + produto + ", preco=" + preco + ", quant=" + quant + ", tipo=" + tipo + ", cliente=" + cliente + ", mes=" + mes + ", filial=" + filial + '}';
    }
    /**
     * Valida uma Venda
     * @param c catalogo de Clientes
     * @param p catalogo de Produtos
     * @return bolean
     */
    public boolean ValidaVenda(CatClientes c,CatProdutos p){
        if (!c.contains(cliente)) return false;
        if (!p.contains(produto)) return false;
        if (tipo!='N' && tipo!='P') return false;
        if (mes<1 || mes>12) return false;
        return true;
    }
    /**
     * Função que verifica se uma venda foi feita a preco 0
     * @return boolean
     */
    public boolean vendaGratis(){
        if(this.preco==0) return true;
        else return false;
    }
}
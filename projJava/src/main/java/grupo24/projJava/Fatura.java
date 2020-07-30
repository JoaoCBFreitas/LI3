package grupo24.projJava;

import java.io.Serializable;


public class Fatura implements Serializable{
    private int id;
    private double preco;
    private Cliente cliente;
    private Produto produto;
    private int quantidade;
    private char promo;
    private int filial;
    private int mes;
    /**
     * Construtor parameterizado de fatura
     * @param id Id da fatura
     * @param preco Preco da fatura
     * @param cliente Cliente da fatura
     * @param produto Produto da fatura
     * @param quantidade Quantidade comprada na fatura
     * @param promo Indicador de Promoçao
     * @param filial Filial onde foi efetuada a compra
     * @param mes Mes em que foi emitida a fatura
     */
    public Fatura(int id, double preco, Cliente cliente, Produto produto, int quantidade, char promo, int filial, int mes){
        this.id = id;
        this.preco = preco;
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.promo = promo;
        this.filial = filial;
        this.mes = mes;
    }
    /**
     * Construtor cópia da fatura
     * @param f Fatura
     */
    public Fatura(Fatura f){
        this.id = f.id;
        this.preco = f.preco;
        this.cliente = f.cliente;
        this.produto = f.produto;
        this.quantidade = f.quantidade;
        this.promo = f.promo;
        this.filial = f.filial;
        this.mes = f.mes;
    }
    /**
     * Devolve o objeto fatura
     * @return Retorna uma fatura
     */
    public Fatura getFatura(){
        return this;
    }
    /**
     * Devolve o id de uma fatura
     * @return inteiro representante do id da fatura
     */
    public int getID(){
        return this.id;
    }
    /**
     * Devolve o preco de uma fatura
     * @return Preço da fatura
     */
    public double getPreco(){
        return this.preco;
    }
    /**
     * Devolve o cliente de uma fatura
     * @return Cliente que efetuou a compra
     */
    public Cliente getCliente(){
        return this.cliente;
    }
    /**
     * Devolve o produto de uma fatura
     * @return Produto comprado
     */
    public Produto getProduto(){
        return this.produto;
    }
    /**
     * Devolve o tipo de uma fatura
     * @return Caracter que indica se houve ou nao promoção
     */
    public char getPromo(){
        return this.promo;
    }
    /**
     * Devolve a quantidade de uma fatura
     * @return Quantidade de produto vendida
     */
    public int getQuantidade(){
        return this.quantidade;
    }
    /**
     * Devolve o mes de uma fatura
     * @return Mês em que a compra foi efectuada
     */
    public int getMes(){
        return this.mes;
    }
    /**
     * Devolve a filial de uma fatura
     * @return A filial onde foi registada a fatura
     */
    public int getFilial(){
        return this.filial;
    }
    /**
     * Funçao que converte fatura para string
     * @return String com os dados da fatura
     */
    @Override
    public String toString() {
        return "Fatura{" + "id=" + id + ", preco=" + preco + ", Cliente=" + cliente + ", Produto=" + produto + ", quantidade=" + quantidade + ", promo=" + promo + ", filial=" + filial + ", mes=" + mes + '}';
    }

}



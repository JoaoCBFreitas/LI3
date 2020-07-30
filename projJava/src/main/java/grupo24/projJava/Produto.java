package grupo24.projJava;

import java.io.Serializable;

public class Produto implements Serializable{
    private String produto;

    /**
     * Construtor Vazio de produtos
     */
    public Produto() {
        this.produto = "n/a";
    }

    /**
     * Construtor de produto através de uma String
     * 
     * @param produto String que identifica um produto
     */
    public Produto(String produto) {
        this.produto = produto;
    }

    /**
     * Construtor de um produto através de um outro produto
     * 
     * @param p Produto que identifica um produto
     */
    public Produto(Produto p) {
        this.produto = p.getProduto();
    }

    /**
     * Função que devolve o nome de um produto
     * 
     * @return Identificação do produto
     */
    public String getProduto() {
        return produto;
    }

    /**
     * Set de um produto através de uma String
     * 
     * @param produto String para alterar a identificação do produto
     */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /**
     * Cria uma cópia de um produto
     * 
     * @return Produto clonado
     */
    public Produto clone() {
        return new Produto(this);
    }

    /**
     * Função HashCode
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((produto == null) ? 0 : produto.hashCode());
        return result;
    }

    /**
     * Compara produtos
     * @param obj Objeto para comparar com um produto
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (produto == null) {
            if (other.produto != null)
                return false;
        } else if (!produto.equals(other.produto))
            return false;
        return true;
    }

    /**
     * Transforma produto numa String
     * @return Produto em forma de string
     */
    public String toString() {
        return "Produto [produto=" + produto + "]";
    }
    /**
     * Função que verifica se um produto é válido ou não
     * @return boolean
     */
    public boolean ValidaProduto(){
        if(this.produto.charAt(0)>='A' && this.produto.charAt(0)<='Z' && this.produto.charAt(1)>='A' && this.produto.charAt(1)<='Z'){
            String aux=this.produto.substring(2);
            int num=Integer.parseInt(aux);
            if(num>=1000 && num<=9999){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
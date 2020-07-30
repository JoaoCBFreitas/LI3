package grupo24.projJava;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CatProdutos implements Serializable {
/**
     *
     */
    private static final long serialVersionUID = 1L;
    private Set<Produto> catProdutos;

    /**
     * Construtor de um Catalogo de Produtos Vazio
     */
    public CatProdutos() {
        this.catProdutos = new TreeSet<>(new ProdutoComparador());
    }

    /**
     * Construtor de um Catalogo de produtos através de um outro catálogo de
     * produtos
     * 
     * @param p Catalogo de produtos
     */
    public CatProdutos(CatProdutos p) {
        this.catProdutos = p.getCatProdutos();
    }

    /**
     * Construtor de um catálogo de Produtos através de uma lista de Produtos
     * 
     * @param catProdutos Lista de Produtos
     */
    public CatProdutos(List<Produto> catProdutos) {
        this.catProdutos = new TreeSet<>(new ProdutoComparador());
        this.catProdutos.addAll(catProdutos);
    }

    /**
     * Devolve o catalogo de Produtos
     * 
     * @return Catalogo de Produtos
     */
    public Set<Produto> getCatProdutos() {
        return catProdutos;
    }

    /**
     * Insere o catalogo de Produtos ja existente
     * 
     * @param catProdutos já existente
     */
    public void setCatProdutos(Set<Produto> catProdutos) {
        this.catProdutos = catProdutos;
    }

    /**
     * Verifica se um Produto existe no catalogo
     * 
     * @param p Produto a verificar
     * @return boolean
     */
    public boolean contains(Produto p) {
        return catProdutos.contains(p);
    }
    
    /**
     * Devolve produtos nunca comprados
     * @param faturas Registo de todas as vendas
     * @return Retorna o número de produtos nunca comprados
     */
    public ArrayList<Produto> prodNC(TreeSet<Fatura> faturas){
        ArrayList<Produto> res = new ArrayList<>(); 
        TreeMap<Produto,Integer> aux = new TreeMap<>(new ProdutoComparador());
        for (Produto p : catProdutos)
            aux.put(p,0);
        for(Fatura f: faturas){
            int x=aux.get(f.getProduto());
            x+=f.getQuantidade();
            aux.put(f.getProduto(), x);
        }
        for(Produto p:aux.keySet()){
            if(aux.get(p)==0) res.add(p.clone());
        }
        return res;
    }
}

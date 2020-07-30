package grupo24.projJava;

import java.io.Serializable;
import java.util.Comparator;

public class ProdutoComparador implements Comparator<Produto>, Serializable {
    /**
     * Compara produtos alfabeticamente
     * 
     * @param a Primeiro Produto a comparar
     * @param b Segundo Produto a comparar
     * @return Resultado da comparação
     */
    public int compare(Produto a, Produto b) {
        return a.getProduto().compareTo(b.getProduto());
    }
}
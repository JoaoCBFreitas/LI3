package grupo24.projJava;

import java.io.Serializable;
import java.util.Comparator;

public class FaturaComparador implements Comparator<Fatura>, Serializable {
    /**
     * Compara produtos alfabeticamente
     * 
     * @param a Primeira Fatura a comparar
     * @param b Segunda Fatura a comparar
     * @return Resultado da Comparação
     */
    @Override
    public int compare(Fatura a, Fatura b) {
        return a.getID() - b.getID();
    }
}
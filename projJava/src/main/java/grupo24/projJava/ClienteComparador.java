package grupo24.projJava;

import java.io.Serializable;
import java.util.Comparator;

public class ClienteComparador implements Comparator<Cliente>, Serializable {
    /**
     * Compara clientes alfabeticamente
     * 
     * @param a Primeiro Cliente a comparar
     * @param b Segundo Cliente a comparar
     * @return Resultado da comparação
     */
    public int compare(Cliente a, Cliente b) {
        return a.getCliente().compareTo(b.getCliente());
    }
}
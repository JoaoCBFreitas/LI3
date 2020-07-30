package grupo24.projJava;

import java.io.Serializable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CatClientes implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Set<Cliente> catClientes;

    /**
     * Construtor de um Catalogo de Clientes Vazio
     */
    public CatClientes() {
        this.catClientes = new TreeSet<Cliente>(new ClienteComparador());
    }

    /**
     * Construtor de um Catalogo de clientes através de um outro catálogo de
     * clientes
     * 
     * @param c Catalogo de clientes
     */
    public CatClientes(CatClientes c) {
        this.catClientes = c.getCatClientes();
    }

    /**
     * Construtor de um catálogo de clientes através de uma lista de clientes
     * 
     * @param catClientes Lista de clientes
     */
    public CatClientes(List<Cliente> catClientes) {
        this.catClientes = new TreeSet<Cliente>(new ClienteComparador());
        this.catClientes.addAll(catClientes);
    }

    /**
     * Devolve o catalogo de clientes
     * 
     * @return Catalogo de clientes
     */
    public Set<Cliente> getCatClientes() {
        return catClientes;
    }

    /**
     * Insere o catalogo de clientes ja existente
     * 
     * @param catClientes já existente
     */
    public void setCatClientes(Set<Cliente> catClientes) {
        this.catClientes = catClientes;
    }

    /**
     * Verifica se um cliente existe no catalogo
     * 
     * @param c Cliente a verificar
     * @return boolean
     */
    public boolean contains(Cliente c) {
        return catClientes.contains(c);
    }
}

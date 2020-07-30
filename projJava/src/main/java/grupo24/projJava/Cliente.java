package grupo24.projJava;

import java.io.Serializable;

public class Cliente implements Serializable{
    private String cliente;

    /**
     * Construtor de Cliente vazio
     */
    public Cliente() {
        this.cliente = "n/a";
    }

    /**
     * Construtor de um cliente através de uma string
     * 
     * @param cliente String que indica o cliente
     */
    public Cliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Contrutor de cliente através de outro cliente
     * 
     * @param c Cliente já existente
     */
    public Cliente(Cliente c) {
        this.cliente = c.getCliente();
    }

    /**
     * Devolve o cliente
     * 
     * @return string do cliente
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Set de um cliente através de um string
     * 
     * @param cliente String indicadora do cliente
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Cria uma cópia do cliente
     * 
     * @return Cópia de um cliente
     */
    public Cliente clone() {
        return new Cliente(this);
    }

    /**
     * Função HashCode
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        return result;
    }

    /**
     * Compara Clientes
     * 
     * @param obj verifica se um objecto e igual a um cliente
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cliente other = (Cliente) obj;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        return true;
    }

    /**
     * Transforma um cliente numa String
     */
    public String toString() {
        return "Cliente [cliente=" + cliente + "]";
    }
    /**
     * Verifica se um cliente é válido
     * @return boolean
     */
    public boolean ValidaCliente(){
        if(this.cliente.charAt(0)>='A' && this.cliente.charAt(0)<='Z'){
            String aux=this.cliente.substring(1);
            int num=Integer.parseInt(aux);
            if(num>=1000 && num<=5000){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
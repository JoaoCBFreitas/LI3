package grupo24.projJava;

import java.io.Serializable;
import java.util.ArrayList;

public class CatFilial implements Serializable{
    
    private ArrayList<Filial> filiais;
    /**
     * Construtor vazio catFilial
     */
    public CatFilial(){
        this.filiais = new ArrayList<>();
    }
    /**
     * Construtor copia CatFilial
     * @param f Catalogo de Filiais
     */
    public CatFilial(CatFilial f){
        this.filiais = f.getFiliais();
    }
    /**
     * Construtor parameterizado catFilial
     * @param f ArrayList de Filais
     */
    public CatFilial(ArrayList<Filial> f){
        this.filiais=f;
    }
    /**
     * Devolve todas as filiais
     * @return Retorna todas as filiais num ArrayList
     */
    public ArrayList<Filial> getFiliais(){
        return this.filiais;
    }
    /**
     * Devolve uma certa filial
     * @param id id Identificador da filial 
     * @return Devolve a filial indicada
     */
    public Filial getFilial(int id){
        return filiais.get(id-1);
    }
    
}

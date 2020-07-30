/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo24.projJava;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.out;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javafx.util.Pair;


public class Queries implements Interface{ 
    /**
    * Função que lê os ficheiros input e preenche SGV
    * @param clientes nome do ficheiro dos clientes
    * @param produtos nome do ficheiro dos produtos
    * @param vendas nome do ficheiro das vendas
    * @return Sistema de gestao de vendas
    */
    public SGV carregaFicheiros(String clientes,String produtos,String vendas){
        String nomeVendas=vendas;
        int vendasErradas,compras0,fatTotal=0;
        Crono.start();
        ArrayList<Cliente> cArray=readClientes(clientes);
        ArrayList<Produto> pArray=readProdutos(produtos);
        ArrayList<Venda> vArray=readVendas(vendas);        
        //Valida Cliente
        ArrayList<Cliente> clientesVal=(ArrayList<Cliente>) cArray.stream().filter(c -> c.ValidaCliente()).collect(Collectors.toList());
        //Valida Produto
        ArrayList<Produto> produtosVal=(ArrayList<Produto>) pArray.stream().filter(p->p.ValidaProduto()).collect(Collectors.toList());
        CatClientes catclientes=new CatClientes(clientesVal);
        CatProdutos catprodutos=new CatProdutos(produtosVal);
        //Valida vendas
        ArrayList<Venda> vendasVal=(ArrayList<Venda>) vArray.stream().filter(v -> v.ValidaVenda(catclientes,catprodutos)).collect(Collectors.toList());
        
        HashMap<Produto,Integer> prod=new HashMap<>();
        HashMap<Cliente,Integer> cli=new HashMap<>();
        ArrayList<Filial> filiais=new ArrayList<>();
        filiais.add(0,new Filial(1));//Criar Filiais vazias
        filiais.add(1,new Filial(2));//Criar Filiais vazias
        filiais.add(2,new Filial(3));//Criar Filiais vazias
        //CRIA FATURACAO
        List<Fatura> f=new ArrayList<>();
        int i=0;
        for(Venda v: vendasVal){
            Filial aux=filiais.get(v.getFilial()-1);
            aux.addVenda(v);
            filiais.set(v.getFilial()-1,aux);
            //---------------------------------------------------------------------------------------------------------------
            Fatura fataux=new Fatura(i,v.getPreco(),v.getCliente(),v.getProduto(),v.getQuant(),v.getTipo(),v.getFilial(),v.getMes());     
            f.add(fataux);
            if(cli.containsKey(v.getCliente())){
                int a=cli.get(v.getCliente());
                a++;
                cli.put(v.getCliente(),a);
            }else{
                cli.put(v.getCliente(),1);
            }
            if(prod.containsKey(v.getProduto())){
                int a=prod.get(v.getProduto());
                a++;
                prod.put(v.getProduto(), a++);
            }else{
                prod.put(v.getProduto(),1);
            }
            //---------------------------------------------------------------------------------------------------------------
            fatTotal+=v.getPreco();
            i++;
        }
        CatFilial filial=new CatFilial(filiais);
        Faturacao faturas=new Faturacao(f);
        SGV sgv=new SGV(catclientes,catprodutos,faturas,filial);
        double timeF=Crono.stop();
        System.out.println("Tempo de leitura->"+timeF+"s");
        compras0 = (int) vendasVal.stream()
                                  .filter(v -> v.vendaGratis())
                                  .count();
        //Calcula o numero de vendas erradas
        vendasErradas=vArray.size()-vendasVal.size();
        //INFO
        System.out.println("Nome do Ficheiro Vendas->"+nomeVendas);
        System.out.println("Número de vendas inválidas->"+vendasErradas);
        System.out.println("Número total de produtos->"+produtosVal.size());
        System.out.println("Número total de diferentes produtos comprados->"+prod.size());
        System.out.println("Número total de diferentes produtos não comprados->"+(produtosVal.size()-prod.size()));
        System.out.println("Número total de clientes->"+clientesVal.size());
        System.out.println("Número total de clientes que realizaram compras->"+cli.size());
        System.out.println("Número total de clientes que não realizaram compras->"+(clientesVal.size()-cli.size()));
        System.out.println("Total de compras de valor igual a 0->"+compras0);
        System.out.println("Faturação total->"+fatTotal);
        return sgv;
    }
    /**
    * Função que imprime MENU interativo+gere queries
    * @param sgv sistema de gestao de vendas
    */
    public void printMenu(SGV sgv){
        int op=-1;
        do{
            System.out.println("--------------------------------Consultas Estatísticas----------------------------------");
            System.out.println("1 - Número total de compras por mês");
            System.out.println("2 - Faturação total por Mês");
            System.out.println("3 - Número de distintos clientes que compraram em cada mês filial a filial");
            System.out.println("--------------------------------Consultas Interativas----------------------------------");
            System.out.println("4 - Lista ordenada com os códigos de produtos nunca comprados e o seu total");
            System.out.println("5 - Dado um mês válido, determinar o número total global de vendas realizadas e o número total de clientes distintos que as fizeram para cada filial");
            System.out.println("6 - Dado  um  código  de  cliente, determinar, para cada mês, quantas compras fez, quantos produtos distintos comprou e quanto gastou no total");
            System.out.println("7 - Dado o código de um produto existente, determinar, mês a mês, quantas vezes foi comprado, por quantos clientes diferentes e o total faturado");
            System.out.println("8 - Dado o código de um cliente determinar a lista de códigos de produtos que mais comprou, ordenada por ordem decrescente de quantidade e, para quantidades iguais, por ordem alfabética dos códigos");
            
            System.out.println("9- Determinar o conjunto dos X produtos mais vendidos em todo o ano indicando o número total de distintos clientes que o compraram");
            System.out.println("10- Determinar, para  cada  filial, a lista dos três  maiores compradores em termos de dinheiro faturado");
            System.out.println("11- Determinar os códigos dos X clientes que compraram mais produtos diferentes, indicando quantos, sendo o critério de ordenação a ordem decrescente do número de produtos");
            System.out.println("12- Dado o código de um produto que deve existir, determinar o conjunto dos X clientes que mais o compraram e, para cada um,qual o valor gasto");
            System.out.println("13- Determinar mês a mês, e para cada mês filial a filial, a faturação total com cada produto");
            System.out.println("14- Gravar em ficheiro de dados");
            System.out.println("15- Carregar através de um ficheiro");
            System.out.println("0 -Sair");
            op=Input.lerInt();
            switch(op){
                case 1:
                    HashMap<Integer,Integer> compraMes=comprasMes(sgv);
                    for(int i=1;i<13;i++){
                        System.out.println("Compras no mês "+i+" ->"+compraMes.get(i));
                    }
                    op=-1;
                    break;
                case 2:
                    HashMap<Integer,Double> fatMes=faturacaoMes(sgv);
                    for(int i=1;i<13;i++){
                        System.out.println("Faturação no mês "+i+" ->"+fatMes.get(i));
                    }
                    op=-1;
                    break;
                case 3:
                    Crono.start();
                    HashMap<Integer,Integer> distClientes1=distClientes(1,sgv);
                    HashMap<Integer,Integer> distClientes2=distClientes(2,sgv);
                    HashMap<Integer,Integer> distClientes3=distClientes(3,sgv);
                    double timeF=Crono.stop();
                    System.out.println("Tempo de execução query 3: " + timeF+"s");
                    for(int i=1;i<13;i++){
                        System.out.println("Filial 1->Clientes no mês "+i+" ->"+distClientes1.get(i));
                    }
                    for(int i=1;i<13;i++){
                        System.out.println("Filial 2->Clientes no mês "+i+" ->"+distClientes2.get(i));
                    }
                    for(int i=1;i<13;i++){
                        System.out.println("Filial 3->Clientes no mês "+i+" ->"+distClientes3.get(i));
                    }
                    op=-1;
                    break;
                case 4:
                    ArrayList<Produto> nuncaComp=prodNC(sgv);
                    System.out.println(nuncaComp.size()+" produtos nunca comprados");
                    Iterator it = nuncaComp.iterator();
                    int sw=0, z=0, i=0, totalS = nuncaComp.size();
                    while(it.hasNext() && sw!=1){
                        if (z==120) {
                            System.out.println("   ---------------------Enter para Apresentar Mais-------------------"+i/120+"/"+((totalS/120)+((totalS%120)/(totalS%120))));
                            String aux = Input.lerString().trim();
                            if (aux.equals("")) {i--;z=0;}
                            else {sw=1;}
                        }
                        else {
                            System.out.print("  |  "+it.next());
                            if ((i%6)==5) System.out.print("  |\n");
                            z++;
                        }
                        i++;
                    }
                    if (sw==0) System.out.println("  |");
                    Input.lerString();
                    op=-1;
                    break;
                case 5:
                    System.out.println("Indique um mês válido");
                    int mes=0;
                    while(mes<=0 || mes>12){
                        mes=Input.lerInt();
                    }
                    HashMap<Integer,ArrayList<Cliente>> mesClientes=resultadoMensal(sgv,mes);
                    for(int j=0;j<3;j++){
                        System.out.println("Clientes únicos na filial "+(j+1)+"->"+mesClientes.get(j).size());
                    }
                    op=-1;
                    break;
                case 6:
                    System.out.println("Indique o cliente");
                    String cliente="";
                    cliente=Input.lerString();
                    HashMap<Integer,ArrayList<Double>> comprasCli=comprasCliente(sgv,cliente);
                    String aux="";
                    System.out.println("Resultado anual do cliente "+cliente);
                    for(i=1;i<13;i++){
                        System.out.println("------MES "+i+"------");
                        System.out.println("Compras efetuadas->"+comprasCli.get(i).get(0));
                        System.out.println("Produtos distintos->"+comprasCli.get(i).get(1));
                        System.out.println("Gastos->"+comprasCli.get(i).get(2));
                        System.out.println("Enter para continuar");
                        aux=Input.lerString();
                        if(Objects.equals(aux,"")){
                            continue;
                        }else{
                            break;
                        }
                    }
                    op=-1;
                    break;
                case 7:
                    System.out.println("Indique o produto");
                    String prod="";
                    prod=Input.lerString();
                    HashMap<Integer,ArrayList<Double>> comprasProd=comprasProduto(sgv,prod);
                    aux="";
                    System.out.println("Resultado anual do produto "+prod);
                    for(i=1;i<13;i++){
                        System.out.println("------MES "+i+"------");
                        System.out.println("Compras efetuadas->"+comprasProd.get(i).get(0));
                        System.out.println("Clientes distintos->"+comprasProd.get(i).get(1));
                        System.out.println("Lucro->"+comprasProd.get(i).get(2));
                        System.out.println("Enter para continuar");
                        aux=Input.lerString();
                        if(Objects.equals(aux,"")){
                            continue;
                        }else{
                            break;
                        }
                    }
                    op=-1;
                    break;
                case 8:
                    System.out.println("Indique o cliente");
                    String cliente1="";
                    cliente1=Input.lerString();
                    Map<Produto,Integer> maisComprou=maisComprou(sgv,cliente1);
                    for(Map.Entry<Produto,Integer> entry:maisComprou.entrySet()){
                        String key = entry.getKey().getProduto();
                        Integer value = entry.getValue();
                        System.out.println(key + " => " + value);
                    }
                    op=-1;
                    break;
                case 9:
                    System.out.println("Indique o numero");
                    int x;
                    x=Input.lerInt();
                    HashMap<Produto, Integer> hashC = new HashMap<>();
                    Map<Produto, Integer> hash = new HashMap<>();
                    hash = getQuantidadeProdutos(sgv, hashC);
                    ArrayList<Produto> produtos = new ArrayList(hash.keySet());
                    System.out.println("Os " + x + " produtos mais vendidos foram:");
                    for(i=0; i<x; i++){
                        System.out.println(produtos.get(i) + " - " + hashC.get(produtos.get(i)));
                    }
                    op=-1;
                    break;
                case 10:
                    System.out.println("Top 3 Clientes da Filial 1:");
                    ArrayList<Cliente> clientes = melhoresClientes(sgv, 1);
                    for(i = 0; i<3; i++){
                        System.out.println(clientes.get(i).toString());
                    }
                    System.out.println("Top 3 Clientes da Filial 2:");
                    clientes = melhoresClientes(sgv, 2);
                    for(i = 0; i<3; i++){
                        System.out.println(clientes.get(i).toString());
                    }
                    System.out.println("Top 3 Clientes da Filial 3:");
                    clientes = melhoresClientes(sgv, 3);
                    for(i = 0; i<3; i++){
                        System.out.println(clientes.get(i).toString());
                    }
                    op=-1;
                    break;
                case 11:
                    System.out.println("Indique o numero");
                    int num;
                    num=Input.lerInt();
                    List<Pair<Cliente, Integer>> list = new ArrayList<>();
                    list = clientesMaisDiversos(sgv);
                    for(i=0; i<num; i++){
                        System.out.println(list.get(i).toString());
                    }
                    op=-1;
                    break;
                case 12:
                    System.out.println("Indique o produto");
                    String produto;
                    produto=Input.lerString();
                    System.out.println("Indique o numero");
                    num=Input.lerInt();
                    Produto p = new Produto(produto);
                    HashMap<Cliente, Double> hashP = new HashMap<>();
                    List<Cliente> listClientes = clientesMaisCompraram(sgv, p, hashP);
                    if(listClientes.size() < num)
                        num = listClientes.size();
                    for(i=0; i<num; i++){
                        System.out.println(listClientes.get(i).toString() + " - " + hashP.get(listClientes.get(i)));
                    }
                    op = -1;
                    break;
                case 13:
                    ArrayList<Double> total = getFatTotalMes(sgv);
                    ArrayList<String> meses = new ArrayList<>();
                    meses.add("Janeiro"); meses.add("Fevereiro"); meses.add("Março");
                    meses.add("Abril"); meses.add("Maio"); meses.add("Junho"); meses.add("Julho");
                    meses.add("Agosto"); meses.add("Setembro"); meses.add("Outubro"); meses.add("Novembro"); meses.add("Dezembro");
                    System.out.println("Filial 1:");
                    for(i=0; i<12; i++){
                        System.out.println(meses.get(i) + " - " + total.get(i));
                    }
                    System.out.println("Filial 2:");
                    for(i=12; i<24; i++){
                        System.out.println(meses.get(i-12) + " - " + total.get(i));
                    }
                    System.out.println("Filial 3:");
                    for(i=24; i<36; i++){
                        System.out.println(meses.get(i-24) + " - " + total.get(i));
                    }
                    op = -1;
                    break;
                case 14:                    
                    System.out.println("A gravar em ficheiro de dados");
                    guarda(sgv);
                    op=-1;
                    break;
                case 15:
                    System.out.println("A carregar ficheiro");
                    sgv=carrega();
                    printMenu(sgv);
                    break;
                case 0:
                    System.out.println("Saindo do programa");
                    break;
                default:
                    System.out.println("Input Inválido");
                    break;
            }
        }while(op<0 ||op>16);
    }
    /**
    * Função que lê o ficheiro dos clientes
    * @param fich nome do ficheiro dos clientes
    * @return ArrayList com todos os clientes lidos
    */
    public ArrayList<Cliente> readClientes(String fich){
        ArrayList<Cliente> clientes = new ArrayList<>();
        BufferedReader inStream = null;
        String linha = null;
        try {
            inStream = new BufferedReader(new FileReader(fich));
            while( (linha = inStream.readLine()) != null ) {
                clientes.add(new Cliente(linha.trim()));
            }
        } catch(IOException e) {
            out.println(e.getMessage()); return null;
        }
        return clientes;  
    }
    /**
    * Função que lê o ficheiro dos produtos
    * @param fich nome do ficheiro dos produtos
    * @return ArrayList com todos os produtos lidos
    */
    public ArrayList<Produto> readProdutos(String fich){
        ArrayList<Produto> produtos = new ArrayList<>();
        BufferedReader inStream = null;
        String linha = null;
        try {
            inStream = new BufferedReader(new FileReader(fich));
            while( (linha = inStream.readLine()) != null ) {
                produtos.add(new Produto(linha.trim()));
            }
        } catch(IOException e) {
            out.println(e.getMessage()); return null;
        }
        return produtos;  
    }
    /**
    * Função que lê o ficheiro das vendas
    * @param fich nome do ficheiro das vendas
    * @return ArrayList com todos as vendas lidas
    */    
    public ArrayList<Venda> readVendas(String fich){
        ArrayList<Venda> ret = new ArrayList<>();
        BufferedReader inStream = null;
        String linha = null;
        try {
            inStream = new BufferedReader(new FileReader(fich));
            while( (linha = inStream.readLine()) != null ) {
                String[] aux = linha.trim().split(" ");
                ret.add(new Venda(new Produto(aux[0]),
                                  Double.parseDouble(aux[1]),
                                  Integer.parseInt(aux[2]),
                                  aux[3].charAt(0),
                                  new Cliente(aux[4]),
                                  Integer.parseInt(aux[5]),
                                  Integer.parseInt(aux[6])));
            }
        } catch(IOException e) {
            out.println(e.getMessage()); return null;
        }
        return ret;  
    }
    /**
    * Função que carrega SGV através de um ficheiro
    * @return Sistema de Gestão de Vendas
    */
    public SGV carrega(){
        Crono.start();
        try{
            SGV sgv  = new SGV();
            sgv.loadState();
            double timeF = Crono.stop();
            System.out.println("Tempo de execução Query 15 " + timeF+"s");
            return sgv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Função que guarda o estado atual num ficheiro
     * @param sgv Sistema de Gestão de Vendas
     */
    public void guarda(SGV sgv){
        Crono.start();
        try{
            sgv.saveState();
            double timef=Crono.stop();
            System.out.println("Tempo de execução Query 14 " + timef+"s");
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
    /**
     * Função que calcula o número de compras efetuadas em todos os meses
     * @param sgv Sistema de Gestão de Vendas
     * @return HashMap com o número de compras num ano, mês a mês, em todo o SGV
     */
    public HashMap<Integer,Integer> comprasMes(SGV sgv){
        TreeSet<Fatura> f=sgv.getFaturacao().getFaturas();
        HashMap<Integer,Integer> compraMes=new HashMap<>();
        Crono.start();
        for(Fatura fat: f){
            if(compraMes.containsKey(fat.getMes())){
                int a=compraMes.get(fat.getMes());
                a++;
                compraMes.put(fat.getMes(), a);
            }else{
                compraMes.put(fat.getMes(),1);
            }
        }
        double timeFim=Crono.stop();
        System.out.println("Tempo Execução Query 1->"+timeFim+"s");
        return compraMes;
    }
    /**
     * Função que calcula a faturação em todos os meses
     * @param sgv Sistema de Gestão de Vendas
     * @return HashMap com faturação num ano, mês a mês, em todo o SGV
     */
    public HashMap<Integer,Double> faturacaoMes(SGV sgv){
        TreeSet<Fatura> f=sgv.getFaturacao().getFaturas();
        HashMap<Integer,Double> compraMes=new HashMap<>();
        Crono.start();
        for(Fatura fat: f){
            if(compraMes.containsKey(fat.getMes())){
                double a=compraMes.get(fat.getMes());
                a+=fat.getPreco();
                compraMes.put(fat.getMes(), a);
            }else{
                compraMes.put(fat.getMes(),fat.getPreco());
            }
        }
        double timeFim=Crono.stop();
        System.out.println("Tempo Execução Query 2->"+timeFim+"s");
        return compraMes;
    }
    /**
     * Função que calcula o numero de clientes distintos a fazerem compras numa certa filial mês a mês
     * @param i Filial
     * @param sgv Sistema de Gestão de Vendas
     * @return HashMap com clientes distintos num mês
     */
    public HashMap<Integer,Integer> distClientes(int i,SGV sgv){
        HashMap<Integer,Integer> res=new HashMap<>();
        Filial f=sgv.getFiliais().getFiliais().get(i-1);
        HashMap<Cliente, List<Venda>> clientesVendas=f.getClientesVendas();
        for(Map.Entry<Cliente, List<Venda>> entry : clientesVendas.entrySet()) {
            List<Venda> lista=entry.getValue();
            for(Venda v: lista){
                if(res.containsKey(v.getMes())){
                    int a=res.get(v.getMes());
                    a++;
                    res.put(v.getMes(),a);
                }else{
                    res.put(v.getMes(),1);
                }
            }
        }
        return res;
    }
    /**
     * Função que devolve uma lista ordenada de todos os produtos nunca comprados e o seu total
     * @param sgv Sistema de Gestão de Vendas
     * @return ArrayList de produtos nunca comprados
     */
    public ArrayList<Produto> prodNC(SGV sgv){
        Crono.start();
        ArrayList<Produto> res=sgv.prodNC();
        double timeF = Crono.stop();
        System.out.println("Tempo de execução Query 4->" + timeF+"s");
        return res;
    }
    /**
     * Dado um mês válido, determina o número total global de vendas e o total de clientes distintos para cada filial
     * @param sgv Sistema de Gestão de Vendas
     * @param i Mes válido
     * @return HashMap com clientes distintos que compraram numa filial 
     */
    public HashMap<Integer,ArrayList<Cliente>> resultadoMensal(SGV sgv,int i){
        Crono.start();
        ArrayList<Fatura> faturaMensal=sgv.fatMensal(i);
        HashMap<Integer,ArrayList<Cliente>> existente=new HashMap<>();
        existente.put(0, new ArrayList<Cliente>());
        existente.put(1,new ArrayList<Cliente>());
        existente.put(2,new ArrayList<Cliente>());
        for(Fatura f: faturaMensal){
            ArrayList<Cliente> c=existente.get(f.getFilial()-1);
            if(!c.contains(f.getCliente())) c.add(f.getCliente());
            existente.put(f.getFilial()-1,c);
        }
        double timeF=Crono.stop();
        System.out.println("Tempo de execução da Query 5->"+timeF+"s");
        System.out.println("Número total de vendas->"+faturaMensal.size());
        return existente;
    }
    /**
     * Determina uma série de estatísticas relativas às compras de um cliente
     * @param sgv Sistema de Gestão de Vendas
     * @param cliente código de cliente
     * @return HashMap com estatísticas para um certo mês
     */
    public HashMap<Integer,ArrayList<Double>> comprasCliente(SGV sgv,String cliente){
        Crono.start();
        ArrayList<Fatura> faturaCliente=sgv.faturaCliente(cliente);
        HashMap<Integer,Integer> comprasMensais=new HashMap<>();
        HashMap<Integer,ArrayList<Produto>> produtosMensais=new HashMap<>();
        HashMap<Integer,Double> gastoMensal=new HashMap<>();
        for(int i=1;i<13;i++){
            comprasMensais.put(i,0);
            produtosMensais.put(i, new ArrayList<Produto>());
            gastoMensal.put(i,(double)0);
        }
        for(Fatura f:faturaCliente){
            int compras=comprasMensais.get(f.getMes());
            ArrayList<Produto> produtos=produtosMensais.get(f.getMes());
            double gasto=gastoMensal.get(f.getMes());
            compras++;
            if(!produtos.contains(f.getProduto())) produtos.add(f.getProduto());
            gasto+=f.getPreco();
            comprasMensais.put(f.getMes(),compras);
            produtosMensais.put(f.getMes(),produtos);
            gastoMensal.put(f.getMes(), gasto);
        }
        double timeF=Crono.stop();
        System.out.println("Tempo de execução da Query 6->"+timeF+"s");
        HashMap<Integer,ArrayList<Double>> res=new HashMap<>();
        for(int i=1;i<13;i++){
            ArrayList<Double> aux=new ArrayList<>();
            aux.add((double) comprasMensais.get(i));
            aux.add((double) produtosMensais.get(i).size());
            aux.add(gastoMensal.get(i));
            res.put(i,aux);
        }
        return res;
    }
    /**
     * Determina uma série de estatísticas relativas às compras de um produto
     * @param sgv sistema de gestao de vendas
     * @param produto código de produtos
     * @return HashMap com estatísticas para um certo mês
     */
    public HashMap<Integer,ArrayList<Double>> comprasProduto(SGV sgv,String produto){
        Crono.start();
        ArrayList<Fatura> faturaProduto=sgv.faturaProduto(produto);
        HashMap<Integer,Integer> comprasMensais=new HashMap<>();
        HashMap<Integer,ArrayList<Cliente>> clientesMensais=new HashMap<>();
        HashMap<Integer,Double> faturadoMensal=new HashMap<>();
        for(int i=1;i<13;i++){
            comprasMensais.put(i,0);
            clientesMensais.put(i, new ArrayList<Cliente>());
            faturadoMensal.put(i,(double)0);
        }
        for(Fatura f: faturaProduto){
            int compras=comprasMensais.get(f.getMes());
            ArrayList<Cliente> clientes=clientesMensais.get(f.getMes());
            double gasto=faturadoMensal.get(f.getMes());
            compras++;
            if(!clientes.contains(f.getCliente())) clientes.add(f.getCliente());
            gasto+=f.getPreco();
            comprasMensais.put(f.getMes(), compras);
            clientesMensais.put(f.getMes(),clientes);
            faturadoMensal.put(f.getMes(), gasto);
        }
        double timeF=Crono.stop();
        System.out.println("Tempo de execução da Query 7->"+timeF+"s");
        
        
        HashMap<Integer,ArrayList<Double>> res=new HashMap<>();
        for(int i=1;i<13;i++){
            ArrayList<Double> aux=new ArrayList<>();
            aux.add((double) comprasMensais.get(i));
            aux.add((double) clientesMensais.get(i).size());
            aux.add(faturadoMensal.get(i));
            res.put(i,aux);
        }
        return res;
    }
    /**
     * Determina os produtos que um cliente mais comprou
     * @param sgv sistema de gestao de vendas
     * @param cliente código de cliente
     * @return HashMap com a relação Produto e quantidade de vezes que foi comprado
     */
    public Map<Produto,Integer> maisComprou(SGV sgv,String cliente){
        Crono.start();
        ArrayList<Fatura> faturaCliente=sgv.faturaCliente(cliente);
        HashMap<Produto,Integer> res=new HashMap<>();
        for(Fatura f: faturaCliente){
            if(res.containsKey(f.getProduto())){
                int quant=res.get(f.getProduto());
                quant+=f.getQuantidade();
                res.put(f.getProduto(), quant);
            }else{
                int quant=f.getQuantidade();
                res.put(f.getProduto(),quant);
            }
        }
        //ordena por chave
        TreeMap<Produto,Integer> sortedaux=new TreeMap<>(new ProdutoComparador());
        sortedaux.putAll(res);
        //ordena por valor
        Map<Produto, Integer> sortedMap = sortedaux.entrySet().stream()
                .sorted(Map.Entry.<Produto,Integer>comparingByValue().reversed())              
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));

        double timeF=Crono.stop();
        System.out.println("Tempo de execução da Query 8->"+timeF+"s");
        return sortedMap;
    }
    
    /**
     * Função que dado um produto indica a quantidade que foi vendida deste mesmo, e o numero de clientes distintos que o comprou.
     * @param sgv sistema de gestao de vendas
     * @param p Indicador do produto
     * @return Array que guarda os valores de quantidade e de clientes.
     */
    public int[] somaQuantidadeProduto(SGV sgv, Produto p){
        int[] res = new int[2];
        int soma = 0;
        AbstractCollection<Object> clientes = new ArrayList<Object>(); 
        List<Venda> vendas1 = sgv.getFiliais().getFilial(1).getListVendasP(p);
        List<Venda> vendas2 = sgv.getFiliais().getFilial(2).getListVendasP(p);
        List<Venda> vendas3 = sgv.getFiliais().getFilial(3).getListVendasP(p);
        for(Venda v : vendas1){
            clientes.add(v.getCliente());
        }
        for(Venda v : vendas2){
            clientes.add(v.getCliente());
        }
        for(Venda v : vendas3){
            clientes.add(v.getCliente());
        }
        soma = vendas1.stream().map((v) -> v.getQuant()).reduce(soma, Integer::sum);
        soma += vendas2.stream().map((v) -> v.getQuant()).reduce(soma, Integer::sum);
        soma += vendas3.stream().map((v) -> v.getQuant()).reduce(soma, Integer::sum);
        res[0] = soma;
        res[1] = clientes.size();
        return res;
    }
    
    /**
     * Função que utilizando a somaQuantidadeProduto corre a lista dos produtos todos
     * e preenche dois HashMaps com as relações entre produtos e quantidades e também produto e clientes distintos
     * @param sgv sistema de gestao de vendas
     * @param hashC HashMap a ser preenchido com os produtos e os clientes distintos que os compraram
     * @return Um sortedMap que é a versão ordenada do HashMap que contem as quantidades.
     */
    public Map<Produto, Integer> getQuantidadeProdutos(SGV sgv, HashMap<Produto, Integer> hashC){
        Crono.start();
        int[] valor;
        Produto p;
        HashMap<Produto, Integer> hash = new HashMap<>();
        Set<Produto> prods = sgv.getCatProdutos().getCatProdutos();
        Iterator<Produto> it = prods.iterator();
        while(it.hasNext()){
            p = it.next();
            valor = somaQuantidadeProduto(sgv, p);
            hash.put(p, valor[0]);
            hashC.put(p, valor[1]);
        }
        Map<Produto, Integer> sortedMap = 
                        hash.entrySet().stream()
                        .sorted(Map.Entry.<Produto, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        
        double timef=Crono.stop();
        System.out.println("Tempo de execução Query 9 " + timef+"s");
        return sortedMap;
    }
    
    /**
     * Função que dada uma filial soma o total gasto de cada cliente e insere num HashMap o seu código como chave e 
     * a quantidade gasta como valor. De seguida ordena o HashMap de forma descendente e retorna um ArrayList com os clientes.
     * @param sgv sistema de gestao de vendas
     * @param fil Indicador da filial
     * @return Um ArrayList que indica os clientes que gastaram mais dinheiro
     */
    public ArrayList<Cliente> melhoresClientes(SGV sgv, int fil){
        Crono.start();
        double total = 0;
        HashMap<Cliente, Double> compras = new HashMap<>();
        Map<Cliente, List<Venda>> cv = sgv.getFiliais().getFilial(fil).getClientesVendas();
        for(Cliente c : cv.keySet()){
            List<Venda> vendas = cv.get(c);
            for(Venda v : vendas){
               total += v.getPreco();
            }
            compras.put(c, total);
            total = 0;
        }
        Map<Cliente, Double> sortedMap = 
                        compras.entrySet().stream()
                        .sorted(Map.Entry.<Cliente, Double>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        
        ArrayList<Cliente> cli = new ArrayList(sortedMap.keySet());
        double timef=Crono.stop();
        System.out.println("Tempo de execução Query 10 " + timef+"s");
        return cli;
    }
    
    /**
     * Função que dado um produto e um HashMap preenche este HashMap e outro com as os clientes que compraram este produto
     * sendo que um deles e preenchido com as quantidades compradas e outro com os valores gastos, ordena o HashMap das quantidades
     * e transforma as suas chaves numa lista
     * @param sgv sistema de gestao de vendas
     * @param prod indicador do produto selecionado
     * @param hashP HashMap a ser preenchido para guardar os preços
     * @return List de clientes que contem os clientes que mais compraram o produto p
     */
    public List<Cliente> clientesMaisCompraram(SGV sgv, Produto prod, HashMap<Cliente, Double> hashP){
        Crono.start();
        int quantidade;
        double preco;
        HashMap<Cliente, Integer> hashQ = new HashMap<>();        
        List<Venda> vendas1 = sgv.getFiliais().getFilial(1).getListVendasP(prod);
        List<Venda> vendas2 = sgv.getFiliais().getFilial(2).getListVendasP(prod);
        List<Venda> vendas3 = sgv.getFiliais().getFilial(3).getListVendasP(prod);
        
        for(Venda v : vendas1){
            quantidade = v.getQuant();
            preco = v.getPreco();
            if(hashQ.containsKey(v.getCliente())){
                hashQ.put(v.getCliente(), hashQ.get(v.getCliente())+quantidade);
                hashP.put(v.getCliente(), hashQ.get(v.getCliente())+preco);
            }
            else{
                hashQ.put(v.getCliente(), quantidade);
                hashP.put(v.getCliente(), preco);
            }
        }
        for(Venda v : vendas2){
            quantidade = v.getQuant();
            preco = v.getPreco();
            if(hashQ.containsKey(v.getCliente())){
                hashQ.put(v.getCliente(), hashQ.get(v.getCliente())+quantidade);
                hashP.put(v.getCliente(), hashQ.get(v.getCliente())+preco);
            }
            else{
                hashQ.put(v.getCliente(), quantidade);
                hashP.put(v.getCliente(), preco);
            }
        }
        for(Venda v : vendas3){
            quantidade = v.getQuant();
            preco = v.getPreco();
            if(hashQ.containsKey(v.getCliente())){
                hashQ.put(v.getCliente(), hashQ.get(v.getCliente())+quantidade);
                hashP.put(v.getCliente(), hashQ.get(v.getCliente())+preco);
            }
            else{
                hashQ.put(v.getCliente(), quantidade);
                hashP.put(v.getCliente(), preco);
            }
        }
        Map<Cliente, Integer> sortedMap = 
                        hashQ.entrySet().stream()
                        .sorted(Map.Entry.<Cliente, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        List<Cliente> clientes = new ArrayList<>(sortedMap.keySet());
        double timef=Crono.stop();
        System.out.println("Tempo de execução Query 12 " + timef+"s");
        return clientes;
    }
    
    /**
     * Função que percorre a lista de clientes e para cada um usa a função produtosDiferentes para calcular
     * quantos produtos diferentes este comprou. Dado isto preenche uma List de pares, cliente e integer, onde cada par 
     * representa o cliente e a quantidade de produtos diferentes que ele comprou. Ordena esta lista de pares de forma descendente
     * @param sgv sistema de gestao de vendas
     * @return Lista de Pair que contem as quantidades de produtos diferentes que cada cliente comprou.
     */
    public List<Pair<Cliente, Integer>> clientesMaisDiversos(SGV sgv){
        Crono.start();
        Cliente c;
        int valor;
        List<Pair<Cliente, Integer>> pairs = new ArrayList<>();
        Set<Cliente> clientes = sgv.getCatClientes().getCatClientes();
        Iterator<Cliente> it = clientes.iterator();
        while(it.hasNext()){
            c = it.next();
            valor = produtosDiferentes(sgv, c);
            Pair<Cliente, Integer> pair = new Pair(c, valor);
            pairs.add(pair);
        }
        pairs.sort(Comparator.comparing((Pair<Cliente, Integer> p) -> p.getValue()).reversed());
        double timef=Crono.stop();
        System.out.println("Tempo de execução Query 11 " + timef+"s");
        return pairs;
    }
    
    /**
     * Função que dado um cliente adiciona todos os produtos que este comprou a um set e depois retorna o seu tamanho
     * @param sgv sistema de gestao de vendas
     * @param c Indicador do cliente
     * @return Inteiro que indica o numero de produtos comprados por um cliente
     */
    public int produtosDiferentes(SGV sgv, Cliente c){
        AbstractCollection<Object> produtos = new ArrayList<Object>(); 
        List<Venda> vendas1 = sgv.getFiliais().getFilial(1).getListVendasC(c);
        List<Venda> vendas2 = sgv.getFiliais().getFilial(2).getListVendasC(c);
        List<Venda> vendas3 = sgv.getFiliais().getFilial(3).getListVendasC(c);
        for(Venda v : vendas1){
            produtos.add(v.getCliente());
        }
        for(Venda v : vendas2){
            produtos.add(v.getCliente());
        }
        for(Venda v : vendas3){
            produtos.add(v.getCliente());
        }
        return produtos.size();
    }
    
    /**
     * Função que percorre o TreeSet da faturação e dependendo da filial e do mes da fatura preenche um ArrayList,
     * somando os valores das vendas
     * @param sgv sistema de gestao de vendas
     * @return ArrayList que contem os valores faturados mensais ordenados por filial.
     */
    public ArrayList<Double> getFatTotalMes(SGV sgv){
        Crono.start();
        ArrayList<Double> total = new ArrayList<>(36);
        for(int i=0; i<36;i++){
            total.add(0.0);
        }
        int mes;
        double preco;
        int filial;
        
        Iterator<Fatura> it = sgv.getFaturacao().getFaturas().iterator();
        while(it.hasNext()){
            Fatura f = it.next();
            mes = f.getMes();
            preco = f.getPreco();
            filial = f.getFilial();
            if(filial == 1){
                total.add(mes-1, preco+total.get(mes-1));
            }else if(filial == 2){
                total.add(mes+11, preco+total.get(mes+11));
            }else if(filial == 3){
                total.add(mes+23, preco+total.get(mes+23));
            }
        }
        double timef=Crono.stop();
        System.out.println("Tempo de execução Query 13 " + timef+"s");
        return total;
    }
    
    
}

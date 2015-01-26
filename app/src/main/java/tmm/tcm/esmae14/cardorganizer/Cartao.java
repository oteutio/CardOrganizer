package tmm.tcm.esmae14.cardorganizer;

/**
 * Created by Nuno on 26/01/2015.
 */
import java.util.ArrayList;


public class Cartao {
    private String nomeCartao;
    private String numero;
    private ArrayList<String> categorias;

    public Cartao(String nomeCartao, String numero, ArrayList<String> categorias){

        setNomeCartao(nomeCartao);
        setNumero(numero);
        categorias=new ArrayList<String> ();


    }
    public Cartao(){

        nomeCartao="";
        numero="";
        categorias=new ArrayList<String> ();
    }

    public void setNomeCartao(String nomeCartao){

        this.nomeCartao=nomeCartao;

    }

    public void setNumero(String numero){

        this.numero=numero;
    }

    public void addCategoria(String categoria){

        categorias.add(categoria);

    }

    public String getNomeCartao(){return nomeCartao;}


    public String getNumero(){return numero;}

    public ArrayList<String> getCategorias(){return categorias;}

    public String toString(){

        String s1="";

        for(int i=0;i<categorias.size();i++){

            s1=s1+ " "+(categorias.get(i));

        }

        return String.format("Nome do Cartão:%s %nNúmero: %s %n",getNomeCartao(),getNumero(),s1);
    }

}

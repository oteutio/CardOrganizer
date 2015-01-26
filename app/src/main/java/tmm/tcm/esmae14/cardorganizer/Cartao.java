package tmm.tcm.esmae14.cardorganizer;

/**
 * Created by Nuno on 26/01/2015.
 */
import java.util.ArrayList;


public class Cartao {
    private String nomeCartao;
    private String numero;
    private String tipo;
    private ArrayList<String> categorias;

    public Cartao(String nomeCartao, String numero, String tipo, ArrayList<String> categorias){

        setNomeCartao(nomeCartao);
        setNumero(numero);
        setTipo(tipo);
        categorias=new ArrayList<String> ();


    }
    public Cartao(){

        nomeCartao="";
        numero="";
        tipo="";
        categorias=new ArrayList<String> ();
    }

    public void setNomeCartao(String nomeCartao){

        this.nomeCartao=nomeCartao;

    }

    public void setNumero(String numero){

        this.numero=numero;
    }

    public void setTipo(String tipo){
        this.tipo=tipo;
    }

    public void addCategoria(String categoria){

        categorias.add(categoria);

    }

    public String getNomeCartao(){return nomeCartao;}


    public String getTipo(){return tipo;}

    public String getNumero(){return numero;}

    public ArrayList<String> getCategorias(){return categorias;}

    public String toString(){

        String s1="";

        for(int i=0;i<categorias.size();i++){

            s1=s1+ " "+(categorias.get(i));

        }

        return String.format("Nome do Cartão:%s %nNúmero: %s %nTipo: %s %n",getNomeCartao(),getNumero(),getTipo(),s1);
    }

}

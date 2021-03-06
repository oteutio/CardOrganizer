package tmm.tcm.esmae14.cardorganizer;

/**
 * Created by Nuno on 26/01/2015.
 */
import java.util.ArrayList;


public class Cartao {
    private int id;
    private String nomeCartao;
    private String numero;
    private String formato;
    private String tipo;
   // private ArrayList<String> categorias;

    public Cartao(int id, String nomeCartao, String numero, String formato, String tipo/*, ArrayList<String> categorias*/){
        setId(id);
        setNomeCartao(nomeCartao);
        setNumero(numero);
        setFormato(formato);
        setTipo(tipo);
       // categorias=new ArrayList<String> ();


    }
    public Cartao(){
        id=0;
        nomeCartao="";
        numero="";
        formato="";
        tipo="";
       // categorias=new ArrayList<String> ();
    }

    public void setNomeCartao(String nomeCartao){

        this.nomeCartao=nomeCartao;

    }

    public void setId(int id){

        this.id=id;

    }
    public void setNumero(String numero){

        this.numero=numero;
    }

    public void setFormato(String formato){

        this.formato=formato;

    }

    public void setTipo(String tipo){

        this.tipo=tipo;

    }

   /* public void addCategoria(String categoria){

        categorias.add(categoria);

    }*/

    public String getNomeCartao(){return nomeCartao;}

    public int getId(){return id;}

    public String getFormato(){return formato;}

    public String getTipo(){return formato;}

    public String getNumero(){return numero;}

   // public ArrayList<String> getCategorias(){return categorias;}

    public String toString(){

        /*String s1="";

        for(int i=0;i<categorias.size();i++){

            s1=s1+ " "+(categorias.get(i));

        }*/

        return String.format("Nome do Cartão: %s %nNúmero: %s %nFormato: %s %nTipo: %s %n",getNomeCartao(),getNumero(),getFormato(),getTipo()/*,s1*/);
    }



}

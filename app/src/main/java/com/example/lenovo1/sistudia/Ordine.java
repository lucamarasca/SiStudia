package com.example.lenovo1.sistudia;

import org.json.JSONObject;

import java.util.List;

public class Ordine {
    String id = null;
    String nome_alunno = null;
    String stato_ordine = null;
    String civico = null;
    String indirizzo = null;
    String provincia = null;
    String nominativo_libraio = null;
    String comune = null;
    String data = null;
    int id_stato_ordine ;
    static List<Libro> libri = null;

    // Costruttore ordine da stringa formattata in json
    public Ordine(String JSonobj)throws Exception {
        // Estraggo l'ordine
        JSONObject jobj = new JSONObject(JSonobj);
        this.id = jobj.getString("id_ordine");
        this.nominativo_libraio = jobj.getString("nominativo");
        this.comune = jobj.getString("comune");
        this.provincia = jobj.getString("provincia");
        this.indirizzo = jobj.getString("indirizzo");
        this.civico = jobj.getString("civico");
        this.stato_ordine = jobj.getString("stato_ordine");
        this.nome_alunno = jobj.getString("nominativo_alunno");
        this.data = jobj.getString("data");
        this.id_stato_ordine = jobj.getInt("id_stato_ordine");

    }


    public String getId() {
        return id;
    }



    public String getNome_alunno() {
        return nome_alunno;
    }



    public String getStato_ordine() {
        return stato_ordine;
    }



    public  String getCivico() {
        return civico;
    }


    public int getId_stato_ordine() { return id_stato_ordine; }
    public  String getIndirizzo() {
        return indirizzo;
    }



    public  String getProvincia() {
        return provincia;
    }



    public  String getNominativo_libraio() {
        return nominativo_libraio;
    }



    public String getComune() {
        return comune;
    }



    public String getData() {
        return data;
    }


}

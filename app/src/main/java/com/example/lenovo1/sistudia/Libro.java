package com.example.lenovo1.sistudia;

import org.json.JSONObject;

public class Libro {
    String id = null;
    String titolo = null;
    String autore = null;
    String casa_editrice = null;
    String codice_isbn = null;
    String materia = null;
    int id_stato_libro ;

    // Costruttore ordine da stringa formattata in json
    public Libro(String JSonobj)throws Exception {

        // Estraggo l'ordine
        JSONObject jobj = new JSONObject(JSonobj);
        this.id = jobj.getString("id_libro");
        this.titolo = jobj.getString("titolo");
        this.autore = jobj.getString("autore");
        this.casa_editrice = jobj.getString("casa_editrice");
        this.codice_isbn = jobj.getString("codice_isbn");
        this.materia = jobj.getString("materia");

    }

    public String getTitolo() {
        return titolo;
    }

    public String getId() {
        return id;
    }

    public String getAutore() {
        return autore;
    }

    public String getCasa_editrice() {
        return casa_editrice;
    }

    public String getCodice_isbn() {
        return codice_isbn;
    }

    public String getMateria() {
        return materia;
    }

    public int getId_stato_libro() {
        return id_stato_libro;
    }
}

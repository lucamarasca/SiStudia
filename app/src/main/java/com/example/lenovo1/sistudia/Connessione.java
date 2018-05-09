package com.example.lenovo1.sistudia;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connessione extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String ... url) {
    // Creao l'oggetto URL che rappresenta l'indirizzo della pagina da richiamare
    URL paginaURL = null;
    try {
        paginaURL = new URL(url[0]);
        // creo l'oggetto HttpURLConnection e apro la connessione al server
        HttpURLConnection client = (HttpURLConnection) paginaURL.openConnection();
    } catch (Exception e) {
        e.printStackTrace();
    }


    return "Connessione al server effettuata";
    }
    @Override
    protected void onPostExecute(String result) {
        // Recupero le informazioni inviate dal server
        InputStream risposta = new BufferedInputStream(client.getInputStream());
    }
}
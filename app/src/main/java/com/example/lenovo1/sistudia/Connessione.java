package com.example.lenovo1.sistudia;

import android.os.AsyncTask;
import org.json.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Connessione extends AsyncTask<String, String, String> {
    private JSONObject postData;
    private String requestType;
    private String responseInfo;


    List<ConnessioneListener> listeners;

    // Costruttore che prende i dati e li mette in un arraylist (In questo modo posso passare un jsonObject formato da quanti campi voglio e non devo fare dumila metodi)
    public Connessione(JSONObject postData, String requestType) {
        this.requestType = requestType;
        this.listeners = new ArrayList<>();
        if (postData != null) {
            this.postData = postData;
        }
    }

    // Metodo che starta un task asincrono e che avvia la connessione con il server (viene invocato tramite "execute()")
    @Override
    protected String doInBackground(String... params) {
        try {
            // in params[0] c'è l'indirizzo della pagina php
            URL url = new URL(params[0]);

            // Creo la Url Connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json; charset= utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod(requestType);
            urlConnection.setConnectTimeout(10000);

            // Invio il JsonObject al server
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            InputStreamReader inputStreamReader;

            // Se lo statuscode è 200 leggo la risposta, altrimenti leggo l'errore
            if (statusCode == 200)
                inputStreamReader = new InputStreamReader (urlConnection.getInputStream());
            else    // Leggo dall' ErrorStream
                inputStreamReader = new InputStreamReader (urlConnection.getErrorStream());

            // Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String response;



            while ((response = reader.readLine()) != null)
                sb.append(response).append("\n");

            response = sb.toString();
            responseInfo = response;

            //publishProgress(response, String.valueOf(statusCode));

            return String.valueOf(statusCode);
        } catch (Exception e) {
            responseInfo = e.getMessage();
        }

        return null;
    }

    public void addListener(ConnessioneListener listener) {
        listeners.add(listener);
    }
    //Metodo che viene chiamato DOPO aver ricevuto la risposta dal server
    //Ad ogni listner invia la risposta.
    @Override
    protected void onPostExecute(String result){ // Result è lo status code di ritorno
        for (ConnessioneListener listener: listeners)
            listener.ResultResponse(result, responseInfo);
    }

    /**
     *  Utilizzare solo in caso di dover informare l'utente durante la trasmissione o elaborazione dei dati,
     *  questo significa che l'utente va avvisato di un eventuale risposta solo alla fine della trasmissione
     *  la suddetta risposta deve essere inviata tramite onPostExecute e non qui.
     */
    @Override
    protected void onProgressUpdate(String... progress) {

    }

    // Funzione per l'estrazione automatica del dato JSon error
    public static String estraiErrore(String data){
        String result;

        try {
            JSONObject response = new JSONObject(data);
            JSONObject error = new JSONObject(response.getString("error"));
            result = error.getString("info");
        } catch (Exception e) {
            result = "Impossibile leggere la risposta del server.";
        }

        return result;
    }

    // Funzione per l'estrazione automatica del dato JSon successful
    public static String estraiSuccessful(String data){
        String result;

        try {
            JSONObject response = new JSONObject(data);
            JSONObject error = new JSONObject(response.getString("successful"));
            result = error.getString("info");
        } catch (Exception e) {
            result = "Impossibile leggere la risposta del server.";
        }

        return result;
    }
}
/*
    <?php
            $json = file_get_contents('php://input');	//Prende i dati passati com JSONObject
            $obj = json_decode($json);	//Trasforma oggetto json in array
            echo $json;
    ?> */
package com.example.lenovo1.sistudia;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.in;

public class SistudiaLoginActivity extends AppCompatActivity implements  ConnessioneListener {

    private ProgressDialog caricamento = null;  //Progress dialog di caricamento


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setto le notifiche in background
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        startService(new Intent(this,Background.class));



        setContentView(R.layout.activity_sistudia_login);
        Button btnlogin = (Button) findViewById(R.id.btnLogin); //Prendo l'oggetto bottone login
        Button btn = (Button) findViewById(R.id.btnRecuperaPassword); //Prendo l'oggetto bottone recupa password
        Button btnBackdoor = (Button) findViewById(R.id.backdoor);
        //Se clicclo il bottone "Login", chiamo la funzione Login()
        btnBackdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parametri.id = "2";
                Backdoor();
            }
        });

        //Se clicclo il bottone "Login", chiamo la funzione Login()
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //Se clicco sul bottone "Recupera Password", chiamo la funzione per recuperare la password
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperaPassword();
            }
        });

    }

    //Metodo che prende i dati delle textview le elabora per poi chiamare il metodo che invia i dati al server
    public void Login()
    {

        TextView user = (TextView) findViewById(R.id.etUsername);
        TextView pswd = (TextView) findViewById(R.id.password);
        String username = user.getText().toString();
        String password = pswd.getText().toString();
        //Cripta la password in md5
        password = md5(password);
        //Metodo che invia i dati di login appena presi al server
        sendDataForLogin(username, password);

    }

    //Funzione che permette all' utente di recuperare la password
    public void RecuperaPassword()
    {

    }

    //Crea un oggetto JSON con username e password e instanzia un oggetto di tipo connessione per comunicare con il server
    //Aggiunge aggiunge un listner e avvia la connessione
    private void sendDataForLogin(String username, String password) {
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(SistudiaLoginActivity.this, "Login in corso",
                "Connessione con il server in corso...", true);
        caricamento.show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (Exception e) {
            caricamento.dismiss();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        //In Parametri.IP c'è la path a cui va aggiunta il nome della pagina.php
        conn.execute(Parametri.IP + "/SistudiaLoginAndroid.php");
    }

    //Metodo che cripta in MD5
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    //Metodo in cui è contenuta la risposta del server
    @Override
    public void ResultResponse(String responseCode, String result) {
        String message;
        //Se L'utente non viene trova all' interno del DB
        if (result == null || result.equals("null") || result.equals("\n\n\n"))
        {
            message = "Utente non trovato.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            caricamento.dismiss();
            return;
        }
        // Estraggo i miei dati restituiti dal server
        try {
            JSONObject utente = new JSONObject(result);
            Parametri.id = utente.getString("id");
            Parametri.nome = utente.getString("nome");
            Parametri.cognome = utente.getString("cognome");
            Parametri.username = utente.getString("user");
            Parametri.password = utente.getString("password");
            Parametri.email = utente.getString("mail");
            Parametri.telefono = utente.getString("cellulare");

            message = "Benvenuto " + Parametri.nome + ".";


        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), SistudiaLoginActivity.class));
            return;
        }

        caricamento.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void Backdoor ()
    {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}

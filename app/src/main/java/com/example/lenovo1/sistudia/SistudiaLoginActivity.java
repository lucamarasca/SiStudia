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
        setContentView(R.layout.activity_sistudia_login);
        Button btnlogin = (Button) findViewById(R.id.btnLogin); //Prendo l'oggetto bottone login
        Button btn = (Button) findViewById(R.id.btnRecuperaPassword); //Prendo l'oggetto bottone recupa password
        Button btnBackdoor = (Button) findViewById(R.id.backdoor);
        //Se clicclo il bottone "Login", chiamo la funzione Login()
        btnBackdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
       /* try {
            password = SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Riscontrati problemi nell' hashing della password.", Toast.LENGTH_LONG).show();
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Riscontrati problemi nell' hashing della password.", Toast.LENGTH_LONG).show();
            return;
        } */

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
        conn.execute(Parametri.IP + "/login");
    }

    // Criptazione SHA1
    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    //Converte in esadecimale (usato per la conversione SHA1)
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
    //Metodo in cui è contenuta la risposta del server
    @Override
    public void ResultResponse(String responseCode, String result) {
        String message;
        // Estraggo i miei dati restituiti dal server
        try {
            JSONObject utente = new JSONObject(result);



            Parametri.id = utente.getString("id");
            Parametri.nome = utente.getString("nome");
            Parametri.cognome = utente.getString("cognome");
            Parametri.username = utente.getString("username");
            Parametri.password = utente.getString("password");
            Parametri.email = utente.getString("mail");

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

package com.example.lenovo1.sistudia;

import android.annotation.TargetApi;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

public class SistudiaLoginActivity extends AppCompatActivity {
    private AutoCompleteTextView etUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistudia_login);
        Button btnlogin = (Button) findViewById(R.id.btnLogin); //Prendo l'oggetto bottone login
        Button btn = (Button) findViewById(R.id.btnRecuperaPassword); //Prendo l'oggetto bottone recupa password
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
                int x =0;
                RecuperaPassword();
            }
        });
    }
    //Funzione che fa loggare l'utente
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Login()
    {

        TextView user = (TextView) findViewById(R.id.etUsername);
        TextView pswd = (TextView) findViewById(R.id.password);
        String username = user.getText().toString();
        String password = pswd.getText().toString();

        new Connessione().execute("http://192.168.1.129/iccs/sistudia_android/SistudiaLoginAndroid.php");



    }

    //Funzione che permette all' utente di recuperare la password
    public void RecuperaPassword()
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String mostroDati(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

package com.example.lenovo1.sistudia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    public void Login()
    {

        TextView user = (TextView) findViewById(R.id.etUsername);
        TextView pswd = (TextView) findViewById(R.id.password);
        String username = user.getText().toString();
        String password = pswd.getText().toString();
        

    }
    //Funzione che permette all' utente di recuperare la password
    public void RecuperaPassword()
    {

    }
}

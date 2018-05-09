package com.example.lenovo1.sistudia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ordini = (Button) findViewById(R.id.btn_ordini); //Prendo l'oggetto bottone login
        Button btn_ritiro_libri = (Button) findViewById(R.id.btn_ritiro_libri); //Prendo l'oggetto bottone recupa password

        btn_ordini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisualizzaOrdini();
            }
        });
        //Se clicco sul bottone "Recupera Password", chiamo la funzione per recuperare la password
        btn_ritiro_libri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               RitiroLibri();
            }
        });
    }
    public void VisualizzaOrdini()
    {
        // definisco l'intenzione di aprire l'Activity "Page1.java"
        Intent openPage1 = new Intent(MainActivity.this,MieiOrdini.class);
        // passo all'attivazione dell'activity page1.java
        startActivity(openPage1);
    }
    public void RitiroLibri()
    {
        // definisco l'intenzione di aprire l'Activity "Page1.java"
        Intent openPage1 = new Intent(MainActivity.this,RitiroLibri.class);
        // passo all'attivazione dell'activity page1.java
        startActivity(openPage1);
    }
}

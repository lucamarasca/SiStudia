package com.example.lenovo1.sistudia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout dl ;
    private ActionBarDrawerToggle action_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.Drawer);
        action_bar = new ActionBarDrawerToggle(this, dl,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        dl.addDrawerListener(action_bar);
        action_bar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (action_bar.onOptionsItemSelected(item))
        {
            return true;
        }
        if (id == R.id.nav_impostazioni) {
            Toast.makeText(MainActivity.this, "Hai cliccato su impostazioni!",
                    Toast.LENGTH_LONG).show();
            //setTitle("Il tuo profilo");
            //ProfileFragment fragment = new ProfileFragment();
            //fragmentTransaction.replace(R.id.fram, fragment, "Fragment Profile");
            //fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

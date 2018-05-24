package com.example.lenovo1.sistudia;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConnessioneListener {
    //Variabile del tipo di layout dell' activity
    private DrawerLayout dl;
    //variabile che rappresenta l'actionbar (la barra in altro)
    private ActionBarDrawerToggle action_bar;
    //variabile che server per gestire i fragment
    FragmentTransaction fragmentTransaction;
    //La navigation view è il menu di sinistra
    NavigationView navigationView;
    //Header del navigation view
    View header ;
    private ProgressDialog caricamento = null;  //Progress dialog di caricamento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("test");
        //Prendo il token associato al dispositivo e aggiungo l'id utente al DB (Mi server per le notifiche)
        Parametri.Token = FirebaseInstanceId.getInstance().getToken();
        RegistraUtente(Parametri.Token);

        //Avvio i servizi della app in background; in questo modo sono in grado di ricevere le notifiche anche se l'applicazione viene rimossa dallo stack.
        startService(new Intent(this,Background.class));


        //dichiaro l'actionbar
        dl = (DrawerLayout) findViewById(R.id.Drawer);
        action_bar = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(action_bar);
        action_bar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Parametri.notifica == false) {
            //Apro il fragment Home
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_conteiner, new SistudiaMainFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Home");
        }else
        {
            //Apro il fragment Dei miei ordini
           // getOrdini();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_conteiner, new SistudiaFragmentMieiOrdini());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("I miei Ordini");
            Parametri.notifica = false;
        }

        //Inizializzo la variabile del navigationview
        navigationView = findViewById(R.id.navigation_view);

        //setto il testo dello header del navigationview
        header = navigationView.getHeaderView(0);
        TextView nome_utente = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email);
        TextView cellulare = (TextView) header.findViewById(R.id.cellulare);

        nome_utente.setText("Username: " + Parametri.username);
        email.setText("e-mail: " + Parametri.email);
        cellulare.setText("cellulare: " + Parametri.telefono);
        //Definisco cosa deve fare la app nel caso in cui clicchi una delle voci del menu di sinistra
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home: {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaMainFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setChecked(true);
                        dl.closeDrawers();
                        break;
                    }
                    case R.id.nav_impostazioni: {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentImpostazioni());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Impostazioni");
                        item.setChecked(true);
                        dl.closeDrawers();
                        break;
                    }
                    case R.id.nav_informazioni: {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentInformazioni());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("About Us");
                        item.setChecked(true);
                        dl.closeDrawers();
                        break;
                    }
                    case R.id.nav_logout: {
                        // definisco l'intenzione di aprire l'Activity "Page1.java"
                        Intent activity = new Intent(MainActivity.this, SistudiaLoginActivity.class);
                        startActivity(activity);
                        dl.closeDrawers();
                        MainActivity.this.finish();
                        break;
                    }
                }
                return true;
            }
        });
    }
    //Metodo che viene utilizzato per aprire il menu di sinistra
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (action_bar.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.Drawer);
        // Se il menù è aperto lo chiudo
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            boolean handled = false;

            for (Fragment f : fragmentList)
                if (f instanceof FragmentWithOnBack) {
                    handled = ((FragmentWithOnBack) f).onBackPressed();

                    if (handled)
                        break;
                }

            if (!handled)
                super.onBackPressed();
        }
    }
    public void RegistraUtente(String token)
    {
        JSONObject postData = new JSONObject();
        try {
            postData.put("Token", token);
            postData.put("id_utente",Parametri.id);

        } catch (Exception e) {
            caricamento.dismiss();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        //In Parametri.IP c'è la path a cui va aggiunta il nome della pagina.php
        conn.execute(Parametri.IP + "/assegna_utente_notifiche.php");
    }


    //Metodo in cui è contenuta la risposta del server
    @Override
    public void ResultResponse(String responseCode, String result) {
            Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
    }
}

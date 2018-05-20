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

        //Setto le notifiche in background
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
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
            getOrdini();
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

    public void getOrdini(){
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(this, "Cerco i tuoi ordini!",
                "Connessione con il server in corso...", true);
        caricamento.show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("id_utente", Parametri.id);

        } catch (Exception e) {
            caricamento.dismiss();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        //In Parametri.IP c'è la path a cui va aggiunta il nome della pagina.php
        conn.execute(Parametri.IP + "/SistudiaMieiOrdiniAndroid.php");

    }

    //Metodo in cui è contenuta la risposta del server
    @Override
    public void ResultResponse(String responseCode, String result) {
        String message;

        //Se L'utente non viene trova all' interno del DB
        if (result == null || result.equals("null") || result.equals("\n\n\n"))
        {
            message = "Nessun Ordine.";
            caricamento.dismiss();
            return;
        }
        ArrayList<Ordine> par = new ArrayList<>();
        // Estraggo i miei dati restituiti dal server
        try {

            JSONObject jsonconvert = new JSONObject(result);
            JSONArray ordini = jsonconvert.getJSONArray("ordini");
            Parametri.ordinieffettutati = new ArrayList<>();

            for (int i = 0; i < ordini.length(); i++)
            {
                par.add(new Ordine(ordini.getJSONObject(i).toString()));
            }

        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        Parametri.ordinieffettutati = par;
        caricamento.dismiss();


    }
}

/* DATABASE
CREATE DATABASE FCM;

USE FCM;

CREATE TABLE users(
    id int (20) NOT NULL AUTO_INCREMENT,
    Token varchar(200) NOT NULL,

    PRIMARY KEY(id),
    UNIQUE KEY(Token));


MIEI ORDINI



<?php

$count = 0;


$arr = array();
$arr["ordini"] = array();
$arr["ordini"] [$count] = array();
$arr["ordini"] [$count]["id_ordine"] = "1";
$arr["ordini"] [$count]["nominativo"] = "idbelloaaaa";
$arr["ordini"] [$count]["comune"] = "aaaaaaaa";
$arr["ordini"] [$count]["provincia"] = "aaaaaaaa";
$arr["ordini"] [$count]["indirizzo"] = "aaaaaaaaa";
$arr["ordini"] [$count]["civico"] = "aaaaaaaa";
$arr["ordini"] [$count]["stato_ordine"] = "aaaaaa";
$arr["ordini"] [$count]["nominativo_alunno"] = "aaaaaaaaaa";
$arr["ordini"] [$count]["data"] = "aaaaaaaaaa";
$arr["ordini"] [$count]["id_stato_ordine"] = 1;

$count++;
$arr["ordini"] [$count]["id_ordine"] = "2";
$arr["ordini"] [$count]["nominativo"] = "idbello";
$arr["ordini"] [$count]["comune"] = "idbello";
$arr["ordini"] [$count]["provincia"] = "idbello";
$arr["ordini"] [$count]["indirizzo"] = "idbello";
$arr["ordini"] [$count]["civico"] = "idbello";
$arr["ordini"] [$count]["stato_ordine"] = "idbello";
$arr["ordini"] [$count]["nominativo_alunno"] = "idbello";
$arr["ordini"] [$count]["data"] = "idbello";
$arr["ordini"] [$count]["id_stato_ordine"] = 3;

$count++;
$arr["ordini"] [$count]["id_ordine"] = "3";
$arr["ordini"] [$count]["nominativo"] = "idbello";
$arr["ordini"] [$count]["comune"] = "idbello";
$arr["ordini"] [$count]["provincia"] = "idbello";
$arr["ordini"] [$count]["indirizzo"] = "idbello";
$arr["ordini"] [$count]["civico"] = "idbello";
$arr["ordini"] [$count]["stato_ordine"] = "idbello";
$arr["ordini"] [$count]["nominativo_alunno"] = "idbello";
$arr["ordini"] [$count]["data"] = "idbello";
$arr["ordini"] [$count]["id_stato_ordine"] = 2;

$count++;
$arr["ordini"] [$count]["id_ordine"] = "4";
$arr["ordini"] [$count]["nominativo"] = "nome_libraio";
$arr["ordini"] [$count]["comune"] = "idbello";
$arr["ordini"] [$count]["provincia"] = "idbello";
$arr["ordini"] [$count]["indirizzo"] = "idbello";
$arr["ordini"] [$count]["civico"] = "idbello";
$arr["ordini"] [$count]["stato_ordine"] = "idbello";
$arr["ordini"] [$count]["nominativo_alunno"] = "idbello";
$arr["ordini"] [$count]["data"] = "idbello";
$arr["ordini"] [$count]["id_stato_ordine"] = 4;

$count++;
$arr["ordini"] [$count]["id_ordine"] = "5";
$arr["ordini"] [$count]["nominativo"] = "idbello";
$arr["ordini"] [$count]["comune"] = "idbello";
$arr["ordini"] [$count]["provincia"] = "idbello";
$arr["ordini"] [$count]["indirizzo"] = "idbello";
$arr["ordini"] [$count]["civico"] = "idbello";
$arr["ordini"] [$count]["stato_ordine"] = "idbello";
$arr["ordini"] [$count]["nominativo_alunno"] = "idbello";
$arr["ordini"] [$count]["data"] = "idbello";
$arr["ordini"] [$count]["id_stato_ordine"] = 2;

$count++;
$arr["ordini"] [$count]["id_ordine"] = "6";
$arr["ordini"] [$count]["nominativo"] = "idbello";
$arr["ordini"] [$count]["comune"] = "idbello";
$arr["ordini"] [$count]["provincia"] = "idbello";
$arr["ordini"] [$count]["indirizzo"] = "idbello";
$arr["ordini"] [$count]["civico"] = "idbello";
$arr["ordini"] [$count]["stato_ordine"] = "idbello";
$arr["ordini"] [$count]["nominativo_alunno"] = "idbello";
$arr["ordini"] [$count]["data"] = "idbello";
$arr["ordini"] [$count]["id_stato_ordine"] = 3;

echo json_encode($arr);

// @$ordini->ordine->$count->id_ordine = "idbello";
// @$ordini->ordine->$count->nominativo = "Nominativo_Libraio";
// @$ordini->ordine->$count->comune = "comune_libraio";
// @$ordini->ordine->$count->provincia = "provincia_libraio";
// @$ordini->ordine->$count->indirizzo = "indirizzo_libraio";
// @$ordini->ordine->$count->civico = "civico_libraio";
// @$ordini->ordine->$count->stato_ordine = "stato ordine";
// @$ordini->ordine->$count->nominativo_alunno = "idbello";
// @$ordini->ordine->$count->data = "data";



// echo json_encode($ordini);


?>
 */
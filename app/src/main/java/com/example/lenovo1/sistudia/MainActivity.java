package com.example.lenovo1.sistudia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dichiaro l'actionbar
        dl = (DrawerLayout) findViewById(R.id.Drawer);
        action_bar = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(action_bar);
        action_bar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Apro il fragment Home
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_conteiner, new SistudiaMainFragment());
        fragmentTransaction.commit();
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
                        getSupportActionBar().setTitle("About Us");
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



}
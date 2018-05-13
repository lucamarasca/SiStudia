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
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout dl ;
    private ActionBarDrawerToggle action_bar;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.Drawer);
        action_bar = new ActionBarDrawerToggle(this, dl,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        dl.addDrawerListener(action_bar);
        action_bar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_conteiner,new SistudiaMainFragment());
        fragmentTransaction.commit();
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId())
               {
                   case R.id.nav_home:
                   {
                       fragmentTransaction = getSupportFragmentManager().beginTransaction();
                       fragmentTransaction.replace(R.id.main_conteiner, new SistudiaMainFragment());
                       fragmentTransaction.commit();
                       getSupportActionBar().setTitle("Home");
                       item.setChecked(true);
                       dl.closeDrawers();
                       break;
                   }
                   case R.id.nav_impostazioni:
                   {
                       fragmentTransaction = getSupportFragmentManager().beginTransaction();
                       fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentImpostazioni());
                       fragmentTransaction.commit();
                       getSupportActionBar().setTitle("About Us");
                       item.setChecked(true);
                       dl.closeDrawers();
                       break;
                   }
                   case R.id.nav_informazioni:
                   {
                       fragmentTransaction = getSupportFragmentManager().beginTransaction();
                       fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentInformazioni());
                       fragmentTransaction.commit();
                       getSupportActionBar().setTitle("About Us");
                       item.setChecked(true);
                       dl.closeDrawers();
                       break;
                   }
                   case R.id.nav_logout:
                   {
                       // definisco l'intenzione di aprire l'Activity "Page1.java"
                       Intent activity = new Intent(MainActivity.this,SistudiaLoginActivity.class);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
/*
<?php
$json = file_get_contents('php://input');	//Prende i dati passati com JSONObject
$obj = json_decode($json);	//Trasforma oggetto json in array
$username = $obj["username"];
$password = $obj["password"];
echo $json;
?>
 */
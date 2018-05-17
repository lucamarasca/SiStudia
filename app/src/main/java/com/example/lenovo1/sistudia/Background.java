package com.example.lenovo1.sistudia;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class Background extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        // Visualizzo un Toast su schermo per avvisare l'utente dell'avvenuta
        // creazione del servizio
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG)
                .show();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        // Visualizzo un Toast su schermo per avvisare l'utente dell'avvenuta
        // inizializzazione del servizio.
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
}

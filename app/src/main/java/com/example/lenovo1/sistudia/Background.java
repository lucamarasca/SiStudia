package com.example.lenovo1.sistudia;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class Background extends Service {
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //Prendo i dati salvati dall' utente in base alle sue scelte (Memorizza credenziali, impostazioni ecc...)
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Parametri.notifica = sharedPreferences.getBoolean("NOTIFICA", true);
        Intent i = new Intent(getApplicationContext(), FirebaseMessagingService.class);
        i.setAction("start");
        startService(i);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Intent i = new Intent(getApplicationContext(), SistudiaLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }
}

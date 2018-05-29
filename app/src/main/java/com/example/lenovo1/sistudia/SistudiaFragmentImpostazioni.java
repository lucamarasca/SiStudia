package com.example.lenovo1.sistudia;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentImpostazioni extends FragmentWithOnBack {


    public SistudiaFragmentImpostazioni() {
        // Required empty public constructor
    }

    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_impostazioni, container, false);
        Switch memorizza_credenziali = (Switch) view.findViewById(R.id.stay_logged);
        Switch notifica = (Switch) view.findViewById(R.id.notifiche);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        //Carico i dati salvati dell' utente
        if(sharedPreferences.getBoolean("SAVE_CREDENTIALS", false))
            memorizza_credenziali.setChecked(true);
        else
            memorizza_credenziali.setChecked(false);
        if (Parametri.notifica)
            notifica.setChecked(true);
        else
            notifica.setChecked(false);
        memorizza_credenziali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putBoolean("SAVE_CREDENTIALS", isChecked);
                   editor.putString("USERNAME", Parametri.username);
                   editor.putString("PASSWORD", Parametri.password);
                   editor.apply();
               }
           }
        );
        notifica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                     if (isChecked)
                        Parametri.notifica = true;
                     else
                         Parametri.notifica = false;
                     SharedPreferences.Editor editor = sharedPreferences.edit();
                     editor.putBoolean("NOTIFICA", isChecked);
                     editor.apply();
                 }
             }
        );
        return view;
    }
    @Override
    public boolean onBackPressed() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        SistudiaMainFragment fragment = new SistudiaMainFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, fragment, "Fragment Find Park");
        fragmentTransaction.commit();
        return true;
    }

}

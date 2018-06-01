package com.example.lenovo1.sistudia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentRitiroLibri extends FragmentWithOnBack implements  ConnessioneListener  {

    private TextView codice_ritiro;
    public SistudiaFragmentRitiroLibri() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_ritiro_libri, container, false);
        codice_ritiro = view.findViewById(R.id.codice_ritiro);
        String codice = GenerateRandomString.randomString(5);
        codice_ritiro.setText("Codice Ritiro: "+codice);
        AggiornaCodice(codice);
        Button btn_rigenera = view.findViewById(R.id.btn_rigenera);
        btn_rigenera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String codice = GenerateRandomString.randomString(5);
                codice_ritiro.setText("Codice Ritiro: "+codice);
                AggiornaCodice(codice);
            }
        });

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

    public void AggiornaCodice(String codice)
    {
        JSONObject postData = new JSONObject();
        try {
            postData.put("id_utente", Parametri.id);
            postData.put("codice", codice);

        } catch (Exception e) {

            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        //In Parametri.IP c'è la path a cui va aggiunta il nome della pagina.php
        conn.execute(Parametri.IP + "/SistudiaConfermaCodice.php");
    }

    @Override
    public void ResultResponse(String responseCode, String result) {
        String resultt = result;
        String response = responseCode;
    }
}

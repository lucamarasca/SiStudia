package com.example.lenovo1.sistudia;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaMainFragment extends FragmentWithOnBack implements ConnessioneListener {

    private ProgressDialog caricamento = null;  //Progress dialog di caricamento
    public SistudiaMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sistudia_main, container, false);
        //imposto i listner sui bottoni
        Button btn_miei_ordini = view.findViewById(R.id.btn_ordini);
        Button btn_ritiro_libri = view.findViewById(R.id.btn_ritiro_libri);
        btn_miei_ordini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MieiOrdini();
            }
        });
        btn_ritiro_libri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RitiroLibri();
            }
        });
        return view;
    }
    //Se clicclo sui miei ordini
    public void MieiOrdini()
    {
        getOrdini();

    }
    //se clicco su ritiro libri
    public void RitiroLibri()
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ritiro Libri");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentRitiroLibri());
        fragmentTransaction.commit();
    }
    public void getOrdini(){
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(getActivity(), "Cerco i tuoi ordini!",
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
                par.add(new Ordine(ordini.getJSONObject(i).toString()));


        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            return;
        }
        Parametri.ordinieffettutati = par;
        caricamento.dismiss();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("I miei Ordini");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentMieiOrdini());
        fragmentTransaction.commit();

    }


    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}

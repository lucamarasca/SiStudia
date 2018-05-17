package com.example.lenovo1.sistudia;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentMieiOrdini extends FragmentWithOnBack implements  ConnessioneListener{
    private ProgressDialog caricamento = null;  //Progress dialog di caricamento

    public SistudiaFragmentMieiOrdini() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getOrdini();
        // Creo delle view dinamiche per scrivere dentro gli ordini
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_miei_ordini, container, false);
        if (Parametri.ordinieffettutati != null) {
            TextView[] viewPrenotazioni = new TextView[Parametri.ordinieffettutati.size()];
            View linearLayout = view.findViewById(R.id.prenotazioni);

            for (int i = 0; i < Parametri.ordinieffettutati.size(); i++) {
                viewPrenotazioni[i] = new TextView(view.getContext());
                viewPrenotazioni[i].setText("Ordine");
                viewPrenotazioni[i].setId(i);
                viewPrenotazioni[i].setBackgroundResource(R.drawable.background_menu);
                viewPrenotazioni[i].setPaddingRelative(4, 8, 4, 8);
                viewPrenotazioni[i].setTextSize(19);
                viewPrenotazioni[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                viewPrenotazioni[i].setTextColor(Color.BLACK);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                param.setMargins(0, 0, 0, 32);

                viewPrenotazioni[i].setLayoutParams(param);

                ((LinearLayout) linearLayout).addView(viewPrenotazioni[i]);
            }
        }
        else
        {
            TextView viewPrenotazioni = new TextView(view.getContext());
            View linearLayout = view.findViewById(R.id.prenotazioni);


                viewPrenotazioni.setText("Nessun Ordine trovato.");
                viewPrenotazioni.setPaddingRelative(4, 8, 4, 8);
                viewPrenotazioni.setTextSize(19);
                viewPrenotazioni.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                viewPrenotazioni.setTextColor(Color.BLACK);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                param.setMargins(0, 0, 0, 32);

                viewPrenotazioni.setLayoutParams(param);

                ((LinearLayout) linearLayout).addView(viewPrenotazioni);

        }
        caricamento.dismiss();
        return view;
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
        // Estraggo i miei dati restituiti dal server
        try {
            JSONArray ordini = new JSONArray(result);
            for (int i = 0; i < ordini.length(); i++)
            {
                JSONObject utente = new JSONObject(ordini.getString(i));
                Parametri.ordinieffettutati.get(i).id = utente.getString("id_ordine");
                Parametri.ordinieffettutati.get(i).id = utente.getString("id_ordine");
                Parametri.ordinieffettutati.get(i).nominativo_libraio = utente.getString("nominativo");
                Parametri.ordinieffettutati.get(i).comune = utente.getString("comune");
                Parametri.ordinieffettutati.get(i).provincia = utente.getString("provincia");
                Parametri.ordinieffettutati.get(i).indirizzo = utente.getString("indirizzo");
                Parametri.ordinieffettutati.get(i).civico = utente.getString("civico");
                Parametri.ordinieffettutati.get(i).stato_ordine = utente.getString("stato_ordine");
                Parametri.ordinieffettutati.get(i).nome_alunno = utente.getString("nominativo_alunno");
                Parametri.ordinieffettutati.get(i).data = utente.getString("data");
            }

        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            return;
        }

        caricamento.dismiss();

    }

    @Override
    public boolean onBackPressed() {
        getActivity().setTitle("Home");
        SistudiaMainFragment fragment = new SistudiaMainFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, fragment, "Fragment Find Park");
        fragmentTransaction.commit();
        return true;
    }
}

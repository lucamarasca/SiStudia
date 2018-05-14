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

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentMieiOrdini extends Fragment implements  ConnessioneListener{
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
        //View linearLayout = view.findViewById(R.id.linearInternalBook);
                TextView[] viewPrenotazioni = new TextView[Parametri.ordinieffettutati.size()];
                View linearLayout = view.findViewById(R.id.layout_ordini);

                for (int i = 0; i < Parametri.ordinieffettutati.size(); i++) {
                    viewPrenotazioni[i] = new TextView(view.getContext());
                    viewPrenotazioni[i].setText("Ordine");
                    viewPrenotazioni[i].setId(i);
                   // viewPrenotazioni[i].setBackgroundResource(R.drawable.roundedtextboxactive);
                   // viewPrenotazioni[i].setPaddingRelative(4, 8, 4, 8);
                    viewPrenotazioni[i].setTextSize(19);
                   // viewPrenotazioni[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    viewPrenotazioni[i].setTextColor(Color.BLACK);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    param.setMargins(0, 0, 0, 32);

                    viewPrenotazioni[i].setLayoutParams(param);

                    ((LinearLayout) linearLayout).addView(viewPrenotazioni[i]);
                }

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

            JSONObject utente = new JSONObject(result);
            int numero_ordini = utente.length();
            while (numero_ordini != 0) {
                Ordine.id = utente.getString("id_ordine");
                Ordine.nominativo_libraio = utente.getString("nominativo");
                Ordine.comune = utente.getString("comune");
                Ordine.provincia = utente.getString("provincia");
                Ordine.indirizzo = utente.getString("indirizzo");
                Ordine.civico = utente.getString("civico");
                Ordine.stato_ordine = utente.getString("stato_ordine");
                Ordine.nome_alunno = utente.getString("nominativo_alunno");
                Ordine.data = utente.getString("data");
                numero_ordini--;
            }

        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), MainActivity.class));
            return;
        }

        caricamento.dismiss();

    }
}

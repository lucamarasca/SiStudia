package com.example.lenovo1.sistudia;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentMieiOrdini extends FragmentWithOnBack {


    public SistudiaFragmentMieiOrdini() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Creo delle view dinamiche per scrivere dentro gli ordini
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_miei_ordini, container, false);
        if (Parametri.ordinieffettutati != null) {
            TextView[] viewPrenotazioni = new TextView[Parametri.ordinieffettutati.size()];
            TextView[] viewPrenotazioniData = new TextView[Parametri.ordinieffettutati.size()];
            ImageView[] viewLinea = new ImageView[Parametri.ordinieffettutati.size()];

            View linearLayout = view.findViewById(R.id.prenotazioni);
            int numero = 0;
            for (int i = 0; i < Parametri.ordinieffettutati.size(); i++) {
                String ordine = "Nome Lirabio: " + Parametri.ordinieffettutati.get(i).getNominativo_libraio() + "\n";
                ordine += "Comune Residenza Libraio: " + Parametri.ordinieffettutati.get(i).getComune() + "\n";
                ordine += "Indirizzo Libraio: " + Parametri.ordinieffettutati.get(i).getIndirizzo() + "\n";
                ordine += "Nome alunno: " + Parametri.ordinieffettutati.get(i).getNome_alunno() + "\n";
                ordine += "Stato Ordine: " + Parametri.ordinieffettutati.get(i).getStato_ordine() ;
                viewPrenotazioni[i] = new TextView(view.getContext());
                viewPrenotazioni[i].setText(ordine);
                viewPrenotazioni[i].setId(i);
                viewPrenotazioni[i].setTextSize(17);
                viewPrenotazioni[i].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                viewPrenotazioni[i].setTextColor(Color.BLACK);
                viewPrenotazioniData[i] = new TextView(view.getContext());
                viewPrenotazioniData[i].setText(Parametri.ordinieffettutati.get(i).getData());
                viewPrenotazioniData[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                viewLinea[i] = new ImageView(view.getContext());
                viewLinea[i].setImageResource(R.drawable.separatore);
                viewLinea[i].setMinimumHeight(5);
                if (numero % 2 == 0) {
                    viewPrenotazioni[i].setBackgroundColor(getResources().getColor(R.color.grigio_chiario));
                    viewPrenotazioniData[i].setBackgroundColor(getResources().getColor(R.color.grigio_chiario));
                }
                numero ++;
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );




                //Setto la funzione da chiamare per mostrare i dettagli della prenotazione
                    viewPrenotazioni[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = view.getId();

                            if (index >= 0 && index < Parametri.ordinieffettutati.size()) {
                                //passo le informazioni relative alla mia prenotazione
                                Bundle bundle = new Bundle();
                                //Dati generici Prenotazione
                                bundle.putString("idPrenotazione", String.valueOf(Parametri.ordinieffettutati.get(index).getId()));
                                bundle.putString("nominativo_alunno", String.valueOf(Parametri.ordinieffettutati.get(index).getNome_alunno()));
                                //Dati libraio
                                bundle.putString("nome_libraio", String.valueOf(Parametri.ordinieffettutati.get(index).getNominativo_libraio()));
                                bundle.putString("comune", String.valueOf(Parametri.ordinieffettutati.get(index).getComune()));
                                bundle.putString("provincia", String.valueOf(Parametri.ordinieffettutati.get(index).getProvincia()));
                                bundle.putString("indirizzo", String.valueOf(Parametri.ordinieffettutati.get(index).getIndirizzo()));
                                bundle.putString("civico", String.valueOf(Parametri.ordinieffettutati.get(index).getCivico()));
                                //Dettagli prenotazione
                                bundle.putString("data", String.valueOf(Parametri.ordinieffettutati.get(index).getData()));
                                bundle.putString("stato_ordine", String.valueOf(Parametri.ordinieffettutati.get(index).getStato_ordine()));
                                bundle.putString("id_stato_ordine", String.valueOf(Parametri.ordinieffettutati.get(index).getId_stato_ordine()));

                                //eseguo la transazione
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                //passo i valori
                                Detail_ordine detail_ordine = new Detail_ordine();
                                detail_ordine.setArguments(bundle);
                                //eseguo la transazione
                                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Dettagli Ordine");
                                fragmentTransaction.replace(R.id.main_conteiner, detail_ordine);
                                fragmentTransaction.addToBackStack("Le tue prenotazioni");
                                fragmentTransaction.commit();
                            }
                        }
                    });



                ((LinearLayout) linearLayout).addView(viewPrenotazioni[i]);
                ((LinearLayout) linearLayout).addView(viewPrenotazioniData[i]);
                ((LinearLayout) linearLayout).addView(viewLinea[i]);
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

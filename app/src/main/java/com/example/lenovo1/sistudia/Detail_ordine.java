package com.example.lenovo1.sistudia;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.shuhart.stepview.StepView.ANIMATION_LINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_ordine extends FragmentWithOnBack implements ConnessioneListener {
    private ProgressDialog caricamento = null;  //Progress dialog di caricamento

    public Detail_ordine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_detail_ordine, container, false);

        Bundle bundle = getArguments();
        final String id = bundle.getString("idPrenotazione");
        int id_stato_ordine = Integer.parseInt(bundle.getString("id_stato_ordine"));
        String nome_libraio = bundle.getString("nome_libraio");
        String nome_alunno = bundle.getString("nominativo_alunno");
        String comune = bundle.getString("comune");
        String provincia = bundle.getString("privincia");
        String indirizzo = bundle.getString("provincia");
        String civico = bundle.getString("civico");
        String data = bundle.getString("data");
        String stato_ordine = bundle.getString("stato_ordine");

        String dettagli = "ID: "+id +"\n"+
                "Nome Alunno: " +nome_alunno + "\n"+
                "Nome Libraio: "+nome_libraio+"\n"+
                "Comune Libraio: " + comune + "\n"+
                "Provincia Libraio: " + provincia + "\n"+
                "Indirizzo Libraio: " + indirizzo + "\n"+
                "Civico Libraio: "+ civico + "\n"+
                "Data ordine: " + data + "\n"+
                "stato_ordine: "+stato_ordine + "\n";
        //Id Prenotazione
        TextView id_prentoazione = view.findViewById(R.id.text_dettagli);
        id_prentoazione.setText(dettagli);

        id_prentoazione.setPaddingRelative(4, 8, 4, 8);
        id_prentoazione.setTextSize(19);
        id_prentoazione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        id_prentoazione.setTextColor(Color.BLACK);

        StepView stepView = view.findViewById(R.id.step_view);
        stepView.clearAnimation();
        stepView.getState()
                .animationType(StepView.ANIMATION_CIRCLE)
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("Ricevuto");
                    add("Parzialmente\ncompletato");
                    add("In attesa\ndi ritiro");
                    add("Completato");

                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .animationDuration(getResources().getInteger(android.R.integer.config_longAnimTime))
                .doneStepMarkColor(getResources().getColor(R.color.bianco))
                .doneCircleColor(getResources().getColor(R.color.verde_chiaro))
                // other state methods are equal to the corresponding xml attributes
                .commit();

        stepView.done(true);
        stepView.go(id_stato_ordine,true);
        Button lista = (Button) view.findViewById(R.id.btn_lista_libri);
        //Se clicclo il bottone "Login", chiamo la funzione Login()
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisualizzaListaLibri(id);
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("I miei Ordini");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
        SistudiaFragmentMieiOrdini fragment = new SistudiaFragmentMieiOrdini();


        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, fragment, "Fragment Miei ordini");
        fragmentTransaction.commit();
        return true;


    }
    public void VisualizzaListaLibri(String id_ordine)
    {
        getLibri(id_ordine);

    }
    public void getLibri(String id_ordine)
    {

        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(getActivity(), "Cerco i tuoi ordini!",
                "Connessione con il server in corso...", true);
        caricamento.show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("id_utente", Parametri.id);
            postData.put("id_ordine",id_ordine);

        } catch (Exception e) {
            caricamento.dismiss();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        //In Parametri.IP c'è la path a cui va aggiunta il nome della pagina.php
        conn.execute(Parametri.IP + "/SistudiaListaLibriAndroid.php");

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
        ArrayList<Libro> par = new ArrayList<>();
        // Estraggo i miei dati restituiti dal server
        try {

            JSONObject jsonconvert = new JSONObject(result);
            JSONArray libri = jsonconvert.getJSONArray("libri");
            Ordine.libri = new ArrayList<>();

            for (int i = 0; i < libri.length(); i++) {
                Libro l = new Libro(libri.getJSONObject(i).toString());
                par.add(new Libro(libri.getJSONObject(i).toString()));
            }


        } catch (Exception e) {
            message = "Errore di risposta del server.";

            caricamento.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            return;
        }
        Ordine.libri = par;
        caricamento.dismiss();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista Libri");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaListaLibri());
        fragmentTransaction.commit();
    }


}

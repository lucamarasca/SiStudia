package com.example.lenovo1.sistudia;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;

import static com.shuhart.stepview.StepView.ANIMATION_LINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_ordine extends FragmentWithOnBack {


    public Detail_ordine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_detail_ordine, container, false);

        Bundle bundle = getArguments();
        String id = bundle.getString("idPrenotazione");
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
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                // other state methods are equal to the corresponding xml attributes
                .commit();


        stepView.go(id_stato_ordine,true);
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

}

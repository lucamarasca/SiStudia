package com.example.lenovo1.sistudia;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaListaLibri extends FragmentWithOnBack {


    public SistudiaListaLibri() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Creo delle view dinamiche per scrivere dentro gli ordini
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_miei_ordini, container, false);
        if (Parametri.ordinieffettutati != null) {
            TextView[] viewLibri = new TextView[Parametri.ordinieffettutati.size()];
            TextView[] viewLibriData = new TextView[Parametri.ordinieffettutati.size()];
            ImageView[] viewLinea = new ImageView[Parametri.ordinieffettutati.size()];

            View linearLayout = view.findViewById(R.id.prenotazioni);
            int numero = 0;
            for (int i = 0; i < Parametri.ordinieffettutati.size(); i++) {
                String libro = "Titolo: " + Ordine.libri.get(i).getTitolo() + "\n";
                libro += "Autore: " + Ordine.libri.get(i).getAutore() + "\n";
                libro += "Casa Editrice: " + Ordine.libri.get(i).getCasa_editrice() + "\n";
                libro += "ISBN: " + Ordine.libri.get(i).getCodice_isbn() + "\n";
                libro += "Materia: " + Ordine.libri.get(i).getMateria() ;
                viewLibri[i] = new TextView(view.getContext());
                viewLibri[i].setText(libro);
                viewLibri[i].setId(i);
                viewLibri[i].setTextSize(17);
                viewLibri[i].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                viewLibri[i].setTextColor(Color.BLACK);
                viewLibriData[i] = new TextView(view.getContext());
                viewLibriData[i].setText(Parametri.ordinieffettutati.get(i).getData());
                viewLibriData[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                viewLinea[i] = new ImageView(view.getContext());
                viewLinea[i].setImageResource(R.drawable.separatore);
                viewLinea[i].setMinimumHeight(5);
                if (numero % 2 == 0) {
                    viewLibri[i].setBackgroundColor(getResources().getColor(R.color.grigio_chiario));
                    viewLibriData[i].setBackgroundColor(getResources().getColor(R.color.grigio_chiario));
                }
                numero ++;
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


                ((LinearLayout) linearLayout).addView( viewLibri[i]);
                ((LinearLayout) linearLayout).addView(viewLibriData[i]);
                ((LinearLayout) linearLayout).addView(viewLinea[i]);
            }
        }
        else
        {
            TextView viewPrenotazioni = new TextView(view.getContext());
            View linearLayout = view.findViewById(R.id.prenotazioni);


            viewPrenotazioni.setText("Nessun Libro trovato.");
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("I miei Ordini");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
        SistudiaFragmentMieiOrdini fragment = new SistudiaFragmentMieiOrdini();


        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, fragment, "Fragment Miei ordini");
        fragmentTransaction.commit();
        return true;


    }
}

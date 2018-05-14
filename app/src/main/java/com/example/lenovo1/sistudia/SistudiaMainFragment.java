package com.example.lenovo1.sistudia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaMainFragment extends Fragment {


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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentMieiOrdini());
        fragmentTransaction.commit();
    }
    //se clicco su ritiro libri
    public void RitiroLibri()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_conteiner, new SistudiaFragmentRitiroLibri());
        fragmentTransaction.commit();
    }
}

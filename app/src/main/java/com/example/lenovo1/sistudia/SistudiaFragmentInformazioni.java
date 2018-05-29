package com.example.lenovo1.sistudia;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class SistudiaFragmentInformazioni extends FragmentWithOnBack {


    public SistudiaFragmentInformazioni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sistudia_fragment_informazioni, container, false);
        TextView phone = view.findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:073785208"));


                startActivity(callIntent);
            }
        });
        // Inflate the layout for this fragment
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

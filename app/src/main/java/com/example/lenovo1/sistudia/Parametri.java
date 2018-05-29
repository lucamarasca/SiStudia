package com.example.lenovo1.sistudia;

import android.location.Location;

import java.io.File;
import java.util.List;

public class Parametri {
    // Dati server e connessione
    //static String IP = "http://192.168.1.129/iccs/sistudia_android";
   static String IP = "http://192.168.1.10/Sistudia";

    static String Token = null;
    static File login_file;
    static File advance_setting_file;

    // Dati account utente
    static String id = null;
    static String username = null;
    static String email = null;
    static String password = null;
    static String nome = null;
    static String cognome = null;
    static String data_nascita = null;
    static String telefono = null;
    static List<Ordine> ordinieffettutati = null;


    //Parametri impostazioni e notifiche
    static boolean notifica = false;

    static public void resetAllParametri() {
        Token = null;
        id = null;
        username = null;
        email = null;
        password = null;
        nome = null;
        cognome = null;
        data_nascita = null;
        telefono = null;
    }
}

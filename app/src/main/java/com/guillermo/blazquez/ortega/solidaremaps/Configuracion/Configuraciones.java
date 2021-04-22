package com.guillermo.blazquez.ortega.solidaremaps.Configuracion;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.R;

public class Configuraciones {
    //Fire base Auth
    public static FirebaseUser firebaseUser;

    //Fire base Realdata Time
    public static final String InfoUsers = "Users";
    public static final String FAQPregunta = " FAQPregunta";

    //Storage Imgs
    public static final String fotoInicial = "gs://solidaremaps-proyectofinal.appspot.com/UsersImgs/imgInicial/userImg.png";

    //Valore Fijos Intentes
    public static final int SING_IN_GOOGLE = 1;
    public static final String ID_LOCAL = "ID_LOCAL";

    //Permisos
    public static final int CAMARA_PERMISO = 100;
    public static final int CAMERA_ACTION = 101;

    public static final int GALERIA_PERMISION = 200;
    public static final int GALERIA_ACTION = 201;

    //Metodos genericos
    public static float calcularPuntuacion(DataSnapshot comentarios)  {
        float puntuacion = 0;

        for (int i = 0; i < comentarios.getChildrenCount(); i++) {
            puntuacion = puntuacion + Float.parseFloat(comentarios.child(String.valueOf(i)).child("puntuacion").getValue().toString());
        }

        puntuacion = puntuacion / comentarios.getChildrenCount();
        return puntuacion;
    }




}

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
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.SupportsClass.FavoritosEstado;

import java.util.ArrayList;

public class Configuraciones {
    //Fire base Auth
    public static FirebaseUser firebaseUser;

    //Fire base Realdata Time
    public static final String FAQPregunta = " FAQPregunta";

    //Storage Imgs
    public static final String fotoInicial = "gs://solidaremapsdb.appspot.com/usersImg/user1.png";

    //Valore Fijos Intentes
    public static final int SING_IN_GOOGLE = 1;
    public static final String ID_LOCAL = "ID_LOCAL";
    public static final int SING_GOOGLE = 2;
    public static final String PASAR_MODEL_GALERIA ="MODEL_GALERIA";
    public static final String PASAR_MODEL_WEB ="MODEL_WEB";
    public static final String PASAR_MODEL_MENU ="MODEL_MENU";
    public static final String PASAR_MODEL_DONATIVOS ="MODEL_DONAR";

    //Permisos
    public static final int CAMARA_PERMISO = 100;
    public static final int CAMERA_ACTION = 101;

    public static final int GALERIA_PERMISION = 200;
    public static final int GALERIA_ACTION = 201;

    public static final int GPS_PERMISION = 300;
    public static final int GPS_ACTION = 301;

    public static final int LLAMADA_PERMISION = 400;
    public static final int LLAMADA_ACTION = 401;

    public static final int INTERNET_PERMISION = 500;
    public static final int INTERNT_ACTION = 501;

    //Metodos genericos
    public static float calcularPuntuacion(DataSnapshot comentarios)  {
        float puntuacion = 0;

        for (int i = 0; i < comentarios.getChildrenCount(); i++) {
            puntuacion = puntuacion + Float.parseFloat(comentarios.child(String.valueOf(i)).child("puntuacion").getValue().toString());
        }

        puntuacion = puntuacion / comentarios.getChildrenCount();
        return puntuacion;
    }

    public static void favoritosEstados(ArrayList<String> favs){
        //Comprobamos ref
        //SI ---> elimnar ref + eliminar de lista +  insertr datos en ref
        //NO ---> añadir a ref

        DatabaseReference refFav = FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()).
                child("favoritos");
        refFav.removeValue();

        for (int i = 0; i < favs.size(); i++) {
            refFav.child(String.valueOf(i)).setValue(favs.get(i).toString());
        }

    }




}

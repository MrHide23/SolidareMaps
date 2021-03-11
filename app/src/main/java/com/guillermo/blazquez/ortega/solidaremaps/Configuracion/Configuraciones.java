package com.guillermo.blazquez.ortega.solidaremaps.Configuracion;

import com.google.firebase.auth.FirebaseUser;

public class Configuraciones {
    //Fire base Auth
    public static FirebaseUser firebaseUser;

    //Fire base Realdata Time
    public static final String InfoUsers = "Users";

    //Storage Imgs
    public static final String fotoInicial = "gs://solidaremaps-proyectofinal.appspot.com/UsersImgs/imgInicial/userImg.png";
    public static final String imgPerfil = "/UsersImgs/imgInicial";

    //Valore Fijos Intentes
    public static final int SING_IN_GOOGLE = 1;

    //Permisos
    public static final int CAMARA_PERMISO = 100;
    public static final int CAMERA_ACTION = 101;

    public static final int GALERIA_PERMISION = 200;
    public static final int GALERIA_ACTION = 201;



}

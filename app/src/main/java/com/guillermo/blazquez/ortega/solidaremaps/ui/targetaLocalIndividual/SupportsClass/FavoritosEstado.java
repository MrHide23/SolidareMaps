package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.SupportsClass;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoritosEstado {
    private static ArrayList<String> list;

    public FavoritosEstado(ArrayList<String> list) {
        this.list = list;
    }

    public FavoritosEstado() {
        list = new ArrayList<>();
    }

    public ArrayList<String> getListaFavs() {
        return list;
    }

    public void setListaFavs(ArrayList<String> list) {
        this.list = list;
    }

    public void ClearFavoritos(){
        list.clear();
    }
}

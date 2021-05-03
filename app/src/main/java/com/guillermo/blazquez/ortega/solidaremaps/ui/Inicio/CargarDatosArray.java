package com.guillermo.blazquez.ortega.solidaremaps.ui.Inicio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Models.MarkerInfoModel;

import java.util.ArrayList;

public class CargarDatosArray  extends AsyncTask<DatabaseReference, DatabaseReference, ArrayList<MarkerInfoModel>> {

    @Override
    protected ArrayList<MarkerInfoModel> doInBackground(DatabaseReference... COSA) {
        ArrayList<MarkerInfoModel> lista = new ArrayList<>();

        DatabaseReference fbLocales = FirebaseDatabase.getInstance().getReference("Locales_SM");

        fbLocales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i <snapshot.getChildrenCount() ; i++) {

                    MarkerInfoModel markerInfoModel = new MarkerInfoModel();
                    markerInfoModel.setNombre(snapshot.child(i+"").child("nombre").getValue().toString());
                    markerInfoModel.setTlfLocal(Integer.parseInt(snapshot.child(i+"").child("telefono").getValue().toString()));
                    markerInfoModel.setDireccion(snapshot.child(i+"").child("direccionLocal").child("direccion").getValue().toString());

                    markerInfoModel.setLat(Double.parseDouble(snapshot.child(i+"").child("direccionLocal").child("cordenadas").child("lat").getValue().toString()));
                    markerInfoModel.setLon(Double.parseDouble(snapshot.child(i+"").child("direccionLocal").child("cordenadas").child("lon").getValue().toString()));

                    lista.add(markerInfoModel);

                    Log.d("VALOR ARRAY", "array --> "+lista.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return lista;
    }
}

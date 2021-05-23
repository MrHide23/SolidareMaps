package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.CargarComentariosSupport;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.guillermo.blazquez.ortega.solidaremaps.Models.ComentariosModel;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.ComentariosAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CargarComentarios {

    private ArrayList<ComentariosModel> comentariosLista;
    private DatabaseReference dbComentarios;
    private ComentariosModel comentariosModel;
    private String id;

    public CargarComentarios(String id) {
        dbComentarios = FirebaseDatabase.getInstance().getReference("Locales_SM").child(id).child("comentarios");
        comentariosModel = new ComentariosModel();
        comentariosLista = new ArrayList<ComentariosModel>();
    }

    public void descargarComentarios(){
        dbComentarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    comentariosModel = new ComentariosModel();

                    comentariosModel.setEmail(snapshot.child(i+"").child("email").getValue().toString());
                    comentariosModel.setComentario(snapshot.child(i+"").child("comentario").getValue().toString());
                    comentariosModel.setPuntuacion(Float.parseFloat(snapshot.child(i+"").child("puntuacion").getValue().toString()));

                    comentariosLista.add(comentariosModel);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Log.d("KAKAKKA", " ****** "+comentariosLista.size());
    }

    public ArrayList<ComentariosModel> getDescargaComentarios(){
        return comentariosLista;
    }
}

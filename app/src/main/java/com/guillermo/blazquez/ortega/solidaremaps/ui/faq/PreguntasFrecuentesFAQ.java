package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Models.FAQModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityPagoSubscripcionBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityPreguntasFrecuentesFaqBinding;

import java.util.ArrayList;

public class PreguntasFrecuentesFAQ extends AppCompatActivity {

    private ActivityPreguntasFrecuentesFaqBinding binding;

    //FireBase
    private DatabaseReference bdPreguntas;

    //Adapter
    private ArrayList<FAQModel> listaFAQ;
    private ComunesFAQAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreguntasFrecuentesFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Firebase
        bdPreguntas = FirebaseDatabase.getInstance().getReference("FAQ");

        //Adapter
        listaFAQ = new ArrayList<>();
        adapter = new ComunesFAQAdapter(listaFAQ, R.layout.adapter_preguntas_frecuentes_faq, this);

        binding.rvFAQComunes.setAdapter(adapter);
        binding.rvFAQComunes.setHasFixedSize(true);
        binding.rvFAQComunes.setLayoutManager(new LinearLayoutManager(this));
        rellenarArray(bdPreguntas);

        //SearchView.OnQueryTextListener
        binding.svBuscadorVerFAQ.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query!= null) {
                    buscarFAQ(query);
                }else{
                    Toast.makeText(PreguntasFrecuentesFAQ.this, "No hemos encontrado ninguna coincidencia", Toast.LENGTH_SHORT).show();
                    listaFAQ.clear();
                    rellenarArray(bdPreguntas);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void buscarFAQ(String query) {

        int cuenta = listaFAQ.size();
        ArrayList<FAQModel> LISTA = new ArrayList<>();

        for (int i = 0; i < cuenta; i++) {
            if (listaFAQ.get(i).getTitulo().contains(query)) {
                LISTA.add(listaFAQ.get(i));
            }
        }
        listaFAQ.clear();
        listaFAQ.addAll(LISTA);

        adapter.notifyDataSetChanged();
    }

    private void rellenarArray(DatabaseReference bdPreguntas) {

        bdPreguntas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    listaFAQ.add(new FAQModel(snapshot.child(i+"").child("titulo").getValue().toString(),
                            snapshot.child(i+"").child("contenido").getValue().toString()));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityPagoSubscripcionBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityPreguntasFrecuentesFaqBinding;

import java.util.ArrayList;

public class PreguntasFrecuentesFAQ extends AppCompatActivity {

    private ActivityPreguntasFrecuentesFaqBinding binding;

    //FireBase
    private DatabaseReference bdPreguntas;

    //Adapter
    private ArrayList<String> listaFAQ;
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
    }

    private void rellenarArray(DatabaseReference bdPreguntas) {

        bdPreguntas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    listaFAQ.add(i+"");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
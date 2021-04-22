package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityVerPreguntaFaqBinding;

public class VerPreguntaFAQ extends AppCompatActivity {

    private ActivityVerPreguntaFaqBinding binding;
    private String posPregunta;

    //FireBASE
    private DatabaseReference dbFAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerPreguntaFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Cazamos pos de pregunta FAQ
        posPregunta = getIntent().getExtras().getString(Configuraciones.FAQPregunta);

        dbFAQ = FirebaseDatabase.getInstance().getReference("FAQ").child(posPregunta);
        dbFAQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
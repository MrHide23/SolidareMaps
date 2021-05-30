package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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

        setSupportActionBar(binding.toolbar6);
        binding.toolbar6.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Cazamos pos de pregunta FAQ
        posPregunta = getIntent().getExtras().getString(Configuraciones.FAQPregunta);

        dbFAQ = FirebaseDatabase.getInstance().getReference("FAQ").child(posPregunta);
        dbFAQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.txtTituloVerFAQ.setText(snapshot.child("titulo").getValue().toString());
                binding.txtContenidoVerFAQ.setText(snapshot.child("contenido").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnSiVerFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Subida respuesta si
                respuetaUtilidadFAQ("si");
            }
        });

        binding.btnNoVerFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respuetaUtilidadFAQ("no");
            }
        });
    }

    private void respuetaUtilidadFAQ(String respuesta) {
        dbFAQ.child("util").child(Configuraciones.firebaseUser.getUid()).setValue(respuesta);
    }
}
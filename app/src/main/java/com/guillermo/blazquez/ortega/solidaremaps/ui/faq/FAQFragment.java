package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;

public class FAQFragment extends Fragment {

    private LinearLayout lyComunes, lyChat;
    public DatabaseReference bdFAQ;

    public static FAQFragment newInstance() {
        return new FAQFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.faq_fragment, container, false);

        lyComunes = root.findViewById(R.id.ComunesFAQ);
        lyChat = root.findViewById(R.id.ChatFAQ);

        lyComunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(root.getContext(), PreguntasFrecuentesFAQ.class));
            }
        });

        lyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacer cahrt en directo
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bdFAQ =  FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()+"").
                child("subscripcion").child("tipo");
        bdFAQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().equals("free")) {
                    lyChat.setVisibility(View.GONE);
                }else{
                    lyChat.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
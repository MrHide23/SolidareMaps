package com.guillermo.blazquez.ortega.solidaremaps.ui.subscripcion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;

public class SubscripcionFragment extends Fragment {

    //Elementos
    private Button btnFree, btnGold;
    private ImageView imgFree, imgGold;

    private DatabaseReference subscripcion;


    public static SubscripcionFragment newInstance() {
        return new SubscripcionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.subscripcion_fragment, container, false);

        subscripcion = FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()+"")
                .child("subscripcion");
        btnFree = root.findViewById(R.id.btnSeleccionarFreeSubscripcion);
        btnGold = root.findViewById(R.id.btnSeleccionarGoldSubscripcion);

        imgFree = root.findViewById(R.id.imgSeleccionadoFree);
        imgGold = root.findViewById(R.id.imgSeleccionadoGold);

        btnFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscripcion.child("tipo").setValue("free");

                Toast.makeText(root.getContext(), "Has seleccionado la subscripcion Free", Toast.LENGTH_SHORT).show();
            }
        });

        btnGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarSubscripcion("free", root);
            }
        });


        return root;
    }

    private void CambiarSubscripcion(String gold, View root) {
        subscripcion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("pagado").getValue().toString().equals("true")) {
                    subscripcion.child("tipo").setValue("gold");
                }else{
                    startActivity(new Intent(root.getContext(), pago_subscripcion.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        subscripcion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               switch (snapshot.child("tipo").getValue().toString()){
                   case "free":
                       imgGold.setVisibility(View.GONE);
                       imgFree.setVisibility(View.VISIBLE);
                       break;
                   case "gold":
                       imgGold.setVisibility(View.VISIBLE);
                       imgFree.setVisibility(View.GONE);
                       break;
               }

                if (snapshot.child("pagado").getValue().toString().equals("true")) {
                    btnGold.setText("PAGADO!");
                }else{
                    btnGold.setText("29,99 €/año");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
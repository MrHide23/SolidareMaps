package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;

import java.util.ArrayList;

public class MiLocalFragment extends Fragment {

    // Recycle view
    private ArrayList<String> listaMisLocales;
    private RecyclerView rvMiLocal;
    private MiLocalAdapter adapter;

    //Llamar Real Data
    private DatabaseReference refInfo;
    
    public static MiLocalFragment newInstance() {
        return new MiLocalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mi_local = inflater.inflate(R.layout.mi_local_fragment, container, false);

        refInfo = FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("miLocal");
        listaMisLocales= new ArrayList<>();

        adapter = new MiLocalAdapter(listaMisLocales,R.layout.adapter_mi_local,getContext());
        rvMiLocal = mi_local.findViewById(R.id.rvMiLocal);

        rvMiLocal.setAdapter(adapter);
        rvMiLocal.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarMisLocales(refInfo);

        FloatingActionButton fab = mi_local.findViewById(R.id.fabNewLocal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Users").
                        child(Configuraciones.firebaseUser.getUid()).child("subscripcion").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (listaMisLocales.size() <= 0 && snapshot.getValue().toString().equalsIgnoreCase("gold")) {
                            Log.d("Puedes", "Crea nuevo local");
                        }else{

                            Log.d("Hola", "ya hay un local insertado");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return mi_local;
    }

    private void cargarMisLocales(DatabaseReference refInfo) {
        refInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMisLocales.clear();

                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    listaMisLocales.add(snapshot.child(String.valueOf(i)).getValue().toString());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } //Generalizar metodo

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
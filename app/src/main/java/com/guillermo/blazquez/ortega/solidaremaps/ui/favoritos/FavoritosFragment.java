

package com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;

import java.util.ArrayList;

public class FavoritosFragment extends Fragment {

    //Recycle View
    private ArrayList<String> listaFavoritos;
    private RecyclerView rvFavoritos;
    private FavoritosAdapter adapter;


    //Llamar Real Data
    private FirebaseDatabase infoLocalesFavoritos;
    private DatabaseReference refInfo;


    public static FavoritosFragment newInstance() {
        return new FavoritosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View favoritos = inflater.inflate(R.layout.favoritos_fragment, container, false);

        infoLocalesFavoritos = FirebaseDatabase.getInstance();
        refInfo = infoLocalesFavoritos.getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("favoritos");

        listaFavoritos = new ArrayList<>();

        adapter = new FavoritosAdapter(listaFavoritos, R.layout.adapter_favoritos, getActivity());

        rvFavoritos = favoritos.findViewById(R.id.rvfavoritos);

        rvFavoritos.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvFavoritos.setAdapter(adapter);
        cargarFavoritos(refInfo);

        return favoritos;
    }

    private void cargarFavoritos(DatabaseReference refInfo) {
        refInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 listaFavoritos.clear();

                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    listaFavoritos.add(snapshot.child(String.valueOf(i)).getValue().toString());
                    Log.d("Enseña valor del array ", listaFavoritos.get(i)+" -- "+listaFavoritos.size());
                }

                adapter.notifyDataSetChanged();
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
package com.guillermo.blazquez.ortega.solidaremaps.ui.buscadorLocales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Buscar_Locales extends Fragment {
    //Adapter
    private ArrayList<String> listaLocales;
    private RecyclerView rvLocales;
    private BuscadorLocalesAdapter adapter;

    //Fribase
    private DatabaseReference rfLocales;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View locales = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        rfLocales = FirebaseDatabase.getInstance().getReference("Locales_SM");

        listaLocales = new ArrayList<>();

        adapter = new BuscadorLocalesAdapter(R.layout.adapter_favoritos, listaLocales, getContext());

        rvLocales = locales.findViewById(R.id.rvLocalesBL);
        rvLocales.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvLocales.setAdapter(adapter);

        cargarLocales(rfLocales);

        return locales;
    }

    private void cargarLocales(DatabaseReference rfLocales) {
        rfLocales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listaLocales.clear();

                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    listaLocales.add(i+"");

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}
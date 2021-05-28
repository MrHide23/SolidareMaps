package com.guillermo.blazquez.ortega.solidaremaps.ui.buscadorLocales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.TargetaLocalPrevisualizacionModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Buscar_Locales extends Fragment {
    //Adapter
    private ArrayList<TargetaLocalPrevisualizacionModel> listaLocales;
    private RecyclerView rvLocales;
    private BuscadorLocalesAdapter adapter;

    //Fribase
    private DatabaseReference rfLocales;

    //Componentes
    private SearchView svBuscadorLocales;

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
                    TargetaLocalPrevisualizacionModel targetLocal = new TargetaLocalPrevisualizacionModel();

                    targetLocal.setNombreLocal(snapshot.child(i+"").child("nombre").getValue().toString());
                    targetLocal.setDireccionLocal(snapshot.child(i+"").child("direccionLocal").child("direccion").getValue().toString());
                    targetLocal.setPuntuacionLocal(Configuraciones.calcularPuntuacion(snapshot.child(i+"").child("comentarios")));

                    for (int j = 0; j < snapshot.child(i+"").child("tipoLocal").getChildrenCount(); j++) {
                        targetLocal.setTipoLocal(snapshot.child(i+"").child("tipoLocal").child(j+"").getValue().toString());
                    }

                    for (int k = 0; k < snapshot.child(i+"").child("imgLocal").getChildrenCount(); k++) {
                        targetLocal.setImagenesLocal(snapshot.child(i+"").child("imgLocal").child(k+"").getValue().toString());
                    }
                    listaLocales.add(targetLocal);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svBuscadorLocales = view.findViewById(R.id.svBuscadorLocalesBL);
        svBuscadorLocales.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String local = svBuscadorLocales.getQuery().toString();
                ArrayList<TargetaLocalPrevisualizacionModel> locales = new ArrayList<>();

                if (local != null && !local.equals("")){
                    //Buscamos por titulo
                    //Buscamos por tipo de locales

                    for (int i = 0; i < listaLocales.size(); i++) {

                        if(listaLocales.get(i).getNombreLocal().equalsIgnoreCase(local)||
                                listaLocales.get(i).getTipoLocal().contains(local)){
                            locales.add(listaLocales.get(i));
                        }
                    }

                    listaLocales.clear();
                    listaLocales.addAll(locales);
                }

                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cargarLocales(rfLocales);
                return false;
            }
        });

    }
}
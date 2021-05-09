package com.guillermo.blazquez.ortega.solidaremaps.ui.Inicio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.MarkerInfoModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InicioFragment extends Fragment {

    //Hilos

    private SearchView buscador;
    private static GoogleMap Map;

    //Cargar lista locales
    private ArrayList<MarkerInfoModel> listaLocales;
    private MarkerInfoModel localMarker;
    private DatabaseReference fbLocales;

    //Componentes layout
    private FloatingActionButton fabPosicion;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Map = googleMap;
            listaLocales = new ArrayList<MarkerInfoModel>();
            //fbLocales = FirebaseDatabase.getInstance().getReference("Locales_SM");
            //cargarLocales(fbLocales);

            //+++++++++++++++++++++++++ARREGLAR MARKER MAPA+++++++++++++++
            try {
                listaLocales = new CargarDatosArray().execute().get();
                Thread.sleep(5000);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Log.d("VALOR ARRAY", "array --> " + listaLocales.size());

            for (int i = 0; i < listaLocales.size(); i++) {
                LatLng latLng = new LatLng(listaLocales.get(i).getLat(), listaLocales.get(i).getLon());
                Map.addMarker(new MarkerOptions().position(latLng).title(listaLocales.get(i).getNombre()));

                Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Log.d("VUELTAS", "CARGA " + i);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        fabPosicion = root.findViewById(R.id.fabMiposicionMapa);
        fabPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(){
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    miPosicion();
                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        miPosicion();
                    } else {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        ActivityCompat.requestPermissions(getActivity(), permisos, Configuraciones.GPS_PERMISION);
                    }
                }

            }
        });
        return root;
    }

    private void miPosicion() {
        //Comprobar si GPS activado
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ) {

            Toast.makeText(getContext(), "Se requiere acceso a la localizacion", Toast.LENGTH_SHORT).show();
            fabPosicion.setImageResource(R.drawable.ic_mipos_inaccesible);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (location != null) {
            Log.d("Coordenadas", location.getLatitude()+" - "+location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Map.addMarker(new MarkerOptions().position(latLng).title("Tu"));

            Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }
        else {
            Toast.makeText(getContext(), "No se ha podido acceder a la localizacion", Toast.LENGTH_SHORT).show();
        }
    } //+++++++++ARREGLAR MI POSICION++++++++++++

    private void cargarLocales(DatabaseReference fbLocales) {
        fbLocales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i <snapshot.getChildrenCount() ; i++) {

                    localMarker = new MarkerInfoModel();
                    localMarker.setNombre(snapshot.child(i+"").child("nombre").getValue().toString());
                    localMarker.setTlfLocal(Integer.parseInt(snapshot.child(i+"").child("telefono").getValue().toString()));
                    localMarker.setDireccion(snapshot.child(i+"").child("direccionLocal").child("direccion").getValue().toString());

                    localMarker.setLat(Double.parseDouble(snapshot.child(i+"").child("direccionLocal").child("cordenadas").child("lat").getValue().toString()));
                    localMarker.setLon(Double.parseDouble(snapshot.child(i+"").child("direccionLocal").child("cordenadas").child("lon").getValue().toString()));

                    listaLocales.add(localMarker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buscador = view.findViewById(R.id.svBuscadorLocalMapa);
        SupportMapFragment mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String local = buscador.getQuery().toString();
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (local != null || !local.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(local, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    Map.addMarker(new MarkerOptions().position(latLng).title(local));
                    Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Configuraciones.GPS_PERMISION){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                miPosicion();
                fabPosicion.setImageResource(R.drawable.ic_mipos);
            }
            else {
                Toast.makeText(getContext(), "Se requiere acceso a la localizacion", Toast.LENGTH_SHORT).show();
                fabPosicion.setImageResource(R.drawable.ic_mipos_inaccesible);
            }
        }
    }
}
package com.guillermo.blazquez.ortega.solidaremaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Registro_Inicio.MainActivity;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityMenuSMBinding;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

public class Menu_SM extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuSMBinding binding;

    //Real time date base
    private FirebaseDatabase database;
    private DatabaseReference usuario;

    //Elemnetos database
    private TextView nombreUser;
    private TextView correoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMenuSMBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.contenido.toolbar);

        database = FirebaseDatabase.getInstance();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navElements, navController);

        //Definimos elementos del Heder de l Menu
        binding.navElements.getHeaderView(0).findViewById(R.id.imgbtnLogoutCabecera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Menu_SM.this, MainActivity.class));
                finish();
            }
        });

        binding.navElements.getHeaderView(0).findViewById(R.id.imgbtnEditarUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu_SM.this, Modificar_Info_User.class));
            }
        });

        nombreUser=binding.navElements.getHeaderView(0).findViewById(R.id.txtNombreUserCabecera);
        correoUser=binding.navElements.getHeaderView(0).findViewById(R.id.txtEmailUserCabecera);

        database.getReference("Users").child(Configuraciones.firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nombreUser.setText(snapshot.child("nombre").getValue().toString());
                    correoUser.setText(Configuraciones.firebaseUser.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu__s_m, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
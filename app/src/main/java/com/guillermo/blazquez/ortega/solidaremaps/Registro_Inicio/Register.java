package com.guillermo.blazquez.ortega.solidaremaps.Registro_Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityRegisterBinding;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    //FireBase
    private FirebaseAuth mAuth;

    //Real Data Base
    private FirebaseDatabase database;
    private DatabaseReference usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.txtVolverInicioRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnRegistrarseRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ComprobarCampos()){
                    if(binding.txtContrasenyaRegistrar.getText().toString().equals(binding.txtContrasenyaRepetirRegistrar.getText().toString())){
                            registrarUser(binding.txtEmailRegistrar.getText().toString(),
                                    binding.txtContrasenyaRegistrar.getText().toString());

                    }

                }
            }
        });
    }

    private void registrarUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            usuario = database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("nombre");
                            usuario.setValue(binding.txtNombreRegistrar.getText().toString());

                            usuario =  database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("password");
                            usuario.setValue(binding.txtContrasenyaRegistrar.getText().toString());

                            usuario = database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("email");
                            usuario.setValue(binding.txtEmailRegistrar.getText().toString());

                            usuario = database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("fecha");
                            usuario.setValue(new Date());

                            usuario = database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("subscripcion");
                            usuario.setValue("free");

                            usuario = database.getReference(Configuraciones.InfoUsers).child(mAuth.getUid()).child("imgPerfil");
                            usuario.setValue(Configuraciones.fotoInicial);

                            Toast.makeText(Register.this, "Registro exitoso"+mAuth.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "createUserWithEmail:success");
                        } else {

                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "El registro ha fallado",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean ComprobarCampos() {
        return !binding.txtNombreRegistrar.getText().toString().isEmpty() &&
                !binding.txtEmailRegistrar.getText().toString().isEmpty() &&
                !binding.txtContrasenyaRegistrar.getText().toString().isEmpty() &&
                !binding.txtContrasenyaRepetirRegistrar.getText().toString().isEmpty();
    }
}
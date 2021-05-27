package com.guillermo.blazquez.ortega.solidaremaps.Registro_Inicio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Menu_SM;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityRegisterBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    //FireBase
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogle;

    //Real Data Base
    private FirebaseDatabase database;
    private DatabaseReference usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Sing In google acount
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogle = GoogleSignIn.getClient(this, gso);

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

        binding.btnSingInGoogleRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mGoogle);
            }
        });
    }

    private void signIn(GoogleSignInClient singInGoogle) {
        Intent signInIntent = singInGoogle.getSignInIntent();
        startActivityForResult(signInIntent, Configuraciones.SING_GOOGLE);
    }


    private void singInGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CrearEspacioUserDB(task);
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void registrarUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CrearEspacioUserDB(task);
                    }
                });
    }

    private boolean ComprobarCampos() {
        return !binding.txtNombreRegistrar.getText().toString().isEmpty() &&
                !binding.txtEmailRegistrar.getText().toString().isEmpty() &&
                !binding.txtContrasenyaRegistrar.getText().toString().isEmpty() &&
                !binding.txtContrasenyaRepetirRegistrar.getText().toString().isEmpty();
    }

    private void CrearEspacioUserDB(@NonNull Task<AuthResult> task){
        if (task.isSuccessful()) {

            usuario = database.getReference("Users").child(mAuth.getUid()).child("nombre");
            usuario.setValue(binding.txtNombreRegistrar.getText().toString());

            usuario =  database.getReference("Users").child(mAuth.getUid()).child("password");
            usuario.setValue(binding.txtContrasenyaRegistrar.getText().toString());

            usuario = database.getReference("Users").child(mAuth.getUid()).child("email");
            usuario.setValue(binding.txtEmailRegistrar.getText().toString());

            usuario = database.getReference("Users").child(mAuth.getUid()).child("fecha");
            usuario.setValue(new Date());

            usuario = database.getReference("Users").child(mAuth.getUid()).child("subscripcion").child("tipo");
            usuario.setValue("free");

            usuario = database.getReference("Users").child(mAuth.getUid()).child("subscripcion").child("pagado");
            usuario.setValue(false);

            usuario = database.getReference("Users").child(mAuth.getUid()).child("imgPerfil");
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

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Configuraciones.SING_GOOGLE && data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                singInGoogle(account.getIdToken());

                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }catch (NullPointerException n){
                Log.w("TAG", "Null point Exception ", n);
            }
        }
    }

}
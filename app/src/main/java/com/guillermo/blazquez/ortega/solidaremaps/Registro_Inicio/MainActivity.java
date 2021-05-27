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
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Menu_SM;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //FireBase
    private FirebaseAuth mAuth;
    private GoogleSignInClient google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gson = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        google = GoogleSignIn.getClient(this, gson);

        binding.txtRegistrarseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        binding.btnInicarSesionLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logearUser(binding.txtUserLogin.getText().toString(),
                        binding.txtPasswordLogin.getText().toString());
            }
        });

        binding.btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicioConGoogle();
            }
        });


    }

    private void inicioConGoogle() {
        Intent intent = google.getSignInIntent();
        startActivityForResult(intent, Configuraciones.SING_IN_GOOGLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Configuraciones.SING_IN_GOOGLE && data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Inico de sesion exitoso", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro al iniciar sesion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            Configuraciones.firebaseUser = mAuth.getCurrentUser();
                            updateUI(Configuraciones.firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Fallo al inicar sesion", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void logearUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Configuraciones.firebaseUser = mAuth.getCurrentUser();
                            updateUI(Configuraciones.firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Ha fallado la autentificaccion", //Poner en ingles
                                    Toast.LENGTH_SHORT).show();
                            Configuraciones.firebaseUser = null;
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            startActivity(new Intent(MainActivity.this, Menu_SM.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Configuraciones.firebaseUser = mAuth.getCurrentUser();
        updateUI( Configuraciones.firebaseUser);
    }

}




package com.guillermo.blazquez.ortega.solidaremaps.NoConection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.Registro_Inicio.MainActivity;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityCrearLocalMiLocalBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityNoConectionBinding;

public class NoConectionActivity extends AppCompatActivity {
    private ActivityNoConectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoConectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lanzarAlert();
    }

    private void comprobarConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConeted = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConeted) {
            Log.d("Ha finalizado", "2");
            finish();
            //startActivity(new Intent(NoConectionActivity.this, MainActivity.class));
        }else{
            lanzarAlert();
        }
    }

    private void lanzarAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error de conexion");
        alert.setMessage("Se requeire connexion para continuar utilizando SolidareMaps");
        alert.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comprobarConexion();
            }
        });
        alert.setNegativeButton("Cerrar App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
            }
        });

        alert.show();

    }
}
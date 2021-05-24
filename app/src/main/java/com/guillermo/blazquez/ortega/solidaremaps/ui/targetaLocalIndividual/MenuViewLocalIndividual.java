package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityMenuViewLocalIndividualBinding;

import java.util.ArrayList;

public class MenuViewLocalIndividual extends AppCompatActivity {
    private ActivityMenuViewLocalIndividualBinding binding;
    private LocalModel localModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuViewLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        localModel = getIntent().getParcelableExtra(Configuraciones.PASAR_MODEL_MENU);

        binding.toolbar10.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
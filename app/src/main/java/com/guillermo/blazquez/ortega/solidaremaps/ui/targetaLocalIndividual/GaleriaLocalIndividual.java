package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityGaleriaLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.GaleriaLocalAdapter;

public class GaleriaLocalIndividual extends AppCompatActivity {

    private ActivityGaleriaLocalIndividualBinding binding;

    //GridView
    private GridView gvFotos;
    private LocalModel localModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGaleriaLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Enlazamos toolbar
        setSupportActionBar(binding.toolbar8);
        binding.toolbar8.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Traer info
        localModel = getIntent().getParcelableExtra(Configuraciones.PASAR_MODEL_GALERIA);

        //Insertar grid View
        binding.gvImagenesGaleriaLocal.setAdapter(new GaleriaLocalAdapter(this, localModel.getImgLocal()));

    }

}
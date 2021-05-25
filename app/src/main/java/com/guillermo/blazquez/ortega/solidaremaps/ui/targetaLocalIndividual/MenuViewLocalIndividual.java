package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityMenuViewLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.MenuLocalAdapter;

import java.util.ArrayList;

public class MenuViewLocalIndividual extends AppCompatActivity {
    private ActivityMenuViewLocalIndividualBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuViewLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalModel localModel = getIntent().getParcelableExtra(Configuraciones.PASAR_MODEL_MENU);

        binding.toolbar10.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.gvMenuLocalIndividual.setAdapter(new MenuLocalAdapter(this, localModel.getMenuLocal()));

    }
}
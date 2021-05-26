package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityDonativoLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityGaleriaLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.DonarAdapter;

public class DonativoLocalIndividual extends AppCompatActivity {

    private ActivityDonativoLocalIndividualBinding binding;

    private DonarAdapter adapter;

    private LocalModel localModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonativoLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar12.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        localModel = getIntent().getParcelableExtra(Configuraciones.PASAR_MODEL_DONATIVOS);

        Log.d("ñañañ", "+++++ "+localModel.getListaDonativos());
//        adapter = new DonarAdapter(this, localModel.getListaDonativos().get(0).getOpciones(), R.layout.adapter_donativos_local_individual);
//
//        binding.rvDonativos.setLayoutManager(new LinearLayoutManager(this));
//        binding.rvDonativos.setAdapter(adapter);
//
//        adapter.notifyDataSetChanged();

    }
}
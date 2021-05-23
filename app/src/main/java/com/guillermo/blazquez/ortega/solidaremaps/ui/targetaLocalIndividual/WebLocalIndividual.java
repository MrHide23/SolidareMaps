package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityGaleriaLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityWebLocalIndividualBinding;

public class WebLocalIndividual extends AppCompatActivity {
    private ActivityWebLocalIndividualBinding binding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        url = getIntent().getStringExtra(Configuraciones.PASAR_MODEL_WEB);

        binding.toolbar9.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.wvLocalIndividual.getSettings().setJavaScriptEnabled(true);
        binding.wvLocalIndividual.setWebViewClient(new WebViewClient());
        binding.wvLocalIndividual.loadUrl(url);

    }
}
package com.guillermo.blazquez.ortega.solidaremaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivitySerieIndividualBinding;

public class SerieIndividual extends AppCompatActivity {

    private ActivitySerieIndividualBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySerieIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configuramos Toolbar
        setSupportActionBar(binding.toolbar3);
        binding.toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_serie_individual, menu);
        menu.findItem(R.id.btnBarFavorito);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnBarFavorito) {
            //Cambiar de fav a no-fav o alreves
            Log.d("HAs cambiado el estado", null);
        }
        return super.onOptionsItemSelected(item);
    }
}
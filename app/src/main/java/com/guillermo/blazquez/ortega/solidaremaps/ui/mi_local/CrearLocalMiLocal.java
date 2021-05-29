package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityCrearLocalMiLocalBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter.DonativosMiLocalAdapter;

import java.util.ArrayList;

public class CrearLocalMiLocal extends AppCompatActivity {

    private ActivityCrearLocalMiLocalBinding binding;

    //Adapter Tipo Local
    private ArrayList<String> tipoLocal;
    private DonativosMiLocalAdapter adapterDonativos;

    //Valores fijos
    private int visible;
    private LocalModel localModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearLocalMiLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Instanciacion valores
        visible = 1;
        localModel = new LocalModel();

        setSupportActionBar(binding.toolbar11);
        binding.toolbar11.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible = visible-1 ;

                onResume();
            }
        });

        //Instanciacion Layout 1



        //Acciones
        binding.fabComprobarMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible = visible +1;

                validacionCamposDatosTexto();
                onResume();

            }
        });
    }


    //Validacciones

    private void validacionCamposDatosTexto() {
        if (!binding.txtNombreLocalMiLoca.getText().toString().isEmpty() &&
        !binding.txtTelefonoMiLocal.getText().toString().isEmpty() &&
        !binding.txtDireccionLocalMiLocal.getText().toString().isEmpty()) {

        }
    }

    private void SeleccionarApartado() {
        switch (visible){
            case 1:
                binding.lyDatosTexto.setVisibility(View.VISIBLE);
                binding.lyHorarios.setVisibility(View.GONE);
                break;
            case 2:
                binding.lyDatosTexto.setVisibility(View.GONE);
                binding.lyHorarios.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.lyHorarios.setVisibility(View.GONE);
                binding.lyDescripcion.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.lyDescripcion.setVisibility(View.GONE);
                binding.lyFotosLocal.setVisibility(View.VISIBLE);
                binding.fabComprobarMiLocal.setImageResource(R.drawable.ic_arrow_down);
                break;
            case 5:
                binding.lyMenu.setVisibility(View.VISIBLE);
                binding.lyFotosLocal.setVisibility(View.GONE);
                binding.fabComprobarMiLocal.setImageResource(R.drawable.ic_confirmar_check);
                break;
            default:
                finish();
                break;
        }
    } //Determinamos que apartado es visible

    @Override
    protected void onResume() {
        super.onResume();
        SeleccionarApartado();
    }
}
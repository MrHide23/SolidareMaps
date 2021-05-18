package com.guillermo.blazquez.ortega.solidaremaps.ui.buscadorLocales;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guillermo.blazquez.ortega.solidaremaps.R;

public class Buscar_Locales extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Buscador_local = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        return Buscador_local;
    }
}
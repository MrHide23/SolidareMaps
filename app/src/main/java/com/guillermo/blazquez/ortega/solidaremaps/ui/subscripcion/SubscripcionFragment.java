package com.guillermo.blazquez.ortega.solidaremaps.ui.subscripcion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guillermo.blazquez.ortega.solidaremaps.R;

public class SubscripcionFragment extends Fragment {

    private SubscripcionViewModel subscripcionViewModel;

    public static SubscripcionFragment newInstance() {
        return new SubscripcionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        subscripcionViewModel = new ViewModelProvider(this).get(SubscripcionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mapa, container, false);


        return root;
    }


}
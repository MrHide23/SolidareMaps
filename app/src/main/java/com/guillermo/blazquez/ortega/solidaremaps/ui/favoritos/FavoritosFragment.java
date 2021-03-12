package com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guillermo.blazquez.ortega.solidaremaps.R;

public class FavoritosFragment extends Fragment {

    private FavoritosViewModel mViewModel;

    public static FavoritosFragment newInstance() {
        return new FavoritosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favoritos_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoritosViewModel.class);
        // TODO: Use the ViewModel
    }

}
package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guillermo.blazquez.ortega.solidaremaps.R;

public class MiLocalFragment extends Fragment {

    private MiLocalViewModel mViewModel;

    public static MiLocalFragment newInstance() {
        return new MiLocalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mi_local_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MiLocalViewModel.class);
        // TODO: Use the ViewModel
    }

}
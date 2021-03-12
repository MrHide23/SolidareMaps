package com.guillermo.blazquez.ortega.solidaremaps.ui.Inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guillermo.blazquez.ortega.solidaremaps.R;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;

    //Data base
    private FirebaseDatabase data;
    private DatabaseReference user;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        data = FirebaseDatabase.getInstance();

        final TextView textView = root.findViewById(R.id.text_home);

        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
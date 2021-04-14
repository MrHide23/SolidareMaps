package com.guillermo.blazquez.ortega.solidaremaps.ui.subscripcion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityPagoSubscripcionBinding;

public class pago_subscripcion extends AppCompatActivity {

    private DatabaseReference subscripcion;

    private ActivityPagoSubscripcionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagoSubscripcionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subscripcion =  FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()+"")
                .child("subscripcion");

        binding.btnPagarSubscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ComprobarCampos()){
                    subscripcion.child("pagado").setValue("true");
                    subscripcion.child("tipo").setValue("gold");
                    finish();
                }
            }
        });

    }

    private boolean ComprobarCampos() {
        return !binding.txtNumeroTargetaSubscripcion.getText().toString().isEmpty() &&
        !binding.txtNombrePropietarioSubscripcion.getText().toString().isEmpty() &&
        !binding.txtFechaCaducidadSubscripcion.getText().toString().isEmpty() &&
        !binding.txtCodigoCVSubscripcion.getText().toString().isEmpty();
    }

}
package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.AppDonativosModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityCrearLocalMiLocalBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter.DonativosMiLocalAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CrearLocalMiLocal extends AppCompatActivity {

    private ActivityCrearLocalMiLocalBinding binding;

    //Valores fijos
    private int visible;
    private LocalModel localModel;
    private Boolean estadobtn;
    private AppDonativosModel appDonativosModel;

    //Adapter Donativos
    private ArrayList<String> tipoLocal;
    private ArrayList<AppDonativosModel> listaDonativos;
    private DonativosMiLocalAdapter adapterDonativos;

    //Definicion Layout 4
    private int numeroLocales;
    private ArrayList<String> nombresImgsLocal;
    private DatabaseReference localesBD;
    private StorageReference imgLocal;
    private StorageReference imgMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearLocalMiLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Instanciacion valores
        visible = 1;
        estadobtn = false;
        localModel = new LocalModel();
        listaDonativos = new ArrayList<>();
        tipoLocal = new ArrayList<>();
        nombresImgsLocal = new ArrayList<>();
        localesBD = FirebaseDatabase.getInstance().getReference("Locales_SM");


        setSupportActionBar(binding.toolbar11);
        binding.toolbar11.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible = visible - 1;
                onResume();
            }
        });

        //Acciones Layout 1
        adapterDonativos = new DonativosMiLocalAdapter(this, listaDonativos, R.layout.adapter_donativos_mi_local);
        binding.rvAppDonativos.setHasFixedSize(true);
        binding.rvAppDonativos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAppDonativos.setAdapter(adapterDonativos);

        binding.imgbtnOpcionesLocalMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir alert con opciones
                alertTipoLocal();
            }
        });

        binding.swDonativosLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.swDonativosLocal.isChecked()) {
                    binding.lyDonativosActivos.setVisibility(View.VISIBLE);
                }else{
                    binding.lyDonativosActivos.setVisibility(View.GONE);
                }
            }
        });
        binding.btnNuevoDonativosMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtAppDonativoMiLocal.getText().toString().isEmpty() &&
                    !binding.txtIDDonativoMiLocal.getText().toString().isEmpty()) {
                    appDonativosModel = new AppDonativosModel(binding.txtAppDonativoMiLocal.getText().toString(),
                            binding.txtIDDonativoMiLocal.getText().toString());

                    listaDonativos.add(appDonativosModel);
                    adapterDonativos.notifyDataSetChanged();
                }else{
                    Toast.makeText(CrearLocalMiLocal.this, "No se admiten campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Acciones Layout 4
        localesBD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Configuraciones.numLocales = (int) snapshot.getChildrenCount();
                adapterDonativos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        imgLocal = FirebaseStorage.getInstance().getReference("localesImgs").child(Configuraciones.numLocales+"");
        imgMenu = FirebaseStorage.getInstance().getReference("localesImgs").child(Configuraciones.numLocales+"");

        binding.lySacarFotoMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.lyAbrirGaleriaMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Acciones Generales
        binding.fabComprobarMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validado = false;

                switch (visible) {
                    case 1:
                        validado = validacionCamposDatosTexto();
                        break;
                    case 2:
                        validado = validacionHorarios();
                        break;

                    case 3:
                        validado = validacionDescripcion();
                        break;
                }

                if (validado) {
                    visible = visible + 1;
                    onResume();
                }

            }
        });
    }


    //Validacion Layout 1
    private boolean validacionCamposDatosTexto() {
        if (!binding.txtNombreLocalMiLoca.getText().toString().isEmpty() &&
                !binding.txtTelefonoMiLocal.getText().toString().isEmpty() &&
                !binding.txtDireccionLocalMiLocal.getText().toString().isEmpty() &&
                tipoLocal.size() > 0) {

            if (binding.swDonativosLocal.isChecked() && listaDonativos.size()>0) {

                    for (int i = 0; i < listaDonativos.size(); i++) {
                        appDonativosModel = new AppDonativosModel(listaDonativos.get(i).getApp(), listaDonativos.get(i).getUser());
                        localModel.getListaDonativos().get(0).setOpciones(appDonativosModel);
                    }

            }else{
                return false;
            }

            localModel.getTipoLocal().addAll(tipoLocal);
            localModel.setNombreLocal(binding.txtNombreLocalMiLoca.getText().toString());
            localModel.getDireccionLocal().setDireccion(binding.txtDireccionLocalMiLocal.getText().toString());
            localModel.setTelefonoLocal(binding.txtTelefonoMiLocal.getText().toString());

            return true;
        }

        return false;
    }

    private void alertTipoLocal() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Seleccion tipo Local");
        View view = LayoutInflater.from(this).inflate(R.layout.alert_seleccion_tipo_local_mi_local, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);

        CheckBox rbBarTradicionalAlert = view.findViewById(R.id.rbBarTradicionalAlert);
        CheckBox rbGatrobarAlert = view.findViewById(R.id.rbGatrobarAlert);
        CheckBox rbAfterlAlert = view.findViewById(R.id.rbAfterlAlert);
        CheckBox rbChiringitoAlert = view.findViewById(R.id.rbChiringitoAlert);
        CheckBox rbPubAlert = view.findViewById(R.id.rbPubAlert);
        CheckBox rbBarVinosAlert = view.findViewById(R.id.rbBarVinosAlert);
        CheckBox rbBarCoctelesAlert = view.findViewById(R.id.rbBarCoctelesAlert);
        CheckBox rbBarCafesAlert = view.findViewById(R.id.rbBarCafesAlert);
        CheckBox rbBarTabernaAlert = view.findViewById(R.id.rbBarTabernaAlert);
        CheckBox rbBarCerveceriaAlert = view.findViewById(R.id.rbBarCerveceriaAlert);


        alertDialog.setNegativeButton("Cancelar", null);
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tipoLocal.clear();

                for (int i = 0; i < 9; i++) {
                    switch (i) {
                        case 0:
                            if (rbBarTradicionalAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarTradicionalAlert.getText()));
                            }
                            break;
                        case 1:
                            if (rbGatrobarAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbGatrobarAlert.getText()));
                            }
                            break;
                        case 2:
                            if (rbAfterlAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbAfterlAlert.getText()));
                            }
                            break;
                        case 3:

                            if (rbChiringitoAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbChiringitoAlert.getText()));
                            }
                            break;
                        case 4:
                            if (rbPubAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbPubAlert.getText()));
                            }
                            break;
                        case 5:

                            if (rbBarVinosAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarVinosAlert.getText()));
                            }
                            break;
                        case 6:

                            if (rbBarCoctelesAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarCoctelesAlert.getText()));
                            }
                            break;
                        case 7:
                            if (rbBarCafesAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarCafesAlert.getText()));
                            }
                            break;
                        case 8:
                            if (rbBarTabernaAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarTabernaAlert.getText()));
                            }
                            break;
                        case 9:
                            if (rbBarCerveceriaAlert.isChecked()) {
                                tipoLocal.add(String.valueOf(rbBarCerveceriaAlert.getText()));
                            }
                            break;
                    }

                }

                if (tipoLocal.size() > 0) {
                    String valor = "";

                    for (int j = 0; j < tipoLocal.size(); j++) {
                        valor = valor.concat(tipoLocal.get(j) + "   ");
                    }

                    binding.txtTipoLocalMiLocal.setText(valor);
                }
            }
        });

        alertDialog.show();
    }


    //Validacion Layout 2

    private boolean estadoCamposHoras() {
        return !binding.txtHora1Lunes.getText().toString().isEmpty() && !binding.txtHora2Lunes.getText().toString().isEmpty() ||
                !binding.txtHora1Martes.getText().toString().isEmpty() && !binding.txtHora2Martes.getText().toString().isEmpty() ||
                !binding.txtHora1Miercoles.getText().toString().isEmpty() && !binding.txtHora2Miercoles.getText().toString().isEmpty() ||
                !binding.txtHora2Jueves.getText().toString().isEmpty() && !binding.txtHora2Jueves.getText().toString().isEmpty() ||
                !binding.txtHora1Viernes.getText().toString().isEmpty() && !binding.txtHora2Viernes.getText().toString().isEmpty() ||
                !binding.txtHora1Sabado.getText().toString().isEmpty() && !binding.txtHora2Sabado.getText().toString().isEmpty() ||
                !binding.txtHora1Domingo.getText().toString().isEmpty() && !binding.txtHora2Domingo.getText().toString().isEmpty();
    }

    private boolean validacionHorarios(){
        if(estadoCamposHoras()){
            localModel.getHorarios().clear();
            ArrayList<String> listaHoras = new ArrayList<>();
            String valor = null;
            String valor2 = null;

            for (int i = 0; i < 6; i++) {
                valor = "CERRADO";
                valor2 = "CERRADO";
                
                switch (i){
                    case 0:
                        if(!binding.txtHora1Lunes.getText().toString().isEmpty() && !binding.txtHora2Lunes.getText().toString().isEmpty()){
                            valor = binding.txtHora1Lunes.getText().toString()+" - "+binding.txtHora2Lunes.getText().toString();
                        }
                        if (!binding.txtHora3Lunes.getText().toString().isEmpty() && !binding.txtHora4Lunes.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Lunes.getText().toString()+" - "+binding.txtHora4Lunes.getText().toString();
                        }

                        
                        break;
                    case 1:
                        if(!binding.txtHora1Martes.getText().toString().isEmpty() && !binding.txtHora2Martes.getText().toString().isEmpty()){
                            valor = binding.txtHora1Martes.getText().toString()+" - "+binding.txtHora2Martes.getText().toString();
                        }
                        if (!binding.txtHora3Martes.getText().toString().isEmpty() && !binding.txtHora4Martes.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Martes.getText().toString()+" - "+binding.txtHora4Martes.getText().toString();
                        }
                        break;
                    case 2:
                        if(!binding.txtHora1Miercoles.getText().toString().isEmpty() && !binding.txtHora2Miercoles.getText().toString().isEmpty()){
                            valor = binding.txtHora1Miercoles.getText().toString()+" - "+binding.txtHora2Miercoles.getText().toString();
                        }
                        if (!binding.txtHora3Miercoles.getText().toString().isEmpty() && !binding.txtHora4Miercoles.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Miercoles.getText().toString()+" - "+binding.txtHora4Miercoles.getText().toString();
                        }
                        break;
                    case 3:
                        if(!binding.txtHora1Jueves.getText().toString().isEmpty() && !binding.txtHora2Jueves.getText().toString().isEmpty()){
                            valor = binding.txtHora1Jueves.getText().toString()+" - "+binding.txtHora2Jueves.getText().toString();
                        }
                        if (!binding.txtHora3Jueves.getText().toString().isEmpty() && !binding.txtHora4Jueves.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Jueves.getText().toString()+" - "+binding.txtHora4Jueves.getText().toString();
                        }
                        break;
                    case 5:
                        if(!binding.txtHora1Viernes.getText().toString().isEmpty() && !binding.txtHora2Viernes.getText().toString().isEmpty()){
                            valor = binding.txtHora1Viernes.getText().toString()+" - "+binding.txtHora2Viernes.getText().toString();
                        }
                        if (!binding.txtHora3Viernes.getText().toString().isEmpty() && !binding.txtHora4Viernes.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Viernes.getText().toString()+" - "+binding.txtHora4Viernes.getText().toString();
                        }
                        break;
                    case 6:
                        if(!binding.txtHora1Sabado.getText().toString().isEmpty() && !binding.txtHora2Sabado.getText().toString().isEmpty()){
                            valor = binding.txtHora1Sabado.getText().toString()+" - "+binding.txtHora2Sabado.getText().toString();
                        }
                        if (!binding.txtHora3Sabado.getText().toString().isEmpty() && !binding.txtHora4Sabado.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Sabado.getText().toString()+" - "+binding.txtHora4Sabado.getText().toString();
                        }
                        break;
                    case 7:
                        if(!binding.txtHora1Domingo.getText().toString().isEmpty() && !binding.txtHora2Domingo.getText().toString().isEmpty()){
                            valor = binding.txtHora1Domingo.getText().toString()+" - "+binding.txtHora2Domingo.getText().toString();
                        }
                        if (!binding.txtHora3Domingo.getText().toString().isEmpty() && !binding.txtHora4Domingo.getText().toString().isEmpty()) {
                            valor2 = binding.txtHora3Domingo.getText().toString()+" - "+binding.txtHora4Domingo.getText().toString();
                        }
                        break;
                }

                listaHoras.add((valor+" , "+valor2));
            }

            //Añdir horas
            localModel.getHorarios().addAll(listaHoras);
            return true;
        }else {
            Toast.makeText(this, "Debe de haber como minimo 1 horario por la mañana o por la tarde", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    //Validacion layout 3
    private boolean validacionDescripcion(){
        if(!binding.txtDireccionLocalMiLocal.getText().toString().isEmpty()){
            localModel.setDescripcionLocal(binding.txtDireccionLocalMiLocal.getText().toString());
            return true;
        }
        return false;
    }

    //Validacion layout 4
    private boolean validacionImgsLocal(){


        return false;
    }


    //Codigo Generico
    private void SeleccionarApartado() {
        switch (visible) {
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
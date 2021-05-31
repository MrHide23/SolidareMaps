package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.guillermo.blazquez.ortega.solidaremaps.Modificar_Info_User;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityCrearLocalMiLocalBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityLocalIndividualBinding;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.AdapterImagenesMiLocalBinding;
import com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter.DonativosMiLocalAdapter;
import com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter.ImagenesLocalAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrearLocalMiLocal extends AppCompatActivity {

    private ActivityCrearLocalMiLocalBinding binding;

    //Valores fijos
    private String cargarDatos;
    private int visible;
    private LocalModel localModel;
    private Boolean estadobtn;
    private AppDonativosModel appDonativosModel;

    //Adapter Donativos
    private ArrayList<String> tipoLocal;
    private ArrayList<AppDonativosModel> listaDonativos;
    private DonativosMiLocalAdapter adapterDonativos;

    //Definicion Layout 4
    private ImagenesLocalAdapter adapterImagenes;
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
        listaDonativos = new ArrayList<AppDonativosModel>();
        tipoLocal = new ArrayList<>();
        nombresImgsLocal = new ArrayList<>();
        localesBD = FirebaseDatabase.getInstance().getReference("Locales_SM");

        if(getIntent().getExtras().getString(Configuraciones.ID_LOCAL) != null){

            cargarDatos = getIntent().getExtras().getString(Configuraciones.ID_LOCAL);
            localesBD.child(cargarDatos).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    binding.txtNombreLocalMiLoca.setText(snapshot.child("nombre").getValue().toString());
                    binding.txtDireccionLocalMiLocal.setText(snapshot.child("direccionLocal").child("direccion").getValue().toString());
                    binding.txtTelefonoMiLocal.setText(snapshot.child("telefono").getValue().toString());

                    String valor1="";
                    for (int j = 0; j < snapshot.child("tipoLocal").getChildrenCount(); j++) {
                        valor1 = valor1.concat(snapshot.child("tipoLocal").child(j+"").getValue().toString() + "   ");
                    }
                    binding.txtTipoLocalMiLocal.setText(valor1);
                    binding.swDonativosLocal.setChecked(Boolean.parseBoolean(snapshot.child("donativos").child("estado").getValue().toString()));

                    if ( binding.swDonativosLocal.isChecked()) {
                        binding.lyDonativosActivos.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < snapshot.child("donativos").child("opciones").getChildrenCount(); i++) {
                    appDonativosModel = new AppDonativosModel(snapshot.child("donativos").child("opciones").child(i+"").child("appDonativos").getValue().toString(),
                                                            snapshot.child("donativos").child("opciones").child(i+"").child("id_user").getValue().toString());
                     listaDonativos.add(appDonativosModel);
                    }

                    //Descripccion
                    binding.txtDireccionLocalMiLocal.setText(snapshot.child("descripcionLocal").getValue().toString());

                    //Horas
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

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

                    AppDonativosModel appDonativosModel = new AppDonativosModel(binding.txtAppDonativoMiLocal.getText().toString(),
                            binding.txtIDDonativoMiLocal.getText().toString());

                    listaDonativos.add(appDonativosModel);
                    adapterDonativos.notifyDataSetChanged();
                }else{
                    Toast.makeText(CrearLocalMiLocal.this, "No se admiten campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Acciones Layout 4
        adapterImagenes = new ImagenesLocalAdapter(this, nombresImgsLocal, R.layout.adapter_imagenes_mi_local);
        binding.ryImgLocal.setHasFixedSize(true);
        binding.ryImgLocal.setLayoutManager(new GridLayoutManager(this, 3));
        binding.ryImgLocal.setAdapter(adapterImagenes);

        imgLocal = FirebaseStorage.getInstance().getReference("localesImgs").child(Configuraciones.numLocales+"");
        imgMenu = FirebaseStorage.getInstance().getReference("localesImgs").child(Configuraciones.numLocales+"");

        binding.lySacarFotoMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    AbrirCamara();
                }
                else {
                    if (ContextCompat.checkSelfPermission(CrearLocalMiLocal.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(CrearLocalMiLocal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED ) {
                        AbrirCamara();
                    }
                    else {
                        String[] permisos = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(CrearLocalMiLocal.this, permisos, Configuraciones.CAMARA_PERMISO);
                    }
                }
            }
        });
        binding.lyAbrirGaleriaMiLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    SeleccionarGaleria();
                }else{
                    if (ContextCompat.checkSelfPermission(CrearLocalMiLocal.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED  ) {
                        SeleccionarGaleria();
                    }else{
                        ActivityCompat.requestPermissions(CrearLocalMiLocal.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Configuraciones.GALERIA_PERMISION);
                    }
                }

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
                    case 4:
                        validado =validacionImgsLocal();
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
                        localModel.getListaDonativos().setOpciones(appDonativosModel);
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
        if (nombresImgsLocal.size()>0) {
            return true;
        }

        return false;
    }

    private void AbrirCamara() {
        try {
            File photoFile = createImageFile();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photURI = FileProvider.getUriForFile(CrearLocalMiLocal.this,
                    "com.guillermo.blazquez.ortega.solidaremaps", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photURI);
            startActivityForResult(cameraIntent, Configuraciones.CAMERA_ACTION);

        } catch (IOException e) {
            Toast.makeText(this, "Error al escribir el fichero", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(imageFileName, ".jpg", storageDir);
        nombresImgsLocal.add(imagen.getAbsolutePath());
        return imagen;
    }

    private void SeleccionarGaleria() {
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
            case 6:
                //Subir imgs local
                //Subir local a Local_SM
                break;
            default:
                finish();
                break;
        }
    } //Determinamos que apartado es visible

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Configuraciones.CAMERA_ACTION && resultCode == RESULT_OK && data != null) {
            adapterImagenes.notifyDataSetChanged();
        }

        if (requestCode == Configuraciones.GALERIA_ACTION && resultCode== RESULT_OK && data!=null) {
            nombresImgsLocal.add(data.getData().toString());
            adapterImagenes.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Configuraciones.CAMARA_PERMISO){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                AbrirCamara();
            }
            else {
                Toast.makeText(this, "Se requieren permisos para realizazar esta accion", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == Configuraciones.GALERIA_PERMISION) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SeleccionarGaleria();
            }else{
                Toast.makeText(this, "Se requieren permisos para realizazar esta accion", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SeleccionarApartado();
    }
}
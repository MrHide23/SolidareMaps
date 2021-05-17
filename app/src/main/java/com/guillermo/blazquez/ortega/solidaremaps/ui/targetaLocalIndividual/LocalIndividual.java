package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.ComentariosModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.AppDonativosModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.CoordenadasModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.DireccionModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.DonativoModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.LocalModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ActivityLocalIndividualBinding;

import org.jetbrains.annotations.NotNull;

public class LocalIndividual extends AppCompatActivity {

    private ActivityLocalIndividualBinding binding;
    private String idLocal;

    //Componenetes
    private boolean horariosDesplegados, comentariosDesplegados;
    private float putuacionBar;

    //Cargar datos FireBase
    private FirebaseStorage imgLocal;
    private DatabaseReference dbLocal;
    private LocalModel localModel;
    private ComentariosModel comentariosModel;
    private AppDonativosModel appDonativosModel;
    private DonativoModel donativoModel;
    private CoordenadasModel coordenadasModel;
    private DireccionModel direccionModel;

    //Adapter
    private ComentariosAdapter adapterComentario;
    private TipoLocalAdapter adapterTipoLocal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idLocal = getIntent().getExtras().getString(Configuraciones.ID_LOCAL); //Id del local que traemos con el intent
        Log.d("TAG", "onCreate: "+idLocal);
        localModel = new LocalModel();

        //Instanciar adapters
        adapterTipoLocal = new TipoLocalAdapter(this, localModel.getTipoLocal(), R.layout.adapter_tipo_local);
        binding.rvTipoLocal.setLayoutManager(new GridLayoutManager(this, 4));
        binding.rvTipoLocal.setAdapter(adapterTipoLocal);

        adapterComentario = new ComentariosAdapter(this, localModel.getComentariosLocal(), R.layout.adapter_comentarios_local);
        binding.rvListaComentarios.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListaComentarios.setAdapter(adapterComentario);

        //Descargar Datos Firebase
        dbLocal = FirebaseDatabase.getInstance().getReference("Locales_SM").child(idLocal);
        dbLocal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                comentariosModel = new ComentariosModel();
                appDonativosModel = new AppDonativosModel();
                donativoModel = new DonativoModel();
                appDonativosModel = new AppDonativosModel();
                direccionModel = new DireccionModel();
                coordenadasModel = new CoordenadasModel();

                localModel.setNombreLocal(snapshot.child("nombre").getValue().toString());
                localModel.setTelefonoLocal(snapshot.child("telefono").getValue().toString());
                localModel.setMenuLocal(snapshot.child("menu").getValue().toString());
                localModel.setDescripcionLocal(snapshot.child("descripcionLocal").getValue().toString());
                localModel.setEmailLocal(snapshot.child("emailLocal").getValue().toString());
                //Traer estado donativos + hacer comparacion

                if (Boolean.parseBoolean(snapshot.child("donativos").child("estado").getValue().toString())) {
                    binding.lyDonativos.setVisibility(View.VISIBLE);
                    donativoModel.setEstado(Boolean.parseBoolean(snapshot.child("donativos").child("estado").getValue().toString()));

                    for (int m = 0; m < snapshot.child("donativos").child("opciones").getChildrenCount(); m++) {
                        appDonativosModel.setApp(snapshot.child("donativos").child("opciones").child(m + "").child("appDonativos").getValue().toString());
                        appDonativosModel.setUser(snapshot.child("donativos").child("opciones").child(m + "").child("id_user").getValue().toString());
                        donativoModel.getOpciones().add(appDonativosModel);
                    }

                    localModel.getListaDonativos().add(donativoModel);
                }

                //Añadimos local
                coordenadasModel.setLatitud(snapshot.child("direccionLocal").child("cordenadas").child("lat").getValue().toString());
                coordenadasModel.setLongitud(snapshot.child("direccionLocal").child("cordenadas").child("lon").getValue().toString());

                direccionModel.setCoordenadas(coordenadasModel);
                direccionModel.setDireccion(snapshot.child("direccionLocal").child("direccion").getValue().toString());
                localModel.setDireccionLocal(direccionModel);

                for (int j = 0; j < snapshot.child("tipoLocal").getChildrenCount(); j++) {
                    localModel.getTipoLocal().add(snapshot.child("tipoLocal").child(j + "").getValue().toString());
                }

                for (int k = 0; k < snapshot.child("comentarios").getChildrenCount(); k++) {

                    comentariosModel.setEmail(snapshot.child("comentarios").child(k + "").child("email").getValue().toString());
                    comentariosModel.setComentario(snapshot.child("comentarios").child(k + "").child("comentario").getValue().toString());
                    comentariosModel.setPuntuacion(Integer.parseInt(snapshot.child("comentarios").child(k + "").
                            child("puntuacion").getValue().toString()));

                    localModel.getComentariosLocal().add(comentariosModel);
                }

                //Calculamos puntuacion
                putuacionBar = Configuraciones.calcularPuntuacion(snapshot.child("comentarios"));

                for (int l = 0; l < snapshot.child("imgLocal").getChildrenCount(); l++) {
                    localModel.getImgLocal().add(snapshot.child("imgLocal").child(l + "").getValue().toString());
                }

                for (int o = 0; o < snapshot.child("horario").getChildrenCount(); o++) {
                    localModel.getHorarios().add(snapshot.child("horario").child(o + "").getValue().toString());
                }

                adapterTipoLocal.notifyDataSetChanged();
                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Configuramos Toolbar
        setSupportActionBar(binding.toolbar3);
        binding.toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        introducirDatosLocal();

        //Configurar Imgbtn Deslegables + mostrar info
        binding.imgbtnDesplegarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horariosDesplegados = CambairImgButton(binding.imgbtnDesplegarHorarios, horariosDesplegados);
                //cargar datos
            }
        });

        binding.imgbtnComentariosDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentariosDesplegados = CambairImgButton(binding.imgbtnDesplegarHorarios, comentariosDesplegados);
                //Cargar datos
            }
        });

        binding.btnMenuLocalIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocalIndividual.this, MenuViewLocalIndividual.class));
            }
        });
        binding.btnGaleriaLocalIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocalIndividual.this, GaleriaLocalIndividual.class));
            }
        });
        binding.btnWebLocalIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocalIndividual.this, WebLocalIndividual.class));
            }
        });

    }

    //Metemos info en componentes
    private void introducirDatosLocal() {

        binding.rbPuntuacionLocalIndividual.setRating(putuacionBar);
        binding.txtDireccionLocalIndividual.setText(localModel.getDireccionLocal().getDireccion());
        binding.toolbar3.setTitle(localModel.getNombreLocal());
        binding.txtEmailLocalIndividual.setText(localModel.getEmailLocal());
        binding.txtNumeroTelefonoLocal.setText(localModel.getTelefonoLocal());

        binding.txtLunesHorarioIndividual.setText(localModel.getHorarios().get(0));
        binding.txtMartesHorarioIndividual.setText(localModel.getHorarios().get(1));
        binding.txtMiercolesHorarioIndividual.setText(localModel.getHorarios().get(2));
        binding.txtJuevesHorarioIndividual.setText(localModel.getHorarios().get(3));
        binding.txtViernesHorarioIndividual.setText(localModel.getHorarios().get(4));
        binding.txtSabadoHorarioIndividual.setText(localModel.getHorarios().get(5));
        binding.txtDomingoHorarioIndividual.setText(localModel.getHorarios().get(6));

        StorageReference refImg = imgLocal.getReferenceFromUrl(localModel.getImgLocal().get(0));
        refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                binding.imgLocalIndividual.setImageURI(uri);
            }
        });
    }

    private boolean CambairImgButton(ImageButton imgButton, Boolean desplegado) {
        if (desplegado) {
            imgButton.setImageResource(R.drawable.ic_arrow_down);
            return false;

        }

        imgButton.setImageResource(R.drawable.ic_arrow_up);

        return true;
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
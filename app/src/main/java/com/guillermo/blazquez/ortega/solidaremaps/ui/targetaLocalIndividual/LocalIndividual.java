package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.ComentariosAdapter;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.TipoLocalAdapter;
import com.squareup.picasso.Picasso;

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
        Log.d("TAG", "onCreate: " + idLocal);
        localModel = new LocalModel();

        //Instanciar adapters
        adapterTipoLocal = new TipoLocalAdapter(this, localModel.getTipoLocal(), R.layout.adapter_tipo_local);
        binding.rvTipoLocal.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvTipoLocal.setAdapter(adapterTipoLocal);

        adapterComentario = new ComentariosAdapter(this, localModel.getComentariosLocal(), R.layout.adapter_comentarios_local);
        binding.rvListaComentarios.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListaComentarios.setAdapter(adapterComentario);

        //Descargar Datos Firebase
        dbLocal = FirebaseDatabase.getInstance().getReference("Locales_SM").child(idLocal);
        dbLocal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                appDonativosModel = new AppDonativosModel();
                donativoModel = new DonativoModel();
                direccionModel = new DireccionModel();
                coordenadasModel = new CoordenadasModel();

                localModel.setNombreLocal(snapshot.child("nombre").getValue().toString());
                localModel.setTelefonoLocal(snapshot.child("telefono").getValue().toString());
                localModel.setMenuLocal(snapshot.child("menu").getValue().toString());
                localModel.setDescripcionLocal(snapshot.child("descripcionLocal").getValue().toString());
                localModel.setEmailLocal(snapshot.child("emailLocal").getValue().toString());
                //Traer estado donativos + hacer comparacion

                if (Boolean.parseBoolean(snapshot.child("donativos").child("estado").getValue().toString())) {
                    binding.btnDonarLocalIndividual.setVisibility(View.VISIBLE);
                    donativoModel.setEstado(Boolean.parseBoolean(snapshot.child("donativos").child("estado").getValue().toString()));

                    for (int m = 0; m < snapshot.child("donativos").child("opciones").getChildrenCount(); m++) {
                        appDonativosModel = new AppDonativosModel();

                        appDonativosModel.setApp(snapshot.child("donativos").child("opciones").child(m + "").child("appDonativos").getValue().toString());
                        appDonativosModel.setUser(snapshot.child("donativos").child("opciones").child(m + "").child("id_user").getValue().toString());
                        donativoModel.setOpciones(appDonativosModel);
                    }

                    localModel.setListaDonativos(donativoModel);
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


                //Calculamos puntuacion
                putuacionBar = Configuraciones.calcularPuntuacion(snapshot.child("comentarios"));

                for (int l = 0; l < snapshot.child("imgLocal").getChildrenCount(); l++) {
                    localModel.setImgLocal(snapshot.child("imgLocal").child(l + "").getValue().toString());
                }

                for (int o = 0; o < snapshot.child("horario").getChildrenCount(); o++) {
                    localModel.setHorarios(snapshot.child("horario").child(o + "").getValue().toString());
                }

                introducirDatosLocal();

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

        horariosDesplegados = false;
        comentariosDesplegados = false;

        //Configurar Imgbtn Deslegables + mostrar info
        binding.imgbtnDesplegarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horariosDesplegados = CambairImgButton(binding.imgbtnDesplegarHorarios, horariosDesplegados);

                if (horariosDesplegados) {
                    binding.lyHorariosDesplegada.setVisibility(View.VISIBLE);
                } else {
                    binding.lyHorariosDesplegada.setVisibility(View.GONE);
                }
                //cargar datos
            }
        });

        binding.imgbtnComentariosDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentariosDesplegados = CambairImgButton(binding.imgbtnComentariosDesplegar, comentariosDesplegados);

                if (comentariosDesplegados) {
                    binding.lyComentarios.setVisibility(View.VISIBLE);
                    cargarComentariosLocal();
                } else {
                    binding.lyComentarios.setVisibility(View.GONE);
                }

            }
        });

        //Buttons de navegacion
        binding.btnDonarLocalIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Bundle bundle = new Bundle();
                bundle.putParcelable(Configuraciones.PASAR_MODEL_GALERIA, localModel);
                startActivity(new Intent(LocalIndividual.this, GaleriaLocalIndividual.class).putExtras(bundle));
            }
        });
        binding.btnWebLocalIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocalIndividual.this, WebLocalIndividual.class));
            }
        });

        //ImageButtons acciones
        binding.imgbtnBuscarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Direccion local", localModel.getDireccionLocal().getDireccion());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LocalIndividual.this, "Se ha copiado la direccion del local", Toast.LENGTH_SHORT).show();

            }
        }); // -- FALTA DISEÑAR ACCIONES
        binding.imgbntLlamarNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    llamadaAction(localModel.getTelefonoLocal().toString());
                } else {
                    if (ContextCompat.checkSelfPermission(LocalIndividual.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        llamadaAction(localModel.getTelefonoLocal());
                    } else {
                        ActivityCompat.requestPermissions(LocalIndividual.this, new String[]{Manifest.permission.CALL_PHONE}, Configuraciones.LLAMADA_PERMISION);
                    }
                }
            }
        });
        binding.imgbtnEscribirCorreoIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Direccion Email", localModel.getEmailLocal().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LocalIndividual.this, "Se ha copiado la direccion email en el portapapel", Toast.LENGTH_SHORT).show();
            }
        });

        //Comentario nuevo
        binding.btnPublicarOpinionLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtEscribirComentarioLocal.getText().toString().isEmpty()) {
                    //publicarComentario(dbLocal, binding.txtEscribirComentarioLocal.getText().toString(), binding.rbPuntuarLocalndicidual.getRating());
                    Toast.makeText(LocalIndividual.this, "Tu comentario ha sido publicado", Toast.LENGTH_SHORT).show();
                }
            }
        }); //ARREGLAR

    }

    //Metemos info en componentes
    private void introducirDatosLocal() {

        binding.rbPuntuacionLocalIndividual.setRating(putuacionBar);
        binding.txtDireccionLocalIndividual.setText(localModel.getDireccionLocal().getDireccion());
        binding.toolbar3.setTitle(localModel.getNombreLocal());
        binding.txtEmailLocalIndividual.setText(localModel.getEmailLocal());
        binding.txtNumeroTelefonoLocal.setText(localModel.getTelefonoLocal());
        binding.txtDescripcionLocalIndividual.setText(localModel.getDescripcionLocal());

        binding.txtLunesHorarioIndividual.setText(localModel.getHorarios().get(0));
        binding.txtMartesHorarioIndividual.setText(localModel.getHorarios().get(1));
        binding.txtMiercolesHorarioIndividual.setText(localModel.getHorarios().get(2));
        binding.txtJuevesHorarioIndividual.setText(localModel.getHorarios().get(3));
        binding.txtViernesHorarioIndividual.setText(localModel.getHorarios().get(4));
        binding.txtSabadoHorarioIndividual.setText(localModel.getHorarios().get(5));
        binding.txtDomingoHorarioIndividual.setText(localModel.getHorarios().get(6));

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(localModel.getImgLocal().get(0));
        //StorageReference refImg = imgLocal.getReferenceFromUrl(localModel.getImgLocal().get(0));
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(binding.imgLocalIndividual);
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

    //Acciones con permisos
    private void llamadaAction(String telefonoLocal) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + telefonoLocal));
        startActivity(intent);
    }

    //Comentarios
    private void cargarComentariosLocal() {
        localModel.getComentariosLocal().clear();

        dbLocal.child("comentarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    comentariosModel = new ComentariosModel();

                    comentariosModel.setEmail(snapshot.child(i + "").child("email").getValue().toString());
                    comentariosModel.setComentario(snapshot.child(i + "").child("comentario").getValue().toString());
                    comentariosModel.setPuntuacion(Float.parseFloat(snapshot.child(i + "").child("puntuacion").getValue().toString()));

                    localModel.setComentariosLocal(comentariosModel);
                }
                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    /*private void publicarComentario(DatabaseReference dbLocal, String comentario, float rating) {

        dbLocal.child("comentarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               String c = String.valueOf(snapshot.getChildrenCount());
                dbLocal.child("comentarios").child(c).child("comentario").setValue(comentario);
                dbLocal.child("comentarios").child(c).child("email").setValue(Configuraciones.firebaseUser.getEmail());
                dbLocal.child("comentarios").child(c).child("puntuacion").setValue(rating);

                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        cargarComentariosLocal();

    }*/ //ARREGLAR Insertar Comentario

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Configuraciones.LLAMADA_PERMISION) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // HAS AUTORIZADO
                llamadaAction(localModel.getTelefonoLocal().toString());
            } else {
                Toast.makeText(this, "Se requieren permisos para poder continuar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
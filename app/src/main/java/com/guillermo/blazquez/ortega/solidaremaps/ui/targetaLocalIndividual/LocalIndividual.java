package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    //Cargar datos FireBase
    private DatabaseReference dbLocal;
    private LocalModel localModel;
    private ComentariosModel comentariosModel;
    private AppDonativosModel appDonativosModel;
    private DonativoModel donativoModel;
    private CoordenadasModel coordenadasModel;
    private DireccionModel direccionModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocalIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idLocal=getIntent().getExtras().getString(Configuraciones.ID_LOCAL); //Id del local que traemos con el intent
        localModel=new LocalModel();

        //Descargar Datos Firebase
        dbLocal = FirebaseDatabase.getInstance().getReference("Locales_SM");
        dbLocal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                comentariosModel = new ComentariosModel();
                appDonativosModel = new AppDonativosModel();
                donativoModel = new DonativoModel();
                appDonativosModel = new AppDonativosModel();
                direccionModel = new DireccionModel();
                coordenadasModel = new CoordenadasModel();


                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    if (snapshot.child(""+i).getValue().toString().equals(idLocal)) {
                        localModel.setNombreLocal(snapshot.child(""+i).child("nombre").getValue().toString());
                        localModel.setTelefonoLocal(Integer.parseInt(snapshot.child(""+i).child("telefono").getValue().toString()));
                        localModel.setMenuLocal(snapshot.child(""+i).child("menu").getValue().toString());
                        localModel.setDescripcionLocal(snapshot.child(""+i).child("descripcionLocal").getValue().toString());

                        //Traer estado donativos + hacer comparacion

                        if (Boolean.parseBoolean(snapshot.child(""+i).child("donativos").child("estado").getValue().toString())) {
                            binding.lyDonativos.setVisibility(View.VISIBLE);
                            donativoModel.setEstado(Boolean.parseBoolean(snapshot.child(""+i).child("donativos").child("estado").getValue().toString()));

                            for (int m = 0; m < snapshot.child(""+i).child("donativos").child("opciones").getChildrenCount(); m++) {
                                appDonativosModel.setApp(snapshot.child(""+i).child("donativos").child("opciones").child(m+"").child("appDonativos").getValue().toString());
                                appDonativosModel.setUser(snapshot.child(""+i).child("donativos").child("opciones").child(m+"").child("id_user").getValue().toString());
                                donativoModel.getOpciones().add(appDonativosModel);
                            }

                            localModel.getListaDonativos().add(donativoModel);
                        }

                        //Añadimos local
                        coordenadasModel.setLatitud(snapshot.child(""+i).child("direccionLocal").child("cordenadas").child("lat").getValue().toString());
                        coordenadasModel.setLongitud(snapshot.child(""+i).child("direccionLocal").child("cordenadas").child("lon").getValue().toString());

                        direccionModel.setCoordenadas(coordenadasModel);
                        direccionModel.setDireccion(snapshot.child(""+i).child("direccionLocal").child("direccion").getValue().toString());
                        localModel.setDireccionLocal(direccionModel);

                        for (int j = 0; j < snapshot.child(""+i).child("tipoLocal").getChildrenCount(); j++) {
                           localModel.getTipoLocal().add(snapshot.child(""+i).child("tipoLocal").child(j+"").getValue().toString());
                        }

                        for (int k = 0; k < snapshot.child(""+i).child("comentarios").getChildrenCount(); k++) {

                            comentariosModel.setEmail(snapshot.child(""+i).child("comentarios").child(k+"").child("email").getValue().toString());
                            comentariosModel.setComentario(snapshot.child(""+i).child("comentarios").child(k+"").child("comentario").getValue().toString());
                            comentariosModel.setPuntuacion(Integer.parseInt(snapshot.child(""+i).child("comentarios").child(k+"").
                                    child("puntuacion").getValue().toString()));

                            localModel.getComentariosLocal().add(comentariosModel);
                        }

                        for (int l = 0; l < snapshot.child(""+i).child("imgLocal").getChildrenCount(); l++) {
                            localModel.getImgLocal().add(snapshot.child(""+i).child("imgLocal").child(l+"").getValue().toString());
                        }

                        for (int o = 0; o < snapshot.child(""+i).child("horario").getChildrenCount(); o++) {
                            localModel.getHorariosLocal().add(snapshot.child(""+i).child("horario").child(o+"").getValue().toString());
                        }

                        break;
                    }
                }
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
package com.guillermo.blazquez.ortega.solidaremaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.databinding.ModificarInfoUserBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Modificar_Info_User extends AppCompatActivity {

    private ModificarInfoUserBinding binding;

    //Real time database
    private FirebaseDatabase database;
    private DatabaseReference userInfo;

    //Img Stroage Fire base
    private StorageReference imgReferenceFirebase;

    //Foto
    private String direccionUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModificarInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        imgReferenceFirebase = FirebaseStorage.getInstance().getReference(Configuraciones.StorageImgsFirebase).
                child(Configuraciones.firebaseUser.getUid());

        setSupportActionBar(binding.toolbar2);

        CargarDatosIniciales();

        binding.toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imUserPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogOpciones();
            }
        });
    }


    private void CargarDatosIniciales() {
        //Sacar info real data
        database.getReference("Users").child(Configuraciones.firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.txtNombreUserPerfil.setText(snapshot.child("nombre").getValue().toString());
                binding.txtEmailUserPerfil.setText(Configuraciones.firebaseUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Sacer info de la imagen

       /* imgReferenceFirebase.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

               binding.imUserPerfil.setImageURI(Uri.parse(task.getResult().toString()));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.imUserPerfil.setImageResource(R.drawable.user1);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pefil_user, menu);
        menu.findItem(R.id.itemAceptarCambionPerfil);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemAceptarCambionPerfil) {
            CambiarDatos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void CambiarDatos() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("¿Estas completamente Seguro?");
        dialog.setMessage("Los datos van a ser modificados de forma definitiva. Comprueba que la informacion introduccida en los campos es correcta.");

        dialog.setNegativeButton("Cancelar", null);
        dialog.setPositiveButton("Modificar Datos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Modificar_Info_User.this, "Datos Modificados", Toast.LENGTH_SHORT).show();
               // updateUserInformation();
            }
        });
        dialog.show();
    }

    private void updateUserInformation() {
        //1- Comporbamos que este toda a info del user y no cambios vacios
        if (CamposVacios()) {
            userInfo = database.getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("nombre");
            userInfo.setValue(binding.txtNombreUserPerfil.getText().toString());

            userInfo = database.getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("email");
            userInfo.setValue(binding.txtEmailUserPerfil.getText().toString());

            if (ContrasenyasVacias()) {
                userInfo = database.getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("password");
                if (binding.txtAntiguaContrasenyaPerfil.getText().toString().equals(userInfo.toString())) {
                    if (binding.txtNuevaContrasenyaPerfil.getText().toString().equals(
                            binding.txtRepetirNuevaContrasenyaPerfil.getText().toString())) {

                        Configuraciones.firebaseUser.updatePassword(binding.txtNuevaContrasenyaPerfil.getText().toString());
                        userInfo.setValue(binding.txtNuevaContrasenyaPerfil.getText().toString());

                    }else{
                        Toast.makeText(this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "La contraseña no es correcta, prueba de nuevo", Toast.LENGTH_SHORT).show();
                }

            }

            //2- Comprobar si ha insertado imagen
            // SI---> Subir imagen al storage de la nube
            //No dejar vacio


            Configuraciones.firebaseUser.updateEmail(binding.txtEmailUserPerfil.getText().toString());
        }else{
            Toast.makeText(this, "Comprueba que los campos no esten vacios", Toast.LENGTH_SHORT).show();
        }




        Toast.makeText(this, "La informacion se ha guradado", Toast.LENGTH_SHORT).show();
    } //Actualizamos la info del user insertando la info de los campos

    private boolean ContrasenyasVacias() {
        return !binding.txtAntiguaContrasenyaPerfil.getText().toString().isEmpty() &&
        !binding.txtNuevaContrasenyaPerfil.getText().toString().isEmpty() &&
        !binding.txtRepetirNuevaContrasenyaPerfil.getText().toString().isEmpty();
    }

    private boolean CamposVacios() {
        return !binding.txtNombreUserPerfil.getText().toString().isEmpty() &&
        !binding.txtEmailUserPerfil.getText().toString().isEmpty();
    }

    //Apartado Camara - Storage

    private void DialogOpciones() {
        AlertDialog.Builder fotos  = new AlertDialog.Builder(this);
        fotos.setTitle("Elige una opcion");
        fotos.setMessage("Elige de donde quieres obtener la imagen");

        fotos.setPositiveButton("Abrir Camara", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    AbrirCamara();
                }
                else {
                    if (ContextCompat.checkSelfPermission(Modificar_Info_User.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(Modificar_Info_User.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED ) {
                        AbrirCamara();
                    }
                    else {
                        String[] permisos = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(Modificar_Info_User.this, permisos, Configuraciones.CAMARA_PERMISO);
                    }
                }
            }
        });

        fotos.setNeutralButton("Abrir Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    SeleccionarGaleria();
                }else{
                    if (ContextCompat.checkSelfPermission(Modificar_Info_User.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED  ) {
                        SeleccionarGaleria();
                    }else{
                        ActivityCompat.requestPermissions(Modificar_Info_User.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Configuraciones.GALERIA_PERMISION);
                    }
                }
            }
        });
        fotos.show();
    }

    private void AbrirCamara() {
        try {
            File photoFile = createImageFile();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photURI = FileProvider.getUriForFile(Modificar_Info_User.this,
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
        direccionUri = imagen.getAbsolutePath();
        return imagen;
    }

    private void SeleccionarGaleria() {
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, Configuraciones.GALERIA_ACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Configuraciones.CAMERA_ACTION && resultCode == RESULT_OK && data != null) {
            binding.imUserPerfil.setImageURI(Uri.parse(direccionUri));

        }

        if (requestCode == Configuraciones.GALERIA_ACTION && resultCode== RESULT_OK && data!=null) {
            direccionUri = data.getData().toString();
            binding.imUserPerfil.setImageURI(data.getData());
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Configuraciones.CAMARA_PERMISO){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                AbrirCamara();
            }
            else {
                Toast.makeText(this, "Se requieren permisos para realizazar esta accion", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == Configuraciones.CAMARA_PERMISO) {
            if (permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SeleccionarGaleria();
            }else{
                Toast.makeText(this, "Se requieren permisos para realizazar esta accion", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
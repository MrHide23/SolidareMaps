package com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;

import java.util.ArrayList;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritosVH>{
    private ArrayList<String> lista;
    private int resource;
    private Context context;

    //Fire base
    private FirebaseDatabase infoFavs;
    private DatabaseReference ref;

    //Storage foto
    private FirebaseStorage imgLocalStorge;

    public FavoritosAdapter(ArrayList<String> lista, int resource, Context context) {
        this.lista = lista;
        this.resource = resource;
        this.context = context;

        infoFavs = FirebaseDatabase.getInstance();
        imgLocalStorge = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public FavoritosVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View favoritos = LayoutInflater.from(context).inflate(resource, null);
        favoritos.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        FavoritosVH favoritosVH = new FavoritosVH(favoritos);
        return favoritosVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosVH holder, int position) {
        ref = infoFavs.getReference("Locales_SM").child(lista.get(position).toString());

        //No funcion dado que no existe las referencias, todavia
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.txtTitulo.setText(snapshot.child("nombreLocal").getValue().toString());
                holder.txtDireccion.setText(snapshot.child("direccionLocal").getValue().toString());
                holder.rbPuntuacion.setRating(Configuraciones.calcularPuntuacion(snapshot.child("comentarios")));

                StorageReference refImg = imgLocalStorge.getReferenceFromUrl(snapshot.child("imgLocal").child("0").getValue().toString());
                refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        holder.imgFotoLocal.setImageURI(uri);
                    }
                });

                Log.d("Ha enrado ", "ii--> "+position);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cambiar color boton
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar informacion en Pagina Locales
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class FavoritosVH extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtDireccion;
        RatingBar rbPuntuacion;
        ImageView imgFotoLocal;
        ImageButton btnFavorito;

        public FavoritosVH(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTituloAdapter);
            txtDireccion = itemView.findViewById(R.id.txtDireccionAdapter);
            rbPuntuacion = itemView.findViewById(R.id.rbPuntuacionAdapter);
            imgFotoLocal = itemView.findViewById(R.id.imgFotoLocalAdapter);
            btnFavorito = itemView.findViewById(R.id.btnImgFavoritoAdapter);
        }
    }
}

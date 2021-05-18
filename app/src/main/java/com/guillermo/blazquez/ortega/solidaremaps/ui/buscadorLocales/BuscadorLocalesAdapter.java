package com.guillermo.blazquez.ortega.solidaremaps.ui.buscadorLocales;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.LocalIndividual;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BuscadorLocalesAdapter extends RecyclerView.Adapter<BuscadorLocalesAdapter.BuscadorVH> {
    private ArrayList<String> list;
    private int resource;
    private Context context;

    public BuscadorLocalesAdapter(int resource, ArrayList<String> list, Context context) {
        this.list = list;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public BuscadorVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View buscador = LayoutInflater.from(parent.getContext()).inflate(resource, null);
        buscador.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        BuscadorVH buscadorVH = new BuscadorVH(buscador);
        return buscadorVH;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BuscadorVH holder, int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Locales_SM").child(list.get(position).toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                holder.txtTitulo.setText(snapshot.child("nombre").getValue().toString());
                holder.txtDireccion.setText(snapshot.child("direccionLocal").child("direccion").getValue().toString());
                holder.rbPuntuacion.setRating(Configuraciones.calcularPuntuacion(snapshot.child("comentarios")));

                StorageReference refImg = FirebaseStorage.getInstance().getReferenceFromUrl(snapshot.child("imgLocal").child("0").getValue().toString());
                refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        holder.imgLocal.setImageURI(uri);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Maracar en rojo si es fav
        DatabaseReference refFav = FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()).child("favoritos");
        refFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    if (list.get(position).equals(i+"")) {
                        holder.btnFavorito.setImageResource(R.drawable.ic_favoritos);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Si pulsa btnCorazon meter o eliminar de lista de favoritos

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar informacion en Pagina Locales
                Bundle bundle = new Bundle();
                bundle.putString(Configuraciones.ID_LOCAL, list.get(position).toString());
                context.startActivity(new Intent(context, LocalIndividual.class).putExtras(bundle));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BuscadorVH extends RecyclerView.ViewHolder {
        private TextView txtTitulo, txtDireccion;
        private ImageView imgLocal;
        private RatingBar rbPuntuacion;
        private ImageButton btnFavorito;

        public BuscadorVH(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloAdapter);
            txtDireccion = itemView.findViewById(R.id.txtDireccionAdapter);
            imgLocal = itemView.findViewById(R.id.imgFotoLocalAdapter);
            rbPuntuacion = itemView.findViewById(R.id.rbPuntuacionAdapter);
            btnFavorito = itemView.findViewById(R.id.btnimgFavoritosAdapter);
        }
    }
}

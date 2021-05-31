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
import com.guillermo.blazquez.ortega.solidaremaps.Models.TargetaLocalPrevisualizacionModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.LocalIndividual;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BuscadorLocalesAdapter extends RecyclerView.Adapter<BuscadorLocalesAdapter.BuscadorVH> {
    private ArrayList<TargetaLocalPrevisualizacionModel> list;
    private int resource;
    private Context context;

    public BuscadorLocalesAdapter(int resource, ArrayList<TargetaLocalPrevisualizacionModel> list, Context context) {
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
        ArrayList<String> listaFavs = new ArrayList<>();

        holder.txtTitulo.setText(list.get(position).getNombreLocal());
        holder.txtDireccion.setText(list.get(position).getDireccionLocal());
        holder.rbPuntuacion.setRating(list.get(position).getPuntuacionLocal());

        if (list.get(position).getImagenesLocal().size()>0) {
            StorageReference refImg = FirebaseStorage.getInstance().getReferenceFromUrl(list.get(position).getImagenesLocal().get(0));
            refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.imgLocal);
                }
            });
        }


        //Maracar en rojo si es fav
        DatabaseReference refFav = FirebaseDatabase.getInstance().getReference("Users").child(Configuraciones.firebaseUser.getUid()).
                child("favoritos");
        refFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listaFavs.clear();

                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    if (snapshot.child(i+"").getValue().toString().equals(position+"")) {
                        holder.btnFavorito.setImageResource(R.drawable.ic_corazon_rojo);

                    }

                    listaFavs.add(snapshot.child(i+"").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Si pulsa btnCorazon meter o eliminar de lista de favoritos
        holder.btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean existe = false;
                for (int i = 0; i < listaFavs.size(); i++) {
                    if(listaFavs.get(i).equals(""+position)){
                        listaFavs.remove(i);
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    holder.btnFavorito.setImageResource(R.drawable.ic_corozon_no_rojo);
                }else{
                    listaFavs.add(position+"");
                    holder.btnFavorito.setImageResource(R.drawable.ic_corazon_rojo);
                }

                refFav.removeValue();
                refFav.setValue(listaFavs);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar informacion en Pagina Locales
                Bundle bundle = new Bundle();
                bundle.putString(Configuraciones.ID_LOCAL, String.valueOf(position));
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

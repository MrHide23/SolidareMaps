package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guillermo.blazquez.ortega.solidaremaps.R;

import java.util.ArrayList;

public class MiLocalAdapter extends RecyclerView.Adapter<MiLocalAdapter.MiLocalVH> {

    private ArrayList<String> lista;
    private int resource;
    private Context context;

    public MiLocalAdapter(ArrayList<String> lista, int resource, Context context) {
        this.lista = lista;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public MiLocalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mi_local = LayoutInflater.from(parent.getContext()).inflate(resource, null);
        mi_local.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MiLocalVH miLocalVH = new MiLocalVH(mi_local);
        return miLocalVH;
    }

    @Override
    public void onBindViewHolder(@NonNull MiLocalVH holder, int position) {
        DatabaseReference refLocal = FirebaseDatabase.getInstance().getReference("Locales_SM");


            refLocal.child(lista.get(position).toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                    holder.txtNombre.setText(snapshot.child("nombreLocal").getValue().toString()+"");
                    holder.txtDireccion.setText(snapshot.child("direccionLocal").getValue().toString());

                    StorageReference refImg = FirebaseStorage.getInstance().
                            getReferenceFromUrl(snapshot.child("imgLocal").child("0").getValue().toString());

                    refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            holder.imgLocal.setImageURI(uri);
                        }});

                    }catch (NullPointerException n){
                        Log.e("TAG", "error al cargar la lista. Datos incorrectos: " + n.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ID_LOCAL", lista.get(position));
                    //context.startActivity(new Intent(context, MiNewLocal.class).putExtras(bundle));
                }
            });
            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eliminar de Locales_SM
                refLocal.child(lista.get(position).toString()).removeValue();

                //Eliminar de lista MiLocal user
                lista.remove(position);

                DatabaseReference refFavs = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).
                        child("miLocal");
                refFavs.getRef().removeValue();

                for (int i = 0; i < lista.size(); i++) {
                    refFavs.child(String.valueOf(i)).setValue(lista.get(i).toString());
                }

                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MiLocalVH extends RecyclerView.ViewHolder {
        ImageView imgLocal;
        TextView txtNombre, txtDireccion;
        ImageButton btnEliminar, btnEditar;

        public MiLocalVH(@NonNull View itemView) {
            super(itemView);
            imgLocal = itemView.findViewById(R.id.imgLocalMiLocal);
            txtNombre = itemView.findViewById(R.id.txtNombreMiLocal);
            txtDireccion = itemView.findViewById(R.id.txtDireccionMiLocal);
            btnEditar = itemView.findViewById(R.id.btnEditarMiLocal);
            btnEliminar = itemView.findViewById(R.id.btnEliminarMiLocal);
        }
    }
}

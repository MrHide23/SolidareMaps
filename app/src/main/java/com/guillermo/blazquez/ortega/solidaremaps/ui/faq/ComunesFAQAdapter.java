package com.guillermo.blazquez.ortega.solidaremaps.ui.faq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.favoritos.FavoritosAdapter;

import java.util.ArrayList;

public class ComunesFAQAdapter extends RecyclerView.Adapter<ComunesFAQAdapter.ComunesVH>{

    private ArrayList<String> lista;
    private int resource;
    private Context context;

    //Firebase
    private DatabaseReference refFAQ;

    public ComunesFAQAdapter(ArrayList<String> lista, int resource, Context context) {
        this.lista = lista;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public ComunesFAQAdapter.ComunesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View faqComunes = LayoutInflater.from(parent.getContext()).inflate(resource, null);
        faqComunes.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ComunesVH favoritosVH = new ComunesVH(faqComunes);
        return favoritosVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ComunesFAQAdapter.ComunesVH holder, int position) {
        refFAQ = FirebaseDatabase.getInstance().getReference("FAQ");
        refFAQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.txtTitulo.setText(snapshot.child(position+"").child("titulo").getValue().toString());
                holder.txtContenido.setText(snapshot.child(position+"").child("contenido").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Configuraciones.FAQPregunta, position+"");
                context.startActivity(new Intent(context, VerPreguntaFAQ.class).putExtras(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ComunesVH extends RecyclerView.ViewHolder {

        private TextView txtTitulo, txtContenido;
        public ComunesVH(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloCardAdapterFAQ);
            txtContenido = itemView.findViewById(R.id.txtContenidoCardAdapterFAQ);
        }
    }
}

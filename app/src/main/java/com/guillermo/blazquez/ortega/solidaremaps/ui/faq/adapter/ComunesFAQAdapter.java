package com.guillermo.blazquez.ortega.solidaremaps.ui.faq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.Configuracion.Configuraciones;
import com.guillermo.blazquez.ortega.solidaremaps.Models.FAQModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.faq.VerPreguntaFAQ;

import java.util.ArrayList;

public class ComunesFAQAdapter extends RecyclerView.Adapter<ComunesFAQAdapter.ComunesVH>{

    private ArrayList<FAQModel> lista;
    private int resource;
    private Context context;


    public ComunesFAQAdapter(ArrayList<FAQModel> lista, int resource, Context context) {
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
        holder.txtTitulo.setText(lista.get(position).getTitulo());
        holder.txtContenido.setText(lista.get(position).getContenido());

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

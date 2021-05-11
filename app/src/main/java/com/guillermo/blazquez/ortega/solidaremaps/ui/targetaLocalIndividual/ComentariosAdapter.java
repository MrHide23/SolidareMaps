package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.Models.ComentariosModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentarioVH>{
    private Context context;
    private ArrayList<ComentariosModel> list;
    private int resource;

    public ComentariosAdapter(Context context, ArrayList<ComentariosModel> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NotNull
    @Override
    public ComentariosAdapter.ComentarioVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View comentario = LayoutInflater.from(context).inflate(resource, null);
        comentario.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ComentarioVH tipoLocal = new ComentarioVH(comentario);
        return tipoLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComentariosAdapter.ComentarioVH holder, int position) {
        holder.txtEmail.setText(list.get(position).getEmail());
        holder.txtComentario.setText(list.get(position).getComentario());
        holder.rbPuntuacion.setRating(list.get(position).getPuntuacion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComentarioVH extends RecyclerView.ViewHolder {
        private TextView txtEmail, txtComentario;
        private RatingBar rbPuntuacion;

        public ComentarioVH(@NonNull @NotNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmailAdapter);
            txtComentario = itemView.findViewById(R.id.txtComentarioAdapter);
            rbPuntuacion = itemView.findViewById(R.id.rbPuntuacionComentarioAdapter);
        }
    }
}

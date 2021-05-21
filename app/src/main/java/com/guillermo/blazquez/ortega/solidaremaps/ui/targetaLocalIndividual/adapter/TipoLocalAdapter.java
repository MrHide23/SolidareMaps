package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TipoLocalAdapter  extends RecyclerView.Adapter<TipoLocalAdapter.TipoLocalVH>{
    private Context context;
    private ArrayList<String> list;
    private int resource;

    public TipoLocalAdapter(Context context, ArrayList<String> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }


    @NotNull
    @Override
    public TipoLocalVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View tipo = LayoutInflater.from(context).inflate(resource, null);
        tipo.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TipoLocalVH tipoLocal = new TipoLocalVH(tipo);
        return tipoLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TipoLocalVH holder, int position) {
        holder.txtTipoLocal.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TipoLocalVH extends RecyclerView.ViewHolder {
        private TextView txtTipoLocal;

        public TipoLocalVH(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTipoLocal = itemView.findViewById(R.id.txtTipoLocalAdapter);
        }
    }
}

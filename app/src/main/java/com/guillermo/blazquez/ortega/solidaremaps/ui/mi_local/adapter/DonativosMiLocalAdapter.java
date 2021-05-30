package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.Models.AppDonativosModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.ComentariosModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter.ComentariosAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DonativosMiLocalAdapter extends RecyclerView.Adapter<DonativosMiLocalAdapter.DonativoVH>{
    private Context context;
    private ArrayList<AppDonativosModel> list;
    private int resource;

    public DonativosMiLocalAdapter(Context context, ArrayList<AppDonativosModel> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NonNull
    @NotNull
    @Override
    public DonativoVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View donativos = LayoutInflater.from(context).inflate(resource, null);
        donativos.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        DonativoVH donativoVH = new DonativoVH(donativos);
        return donativoVH;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DonativoVH holder, int position) {
        holder.txtApp.setText(list.get(position).getApp());
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DonativoVH extends RecyclerView.ViewHolder {

        private TextView txtApp;
        private ImageButton btnEliminar;

        public DonativoVH(@NonNull @NotNull View itemView) {
            super(itemView);
            txtApp = itemView.findViewById(R.id.txtAppDonativoAdapter);
            btnEliminar = itemView.findViewById(R.id.imgbtnEliminiarAdapterMiLocal);

        }
    }
}

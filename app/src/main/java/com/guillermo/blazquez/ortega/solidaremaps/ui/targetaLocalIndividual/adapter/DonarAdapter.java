package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.Models.AppDonativosModel;
import com.guillermo.blazquez.ortega.solidaremaps.Models.DonativoModel;
import com.guillermo.blazquez.ortega.solidaremaps.R;
import com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.LocalIndividual;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DonarAdapter extends RecyclerView.Adapter<DonarAdapter.DonarVH>{
    private Context context;
    private ArrayList<AppDonativosModel> list;
    private int resource;

    public DonarAdapter(Context context, ArrayList<AppDonativosModel> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NonNull
    @NotNull
    @Override
    public DonarVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View donar = LayoutInflater.from(context).inflate(resource, null);
        donar.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        DonarVH donarVH = new DonarVH(donar);

        return donarVH;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DonarVH holder, int position) {
        holder.txtApp.setText(list.get(position).getApp());
        holder.txtID.setText(list.get(position).getUser());
        holder.btnCopiarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Id de "+list.get(position).getApp()+" copiado", list.get(position).getUser());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Id de "+list.get(position).getApp()+" copiado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DonarVH extends RecyclerView.ViewHolder {
        private TextView txtApp, txtID;
        private ImageView btnCopiarID;

        public DonarVH(@NonNull @NotNull View itemView) {
            super(itemView);
            txtApp = itemView.findViewById(R.id.txtAppDonativoAdapter);
            txtID = itemView.findViewById(R.id.txtIdDonativosAdapter);
            btnCopiarID = itemView.findViewById(R.id.imgbtnCopiarIDDonativosAdapter);

        }
    }
}

package com.guillermo.blazquez.ortega.solidaremaps.ui.mi_local.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillermo.blazquez.ortega.solidaremaps.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImagenesLocalAdapter extends RecyclerView.Adapter<ImagenesLocalAdapter.ImagenesVH>{
    private Context context;
    private ArrayList<String> list;
    private int resource;

    public ImagenesLocalAdapter(Context context, ArrayList<String> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NonNull
    @NotNull
    @Override
    public ImagenesVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View imagenes = LayoutInflater.from(context).inflate(resource, null);
        imagenes.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImagenesVH imagenesVH = new ImagenesVH(imagenes);
        return imagenesVH;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImagenesVH holder, int position) {
        holder.imgLocal.setImageURI(Uri.parse(list.get(position)));

        if (!list.get(position).isEmpty()) {
            holder.btnImgLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImagenesVH extends RecyclerView.ViewHolder {
        private ImageView imgLocal;
        private ImageButton btnImgLocal;

        public ImagenesVH(@NonNull @NotNull View itemView) {
            super(itemView);
            imgLocal = itemView.findViewById(R.id.imgInsertarMiLocal);
            imgLocal = itemView.findViewById(R.id.imgbtnEliminarImage);
        }
    }
}

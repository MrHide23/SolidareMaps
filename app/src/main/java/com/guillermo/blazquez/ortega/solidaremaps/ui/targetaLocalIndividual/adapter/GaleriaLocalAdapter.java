package com.guillermo.blazquez.ortega.solidaremaps.ui.targetaLocalIndividual.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GaleriaLocalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> lista;
    private int resource;

    public GaleriaLocalAdapter(Context context, ArrayList<String> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView = new ImageView(context);

        StorageReference refImg = FirebaseStorage.getInstance().getReferenceFromUrl(lista.get(position));
        refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgView.setImageURI(uri);
            }
        });

        return imgView;
    }
}

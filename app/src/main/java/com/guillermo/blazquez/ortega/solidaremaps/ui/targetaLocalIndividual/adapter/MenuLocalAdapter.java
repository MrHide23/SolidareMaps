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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuLocalAdapter extends BaseAdapter {

    private ArrayList<String> listaMenu;
    private Context context;

    public MenuLocalAdapter(Context context, ArrayList<String> listaMenu) {
        this.listaMenu = listaMenu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView = new ImageView(context);

        StorageReference refImg = FirebaseStorage.getInstance().getReferenceFromUrl(listaMenu.get(position));
        refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgView);
            }
        });

        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setLayoutParams(new ViewGroup.LayoutParams(1100, 1800));

        return imgView;
    }
}

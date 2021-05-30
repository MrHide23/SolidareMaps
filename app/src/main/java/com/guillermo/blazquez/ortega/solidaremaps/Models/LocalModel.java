package com.guillermo.blazquez.ortega.solidaremaps.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LocalModel implements Parcelable {

    private ArrayList<String> imgLocal;
    private String nombreLocal;
    private String emailLocal;
    private DireccionModel direccionLocal;
    private String descripcionLocal;
    private ArrayList<String> menuLocal;
    private String webLocal;
    private String telefonoLocal;
    private ArrayList<String> horarios;
    private ArrayList<String> tipoLocal;
    private DonativoModel listaDonativos;
    private ArrayList<ComentariosModel> comentariosLocal;

    private ArrayList<String> listaFavoritos;

    private DonativoModel donativoModel;

    public LocalModel() {
        direccionLocal = new DireccionModel();
        horarios = new ArrayList<>();
        tipoLocal = new ArrayList<>();
        listaDonativos = new DonativoModel();
        comentariosLocal = new ArrayList<>();
        imgLocal= new ArrayList<>();
        menuLocal = new ArrayList<>();
        listaFavoritos = new ArrayList<>();
    }

    protected LocalModel(Parcel in) {
        imgLocal = in.createStringArrayList();
        nombreLocal = in.readString();
        emailLocal = in.readString();
        descripcionLocal = in.readString();
        menuLocal = in.createStringArrayList();
        webLocal = in.readString();
        telefonoLocal = in.readString();
        horarios = in.createStringArrayList();
        tipoLocal = in.createStringArrayList();
        listaFavoritos = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(imgLocal);
        dest.writeString(nombreLocal);
        dest.writeString(emailLocal);
        dest.writeString(descripcionLocal);
        dest.writeStringList(menuLocal);
        dest.writeString(webLocal);
        dest.writeString(telefonoLocal);
        dest.writeStringList(horarios);
        dest.writeStringList(tipoLocal);
        dest.writeStringList(listaFavoritos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocalModel> CREATOR = new Creator<LocalModel>() {
        @Override
        public LocalModel createFromParcel(Parcel in) {
            return new LocalModel(in);
        }

        @Override
        public LocalModel[] newArray(int size) {
            return new LocalModel[size];
        }
    };

    public ArrayList<String> getImgLocal() {
        return imgLocal;
    }

    public void setImgLocal(String imgLocal) {
        this.imgLocal.add(imgLocal);
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getEmailLocal() {
        return emailLocal;
    }

    public void setEmailLocal(String emailLocal) {
        this.emailLocal = emailLocal;
    }

    public DireccionModel getDireccionLocal() {
        return direccionLocal;
    }

    public void setDireccionLocal(DireccionModel direccionLocal) {
        this.direccionLocal = direccionLocal;
    }

    public String getDescripcionLocal() {
        return descripcionLocal;
    }

    public void setDescripcionLocal(String descripcionLocal) {
        this.descripcionLocal = descripcionLocal;
    }

    public ArrayList<String> getMenuLocal() {
        return menuLocal;
    }

    public void setMenuLocal(String menuLocal) {
        this.menuLocal.add(menuLocal);
    }

    public String getWebLocal() {
        return webLocal;
    }

    public void setWebLocal(String webLocal) {
        this.webLocal = webLocal;
    }

    public String getTelefonoLocal() {
        return telefonoLocal;
    }

    public void setTelefonoLocal(String telefonoLocal) {
        this.telefonoLocal = telefonoLocal;
    }

    public ArrayList<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios.add(horarios);
    }

    public ArrayList<String> getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(ArrayList<String> tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public DonativoModel getListaDonativos() {
        return listaDonativos;
    }

    public void setListaDonativos(AppDonativosModel donativos) {
        listaDonativos.setOpciones(donativos);
    }

    public ArrayList<ComentariosModel> getComentariosLocal() {
        return comentariosLocal;
    }

    public void setComentariosLocal(ComentariosModel comentariosLocal) {
        this.comentariosLocal.add(comentariosLocal);
    }

    public ArrayList<String> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(String listaFavoritos) {
        this.listaFavoritos.add(listaFavoritos);
    }
}

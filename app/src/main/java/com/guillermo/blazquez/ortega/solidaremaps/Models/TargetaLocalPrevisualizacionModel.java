package com.guillermo.blazquez.ortega.solidaremaps.Models;

import java.util.ArrayList;

public class TargetaLocalPrevisualizacionModel {
    private ArrayList<String> imagenesLocal;
    private String nombreLocal;
    private String direccionLocal;
    private float puntuacionLocal;
    private ArrayList<String> tipoLocal;

    public TargetaLocalPrevisualizacionModel(ArrayList<String> imagenesLocal, String nombreLocal, String direccionLocal,
                                             float puntuacionLocal, ArrayList<String> tipoLocal) {
        this.imagenesLocal = imagenesLocal;
        this.nombreLocal = nombreLocal;
        this.direccionLocal = direccionLocal;
        this.puntuacionLocal = puntuacionLocal;
        this.tipoLocal = tipoLocal;
    }

    public TargetaLocalPrevisualizacionModel() {
        imagenesLocal = new ArrayList<>();
        tipoLocal = new ArrayList<>();
    }

    public ArrayList<String> getImagenesLocal() {
        return imagenesLocal;
    }

    public void setImagenesLocal(String imagenesLocal) {
        this.imagenesLocal.add(imagenesLocal);
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getDireccionLocal() {
        return direccionLocal;
    }

    public void setDireccionLocal(String direccionLocal) {
        this.direccionLocal = direccionLocal;
    }

    public float getPuntuacionLocal() {
        return puntuacionLocal;
    }

    public void setPuntuacionLocal(float puntuacionLocal) {
        this.puntuacionLocal = puntuacionLocal;
    }

    public ArrayList<String> getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(String tipoLocal) {
        this.tipoLocal.add(tipoLocal);
    }
}

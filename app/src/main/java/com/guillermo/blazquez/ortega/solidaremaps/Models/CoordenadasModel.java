package com.guillermo.blazquez.ortega.solidaremaps.Models;

public class CoordenadasModel {
    private String latitud;
    private String longitud;

    public CoordenadasModel(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public CoordenadasModel() {
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}

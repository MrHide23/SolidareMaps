package com.guillermo.blazquez.ortega.solidaremaps.Models;

public class MarkerInfoModel {
    private String nombre;
    private String direccion;
    private int tlfLocal;
    private double lat;
    private double lon;

    public MarkerInfoModel(String nombre, String direccion, int tlfLocal, double lat, double lon) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tlfLocal = tlfLocal;
        this.lat = lat;
        this.lon = lon;
    }

    public MarkerInfoModel() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTlfLocal() {
        return tlfLocal;
    }

    public void setTlfLocal(int tlfLocal) {
        this.tlfLocal = tlfLocal;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

package com.guillermo.blazquez.ortega.solidaremaps.Models;

import java.util.ArrayList;

public class DireccionModel {

    private String direccion;
    private CoordenadasModel coordenadas;

    public DireccionModel(String direccion, CoordenadasModel coordenadas) {
        this.direccion = direccion;
        this.coordenadas = coordenadas;
    }

    public DireccionModel() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public CoordenadasModel getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(CoordenadasModel coordenadas) {
        this.coordenadas = coordenadas;
    }
}

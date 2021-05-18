package com.guillermo.blazquez.ortega.solidaremaps.Models;

import java.util.ArrayList;

public class DonativoModel {
    private boolean estado;
    private ArrayList<AppDonativosModel> opciones;

    public DonativoModel() {
        opciones = new ArrayList<>();
    }

    public DonativoModel(boolean estado, ArrayList<AppDonativosModel> opciones) {
        this.estado = estado;
        this.opciones = opciones;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public ArrayList<AppDonativosModel> getOpciones() {
        return opciones;
    }

    public void setOpciones(AppDonativosModel opciones) {
        this.opciones.add(opciones);
    }
}

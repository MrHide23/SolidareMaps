package com.guillermo.blazquez.ortega.solidaremaps.Models;

import java.util.ArrayList;

public class LocalModel {

    private ArrayList<String> imgLocal;
    private String nombreLocal;
    private DireccionModel direccionLocal;
    private String descripcionLocal;
    private String menuLocal;
    private int telefonoLocal;
    private ArrayList<String> horariosLocal;
    private ArrayList<String> tipoLocal;
    private ArrayList<DonativoModel> listaDonativos;
    private ArrayList<ComentariosModel> comentariosLocal;

    public LocalModel(ArrayList<String> imgLocal, String nombreLocal, DireccionModel direccionLocal,
                      String descripcionLocal, String menuLocal, int telefonoLocal, ArrayList<String> horariosLocal,
                      ArrayList<String> tipoLocal, ArrayList<DonativoModel> listaDonativos,
                      ArrayList<ComentariosModel> comentariosLocal) {
        this.imgLocal = imgLocal;
        this.nombreLocal = nombreLocal;
        this.direccionLocal = direccionLocal;
        this.descripcionLocal = descripcionLocal;
        this.menuLocal = menuLocal;
        this.telefonoLocal = telefonoLocal;
        this.horariosLocal = horariosLocal;
        this.tipoLocal = tipoLocal;
        this.listaDonativos = listaDonativos;
        this.comentariosLocal = comentariosLocal;
    }

    public LocalModel() {
        this.horariosLocal = new ArrayList<>();
        this.tipoLocal = new ArrayList<>();
        this.listaDonativos = new ArrayList<>();
        this.imgLocal = new ArrayList<>();
        this.comentariosLocal = new ArrayList<>();
    }

    public ArrayList<String> getImgLocal() {
        return imgLocal;
    }

    public void setImgLocal(ArrayList<String> imgLocal) {
        this.imgLocal = imgLocal;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
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

    public String getMenuLocal() {
        return menuLocal;
    }

    public void setMenuLocal(String menuLocal) {
        this.menuLocal = menuLocal;
    }

    public int getTelefonoLocal() {
        return telefonoLocal;
    }

    public void setTelefonoLocal(int telefonoLocal) {
        this.telefonoLocal = telefonoLocal;
    }

    public ArrayList<String> getHorariosLocal() {
        return horariosLocal;
    }

    public void setHorariosLocal(ArrayList<String> horariosLocal) {
        this.horariosLocal = horariosLocal;
    }

    public ArrayList<String> getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(ArrayList<String> tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public ArrayList<DonativoModel> getListaDonativos() {
        return listaDonativos;
    }

    public void setListaDonativos(ArrayList<DonativoModel> listaDonativos) {
        this.listaDonativos = listaDonativos;
    }

    public ArrayList<ComentariosModel> getComentariosLocal() {
        return comentariosLocal;
    }

    public void setComentariosLocal(ArrayList<ComentariosModel> comentariosLocal) {
        this.comentariosLocal = comentariosLocal;
    }
}

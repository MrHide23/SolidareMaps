package com.guillermo.blazquez.ortega.solidaremaps.Models;

import java.util.ArrayList;

public class LocalModel {

    private ArrayList<String> imgLocal;
    private String nombreLocal;
    private String emailLocal;
    private DireccionModel direccionLocal;
    private String descripcionLocal;
    private String menuLocal;
    private String telefonoLocal;
    private ArrayList<String> horarios;
    private ArrayList<String> tipoLocal;
    private ArrayList<DonativoModel> listaDonativos;
    private ArrayList<ComentariosModel> comentariosLocal;

    public LocalModel(ArrayList<String> imgLocal, String nombreLocal, String emailLocal, DireccionModel direccionLocal,
                      String descripcionLocal, String menuLocal, String telefonoLocal, ArrayList<String> horarios,
                      ArrayList<String> tipoLocal, ArrayList<DonativoModel> listaDonativos,
                      ArrayList<ComentariosModel> comentariosLocal) {
        this.imgLocal = imgLocal;
        this.nombreLocal = nombreLocal;
        this.emailLocal = emailLocal;
        this.direccionLocal = direccionLocal;
        this.descripcionLocal = descripcionLocal;
        this.menuLocal = menuLocal;
        this.telefonoLocal = telefonoLocal;
        this.horarios = horarios;
        this.tipoLocal = tipoLocal;
        this.listaDonativos = listaDonativos;
        this.comentariosLocal = comentariosLocal;
    }

    public LocalModel() {
        direccionLocal = new DireccionModel();
        horarios = new ArrayList<>();
        tipoLocal = new ArrayList<>();
        listaDonativos = new ArrayList<>();
        comentariosLocal = new ArrayList<>();
        imgLocal= new ArrayList<>();
    }

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

    public String getMenuLocal() {
        return menuLocal;
    }

    public void setMenuLocal(String menuLocal) {
        this.menuLocal = menuLocal;
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

    public ArrayList<DonativoModel> getListaDonativos() {
        return listaDonativos;
    }

    public void setListaDonativos(DonativoModel donativos) {
        this.listaDonativos.add(donativos);
    }

    public ArrayList<ComentariosModel> getComentariosLocal() {
        return comentariosLocal;
    }

    public void setComentariosLocal(ComentariosModel comentariosLocal) {
        this.comentariosLocal.add(comentariosLocal);
    }
}

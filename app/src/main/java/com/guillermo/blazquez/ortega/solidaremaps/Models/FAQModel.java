package com.guillermo.blazquez.ortega.solidaremaps.Models;

public class FAQModel {

    private String titulo;
    private String contenido;

    public FAQModel(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public FAQModel() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}


package com.guillermo.blazquez.ortega.solidaremaps.Models;

public class ComentariosModel {
    private String email;
    private String comentario;
    private float puntuacion;

    public ComentariosModel(String email, String comentario, float puntuacion) {
        this.email = email;
        this.comentario = comentario;
        this.puntuacion = puntuacion;
    }

    public ComentariosModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }
}

package com.guillermo.blazquez.ortega.solidaremaps.Models;

public class AppDonativosModel {
    private String app;
    private String user;

    public AppDonativosModel(String app, String user) {
        this.app = app;
        this.user = user;
    }

    public AppDonativosModel() {
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

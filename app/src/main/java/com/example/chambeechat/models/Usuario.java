package com.example.chambeechat.models;

public class Usuario {

    private String uid;
    private String email;
    private String password;
    private String displayName;
    private String photoUrl;
    private String locacionEstado;
    private String locacionCiudad;
    private String situacion;
    private String estado;
    private String acercaDeMi;

    public Usuario() { }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLocacionEstado() {
        return locacionEstado;
    }

    public void setLocacionEstado(String locacionEstado) {
        this.locacionEstado = locacionEstado;
    }

    public String getLocacionCiudad() {
        return locacionCiudad;
    }

    public void setLocacionCiudad(String locacionCiudad) {
        this.locacionCiudad = locacionCiudad;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAcercaDeMi() {
        return acercaDeMi;
    }

    public void setAcercaDeMi(String acercaDeMi) {
        this.acercaDeMi = acercaDeMi;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", locacionEstado='" + locacionEstado + '\'' +
                ", locacionCiudad='" + locacionCiudad + '\'' +
                ", situacion='" + situacion + '\'' +
                ", estado='" + estado + '\'' +
                ", acercaDeMi='" + acercaDeMi + '\'' +
                '}';
    }
}

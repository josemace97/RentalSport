package com.iesmaestredecalatrava.rentalsport.modelo;

public class GaleriaPista {

    int id;
    int foto;
    int pista;

    public GaleriaPista(int id, int foto, int pista) {
        this.id = id;
        this.foto = foto;
        this.pista = pista;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getPista() {
        return pista;
    }

    public void setPista(int pista) {
        this.pista = pista;
    }
}

package com.iesmaestredecalatrava.rentalsport.modelo;

public class UbicacionPista {

    int id;
    int pista;
    int ubicacion;

    public UbicacionPista(int id, int pista, int ubicacion) {
        this.id = id;
        this.pista = pista;
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPista() {
        return pista;
    }

    public void setPista(int pista) {
        this.pista = pista;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }
}

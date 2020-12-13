package com.iesmaestredecalatrava.rentalsport.modelo;

public class Foto {

    int id;
    byte foto [];

    public Foto(int id, byte[] foto) {
        this.id = id;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}

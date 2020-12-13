package com.iesmaestredecalatrava.rentalsport.modelo;

import java.io.Serializable;

public class Pista implements Serializable {

    private static int ID=0;

    private int id;
    private String nombre;
    private int idDeporte;
    private byte [] foto;

    public Pista(String nombre) {
        this.nombre = nombre;
    }


    public Pista(String nombre,byte [] foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    public Pista(String nombre,int deporte){

        this.nombre=nombre;
        this.idDeporte=deporte;
    }

    public Pista(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte [] getFoto() {
        return foto;
    }

    public void setFoto(byte [] foto) {
        this.foto = foto;
    }
}

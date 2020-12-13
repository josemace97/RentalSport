package com.iesmaestredecalatrava.rentalsport.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static int ID=0;

    private int id;
    private String nombre;
    private String email;
    private String password;
    private String telefono;

    public Usuario(int id,String nombre,String email,String password,String telefono){

        this.id=id;
        this.nombre=nombre;
        this.email=email;
        this.password=password;
        this.telefono=telefono;
    }

    public Usuario(String nombre,String email,String password,String telefono){

        this.id=ID++;
        this.nombre=nombre;
        this.email=email;
        this.password=password;
        this.telefono=telefono;
    }

    public Usuario(String nombre,String email,String telefono){

        this.nombre=nombre;
        this.email=email;
        this.telefono=telefono;
    }

    public Usuario(){


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

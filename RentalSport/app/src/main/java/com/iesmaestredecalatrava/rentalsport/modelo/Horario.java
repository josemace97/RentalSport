package com.iesmaestredecalatrava.rentalsport.modelo;

public class Horario {

    private int id;
    private int idDeporte;
    private int idPista;
    private String horaInicio;
    private String horaFin;
    private double precio;

    public Horario(int id, int idDeporte, int idPista,String horaInicio, String horaFin, double precio) {
        this.id = id;
        this.idDeporte = idDeporte;
        this.idPista=idPista;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
    }

    public Horario(int idDeporte, String horaInicio, String horaFin, double precio) {
        this.idDeporte = idDeporte;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
    }

    public Horario(String horaInicio, String horaFin, double precio) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

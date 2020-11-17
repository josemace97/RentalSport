package com.iesmaestredecalatrava.rentalsport.modelo;

public class Reserva {

    private static int ID=0;

    private int id;
    private String cliente;
    private String nombrePista;
    private String horaInicio;
    private String horaFin;
    private String fechaReserva;
    private byte [] foto;

    public Reserva(String cliente, String nombrePista, String horaInicio, String horaFin, String fechaReserva) {
        this.cliente = cliente;
        this.nombrePista = nombrePista;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaReserva = fechaReserva;
    }

    public Reserva(String nombrePista,String fechaReserva,byte [] foto){

        this.nombrePista=nombrePista;
        this.fechaReserva=fechaReserva;
        this.foto=foto;
    }

    public String getCliente() {
        return cliente;
    }

    public String getNombrePista() {
        return nombrePista;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public byte[] getFoto() {
        return foto;
    }
}

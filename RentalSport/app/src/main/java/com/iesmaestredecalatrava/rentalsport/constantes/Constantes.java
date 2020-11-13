package com.iesmaestredecalatrava.rentalsport.constantes;

public class Constantes {

    public static final String CREAR_TABLA_USUARIOS="CREATE TABLE IF NOT EXISTS USUARIOS " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "NOMBRE TEXT,EMAIL TEXT,PASSWORD TEXT,TELEFONO TEXT)";

    public static String CREAR_TABLA_DEPORTES="CREATE TABLE IF NOT EXISTS DEPORTES " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DEPORTE TEXT)";

    public static final String CREAR_TABLA_PISTAS="CREATE TABLE IF NOT EXISTS PISTAS "+
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "NOMBRE TEXT,FOTO BLOB,DEPORTE INTEGER,FOREIGN KEY(DEPORTE) REFERENCES ID(DEPORTES))";

    public static final String CREAR_TABLA_HORARIOS_PRECIOS="CREATE TABLE IF NOT EXISTS HORARIOS "+
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT,ID_DEPORTE INTEGER," +
            "HORA_INICIO TEXT,HORA_FIN TEXT," +
            "PRECIO REAL,ID_PISTA INTEGER,FOREIGN KEY(ID_DEPORTE) REFERENCES DEPORTES(ID),FOREIGN KEY(ID_PISTA) REFERENCES PISTAS(ID))";

    public static final String CREAR_TABLA_UBICACIONES="CREATE TABLE IF NOT EXISTS UBICACION (ID INTEGER PRIMARY KEY AUTOINCREMENT,LATITUD REAL,LONGITUD REAL)";

    public static final String CREAR_TABLA_UBICACIONES_PISTA="CREATE TABLE IF NOT EXISTS UBICACIONES_PISTA " +
            "(ID INTEGER PRIMARY KEY,ID_PISTA INTEGER,ID_UBICACION INTEGER," +
            "FOREIGN KEY(ID_PISTA) REFERENCES PISTAS(ID),FOREIGN KEY(ID_UBICACION) REFERENCES UBICACION(ID))";

    public static final String CREAR_TABLA_RESERVAS="CREATE TABLE IF NOT EXISTS RESERVAS "+
            " (ID INTEGER PRIMARY KEY,ID_USUARIO INTEGER,ID_PISTA INTEGER,"+
            "ID_HORARIO INTEGER,FECHA TEXT," +
            "FOREIGN KEY(ID_USUARIO) REFERENCES USUARIOS(ID)," +
            "FOREIGN KEY(ID_PISTA) REFERENCES PISTAS(ID)," +
            "FOREIGN KEY(ID_HORARIO) REFERENCES HORARIOS(ID))";


    public static final String ACCESO_AYTO_PAG_WEB_ALMAGRO="http://www.almagro.es/concejalia-de-deportes/informacion/instalaciones-";
    public static final String DESCRIPCION="RentalSport es una aplicación realizada" +
            " por José Manuel Costoso del IES Maestre de Calatrava orientada a la gestión de " +
            "alquileres de los complejos deportivos de Almagro con el objetivo de incitar a los habitantes " +
            " de la localidad a acceder a las instalaciones y contactar de una manera mas rápida y cómoda.";

}

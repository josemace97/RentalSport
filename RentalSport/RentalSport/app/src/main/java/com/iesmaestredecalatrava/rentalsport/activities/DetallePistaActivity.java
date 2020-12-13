package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;

public class DetallePistaActivity extends DialogFragment {

    private ConexionBD conexionBD;
    private TextView codigoPostal,direccion,
    provincia,municipio,telefono,correo,
    paginaWeb,propietario,extension,GPS;

    private String nombrePista;
    private String direcc,superficie;
    private double latitud,longitud;

     public Dialog onCreateDialog(Bundle savedInstanceState){

         AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
         LayoutInflater inflater=getActivity().getLayoutInflater();
         View v=inflater.inflate(R.layout.activity_detalle_pista,null);

         codigoPostal=v.findViewById(R.id.txtCodigoPostal);
         direccion=v.findViewById(R.id.txtDireccion);
         provincia=v.findViewById(R.id.txtProvincia);
         municipio=v.findViewById(R.id.txtMunicipio);
         telefono=v.findViewById(R.id.txtTelefono);
         correo=v.findViewById(R.id.txtCorreo);
         paginaWeb=v.findViewById(R.id.txtPaginaWeb);
         propietario=v.findViewById(R.id.txtPropietario);
         extension=v.findViewById(R.id.txtExtension);
         GPS=v.findViewById(R.id.txtGPS);

         codigoPostal.setText("Código posta:13270");
         direccion.setText("Dirección:"+direcc);
         provincia.setText("Provincia:Ciudad Real");
         municipio.setText("Municipio:Almagro");
         telefono.setText("Teléfono:926860268");
         correo.setText("Correo:deporte@almagro.es");
         paginaWeb.setText("Página web:http://www.almagro.es");
         propietario.setText("Tipo de propietario:Ayuntamiento");
         extension.setText("Extension:"+superficie+" m2");
         GPS.setText("Lat:"+latitud+",Long:"+longitud);

         conexionBD=new ConexionBD(getContext());

         try {
             conexionBD.openDataBase();
         } catch (IOException e) {
             e.printStackTrace();
         }

         cargarDatos();

         nombrePista=getArguments().getString("nombre_pista");

         builder.setView(v);

         return builder.create();
    }

    private void cargarDatos(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT P.DIRECCION,P.EXTENSION,U.LATITUD,U.LONGITUD " +
                "FROM PISTAS P,UBICACION_PISTA UP,UBICACION U " +
                "WHERE P.ID=UP.PISTA AND U.ID=UP.UBICACION AND " +
                "P.NOMBRE='"+nombrePista+"'",null);

        while (c.moveToFirst()){

            direcc=c.getString(0);
            superficie=c.getString(1);
            latitud=c.getDouble(2);
            longitud=c.getDouble(3);
        }

        c.close();
        sqLiteDatabase.close();
        conexionBD.close();

     }
}

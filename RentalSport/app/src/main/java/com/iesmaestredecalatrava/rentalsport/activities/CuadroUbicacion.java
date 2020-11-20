package com.iesmaestredecalatrava.rentalsport.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.util.ArrayList;

public class CuadroUbicacion extends DialogFragment {

    private ConexionBD conexionBD;
    private Spinner pistas;
    private EditText eLatitud,eLongitud;
    private Button button;
    private ArrayList<String> datos;
    private String nombrePista="";

    public Dialog onCreateDialog(Bundle savedInstanceState){

        conexionBD=new ConexionBD(getContext());

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.activity_dialogo_ubicacion,null);

        datos=new ArrayList<>();

        eLatitud=v.findViewById(R.id.latitud);
        eLongitud=v.findViewById(R.id.longitud);
        pistas=v.findViewById(R.id.spinnerPistas);
        button=v.findViewById(R.id.btnAniadirUbicacion);

        rellenarSpinner();

        ArrayAdapter<CharSequence> adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,datos);

        pistas.setAdapter(adapter);

        pistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nombrePista=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarUbicacion();
            }
        });


        builder.setView(v);

        return builder.create();
    }

    public void insertarUbicacion(){

        double latitud=Double.parseDouble(eLatitud.getText().toString());
        double longitud=Double.parseDouble(eLongitud.getText().toString());

        SQLiteDatabase db=conexionBD.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("LATITUD",latitud);
        contentValues.put("LONGITUD",longitud);

        db.insert("COORDENADAS",null,contentValues);

        Toast.makeText(getActivity(),"Se ha insertado la ubicacion",Toast.LENGTH_SHORT).show();

        insertarUbicacionPista();

    }

    public void insertarUbicacionPista(){

        int idPista=getIdPista();
        int idUbicacion=getIdUbicacion();

        SQLiteDatabase db=conexionBD.getWritableDatabase();

        Toast.makeText(getActivity(),idPista+" "+idUbicacion,Toast.LENGTH_SHORT).show();

        ContentValues contentValues=new ContentValues();

        contentValues.put("ID",idPista);
        contentValues.put("PISTA",idPista);
        contentValues.put("UBICACION",idUbicacion);

        db.insert("UBICACION_PISTA",null,contentValues);

        Toast.makeText(getActivity(),"Se ha insertado la ubicación pista "+nombrePista,Toast.LENGTH_SHORT).show();
    }

    private void rellenarSpinner(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT NOMBRE FROM PISTAS",null);

        datos.add("Seleccionar pista: ");

        while (c.moveToNext()){

            datos.add(c.getString(0));
        }
    }

    private int getIdUbicacion(){

        double latitud=Double.parseDouble(eLatitud.getText().toString());
        double longitud=Double.parseDouble(eLongitud.getText().toString());

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM UBICACION WHERE LATITUD="+latitud+" AND LONGITUD="+longitud,null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;
    }

    private int getIdPista(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM PISTAS WHERE NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;
    }
}

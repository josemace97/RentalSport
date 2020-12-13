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

public class CuadroHorarios extends DialogFragment {

    private ConexionBD conexionBD;
    private Spinner cmbHoraInicio,cmbHoraFin,cmbDeportes,cmbPistas;
    private EditText precioPista;
    private String deporte,horaInicio,horaFin,pista;
    private Button nuevoHorario;
    private ArrayList<String> datos;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        conexionBD=new ConexionBD(getContext());
        datos=new ArrayList<String>();

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.activity_dialogo_horarios,null);

        cmbHoraInicio=v.findViewById(R.id.spinnerHoraInicio);
        cmbHoraFin=v.findViewById(R.id.spinnerHoraFin);
        cmbDeportes=v.findViewById(R.id.spinnerdeportes);
        cmbPistas=v.findViewById(R.id.spinnerpistas);
        precioPista=v.findViewById(R.id.precioalquiler);
        nuevoHorario=v.findViewById(R.id.btnNuevoHorario);

        cargarSpinnerDeportes();
        cargarSpinnerHorasInicio();
        cargarSpinnerHorasFin();
        cargarSpinnerPistas();

        ArrayAdapter<CharSequence> adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,datos);

        cmbPistas.setAdapter(adapter);

        cmbHoraInicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                horaInicio=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbHoraFin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                horaFin=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbDeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                deporte=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbPistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pista=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nuevoHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarHorario();
            }
        });

        builder.setView(v);

        return builder.create();

    }

    private void cargarSpinnerDeportes(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                R.array.combo_deportes,android.R.layout.simple_spinner_item);

        cmbDeportes.setAdapter(adapter);
    }

    private void cargarSpinnerHorasInicio(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                R.array.combo_horas,android.R.layout.simple_spinner_item);

        cmbHoraInicio.setAdapter(adapter);
    }

    private void cargarSpinnerHorasFin(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                R.array.combo_horas,android.R.layout.simple_spinner_item);

        cmbHoraFin.setAdapter(adapter);
    }

    private void cargarSpinnerPistas(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT NOMBRE FROM PISTAS",null);

        datos.add("Seleccionar pista: ");

        while (c.moveToNext()){

            datos.add(c.getString(0));
        }
    }

    private void insertarHorario(){

        double precio=Double.parseDouble(precioPista.getText().toString());
        int deporte=getIdDeporte();
        int idPista=getIdPista();

        SQLiteDatabase sqLiteDatabase=conexionBD.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("PISTA",idPista);
        contentValues.put("DEPORTE",deporte);
        contentValues.put("HORA_INICIO",horaInicio);
        contentValues.put("HORA_FIN",horaFin);
        contentValues.put("PRECIO",precio);

        sqLiteDatabase.insert("HORARIOS",null,contentValues);

        Toast.makeText(getActivity(),"Se ha habilitado el horario de "+horaInicio+" a "+horaFin,Toast.LENGTH_SHORT).show();

    }

    private int getIdDeporte(){

        int id_deporte=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM DEPORTES WHERE DEPORTE='"+deporte+"'",null);

        if(c==null){

            Toast.makeText(getContext(),"El cursor es nulo",Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){

            id_deporte=c.getInt(0);
        }

        return id_deporte;
    }

    private int getIdPista(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM PISTAS WHERE NOMBRE='"+pista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;
    }
}

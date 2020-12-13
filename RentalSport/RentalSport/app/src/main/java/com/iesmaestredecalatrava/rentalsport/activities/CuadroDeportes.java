package com.iesmaestredecalatrava.rentalsport.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

public class CuadroDeportes extends DialogFragment {

    private Spinner cmbDeportes;
    private String deporte;
    private Button nuevoDeporte;
    private ConexionBD conexionBD;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.activity_dialogo_deporte,null);

        cmbDeportes=v.findViewById(R.id.spinnerDeporte);
        nuevoDeporte=v.findViewById(R.id.btnAnidirDeporte);
        conexionBD=new ConexionBD(getActivity(),"bd_rentalsport",null,2);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                R.array.combo_deportes,android.R.layout.simple_spinner_item);

        cmbDeportes.setAdapter(adapter);

        cmbDeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                deporte=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nuevoDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDeporte();
            }
        });

        builder.setView(v);

        return builder.create();

    }

    private void insertarDeporte(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        String insertar="INSERT INTO DEPORTES (DEPORTE) " +
                " VALUES ('"+deporte+"')";

        sqLiteDatabase.execSQL(insertar);

        Toast.makeText(getActivity(),"Se ha habilitado la instalaciones de "+deporte,Toast.LENGTH_SHORT).show();

    }
}

package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;

public class DetalleReservaActivity extends AppCompatActivity {

    private EditText horaInicio, horaFin, tiempoAlquiler, precio;
    private Button cancelar;
    private String fechaReserva;
    private ImageView imagenPista;
    private ConexionBD conexionBD;
    private int id;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);


        conexionBD = new ConexionBD(this);
        sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        id = sharedPreferences.getInt("id_usuario", 0);

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
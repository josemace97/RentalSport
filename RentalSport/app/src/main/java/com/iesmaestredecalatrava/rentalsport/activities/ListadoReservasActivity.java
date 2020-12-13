package com.iesmaestredecalatrava.rentalsport.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorReservas;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;
import java.util.ArrayList;

public class ListadoReservasActivity extends AppCompatActivity {

    private ArrayList<Reserva> listaReservas;
    private RecyclerView recyclerView;
    private ConexionBD conexion;
    private AdaptadorReservas adaptadorReservas;
    private int idUsuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas_usuario);

        conexion=new ConexionBD(this);

        try {
            conexion.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listaReservas=new ArrayList<>();
        recyclerView=findViewById(R.id.RecyclerReservasUsuario);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        llenarReservas();

        adaptadorReservas=new AdaptadorReservas(listaReservas);

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        idUsuario=sharedPreferences.getInt("id_usuario",0);


    }

    private void llenarReservas(){

        SQLiteDatabase db=conexion.getReadableDatabase();

        String id=String.valueOf(idUsuario);

        Cursor cursor=db.rawQuery("SELECT P.FOTO,P.NOMBRE " +
                "FROM RESERVAS R,USUARIOS U,PISTAS P " +
                "WHERE R.ID_USUARIO="+id+" AND R.ID_PISTA=P.ID ",null);

        while (cursor.moveToNext()){

            listaReservas.add(new Reserva(cursor.getString(1),cursor.getString(2),cursor.getBlob(0)));

        }

    }
}

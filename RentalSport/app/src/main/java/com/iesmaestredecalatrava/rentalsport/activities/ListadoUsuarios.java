package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.constantes.Constantes;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.util.ArrayList;

public class ListadoUsuarios extends AppCompatActivity {

    private ArrayList<Usuario> listaUsuarios;
    private RecyclerView recyclerView;
    private ConexionBD conexion;
    private AdaptadorUsuarios adaptadorUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuarios);

        conexion=new ConexionBD(getApplicationContext(),"bd_rentalsport",null,3);

        listaUsuarios=new ArrayList<>();
        recyclerView=findViewById(R.id.RecyclerUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        llenarUsuarios();

        adaptadorUsuarios=new AdaptadorUsuarios(listaUsuarios);

        adaptadorUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"Datos: "+listaUsuarios.get(recyclerView.getChildAdapterPosition(v)).getPassword(),Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adaptadorUsuarios);

    }



    private void llenarUsuarios(){

        int id;
        String nombre,email,password,telefono;

        SQLiteDatabase db=conexion.getReadableDatabase();

        Usuario usuario=null;

        Cursor cursor=db.rawQuery("SELECT * FROM USUARIOS",null);

       while (cursor.moveToNext()){

            id=cursor.getInt(0);
            nombre=cursor.getString(1);
            email=cursor.getString(2);
            password=cursor.getString(3);
            telefono=cursor.getString(4);

            usuario=new Usuario(id,nombre,email,password,telefono);

            listaUsuarios.add(usuario);

        }
       
    }


    private void display(int mensaje){

       Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();

    }
}

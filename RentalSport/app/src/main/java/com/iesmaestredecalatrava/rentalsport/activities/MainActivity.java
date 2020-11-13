package com.iesmaestredecalatrava.rentalsport.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.fragments.MapaFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MapaFragment.OnFragmentInteractionListener {

    CardView cardPistas, cardPerfil, cardPrecios, cardAcercaDe, cardCalendario;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardPistas = findViewById(R.id.pistas);
        cardPerfil = findViewById(R.id.perfil);
        cardPrecios = findViewById(R.id.precios);
        cardAcercaDe = findViewById(R.id.acercade);
        cardCalendario=findViewById(R.id.calendario);


        cardPistas.setOnClickListener(this);
        cardPrecios.setOnClickListener(this);
        cardPerfil.setOnClickListener(this);
        cardAcercaDe.setOnClickListener(this);
        cardCalendario.setOnClickListener(this);


        /*cargarPreferencias();*/



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {

            case R.id.pistas:

                i = new Intent(this, DeportesActivity.class);
                startActivity(i);

                break;

            case R.id.perfil:

                i = new Intent(this, DrawerActivity.class);
                startActivity(i);

                break;

            case R.id.precios:

                i=new Intent(this,PreciosActivity.class);
                startActivity(i);

                break;

            case R.id.acercade:

                i = new Intent(this, AcercaDeActivity.class);
                startActivity(i);

                break;

            case R.id.calendario:

                i=new Intent(this,CalendarActivity.class);
                startActivity(i);

        }
    }

    /*private void cargarPreferencias(){

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        int id=sharedPreferences.getInt("id_usuario",0);
        String email=sharedPreferences.getString("email","No hay datos");
        String pass=sharedPreferences.getString("password","NO hay datos");

        /*Toast.makeText(this,"Id: "+id+",email: "+email,Toast.LENGTH_SHORT).show();*/
    /*}*/



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void cargarReservas(){


    }
}



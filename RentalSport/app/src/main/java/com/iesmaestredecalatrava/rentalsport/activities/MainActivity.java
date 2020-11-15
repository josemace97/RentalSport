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
<<<<<<< HEAD
import android.widget.EditText;
import android.widget.ImageView;
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
<<<<<<< HEAD
import androidx.appcompat.app.AlertDialog;
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.iesmaestredecalatrava.rentalsport.R;
<<<<<<< HEAD
import com.iesmaestredecalatrava.rentalsport.constantes.Constantes;
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
import com.iesmaestredecalatrava.rentalsport.fragments.MapaFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MapaFragment.OnFragmentInteractionListener {

    CardView cardPistas, cardPerfil, cardPrecios, cardAcercaDe, cardCalendario;
    Cursor c;

<<<<<<< HEAD
    EditText programador,organizacion,version;
    TextView contacto,codigoFuente;
    ImageView imageView;

=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
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

<<<<<<< HEAD
               mostrarAlertDialog();
=======
                i = new Intent(this, AcercaDeActivity.class);
                startActivity(i);
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

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

<<<<<<< HEAD
    private void mostrarAlertDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View v= getLayoutInflater().inflate(R.layout.activity_acerca_de,null);

        programador=v.findViewById(R.id.edProgramador);
        contacto=v.findViewById(R.id.edContacto);
        version=v.findViewById(R.id.edVersion);
        codigoFuente=v.findViewById(R.id.edCodigoFuente);
        organizacion=v.findViewById(R.id.edOrganizacion);
        imageView=v.findViewById(R.id.imgIcono);

        imageView.setImageResource(R.drawable.iconoapp);
        programador.setText("Programado por: José Manuel Costoso Escobar");
        contacto.setText("josemace97@gmail.com");
        version.setText("Versión: 1.0");
        codigoFuente.setText("https://github.com/josemace97/RentalSport");
        organizacion.setText("Orgzanización:I.E.S Maestre de Calatrava");

        contacto.setOnClickListener(this);
        codigoFuente.setOnClickListener(this);

        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setTitle("RentalSport");
        dialog.show();

        codigoFuente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/josemace97/RentalSport"));
                startActivity(intent);
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent email=new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{contacto.getText().toString()});

                startActivity(Intent.createChooser(email,"Envíar Gmail: "));
            }
        });

    }
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void cargarReservas(){


    }
}



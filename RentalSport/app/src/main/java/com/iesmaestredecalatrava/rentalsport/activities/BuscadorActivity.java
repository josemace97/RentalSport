package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.iesmaestredecalatrava.rentalsport.R;

import java.util.Calendar;
import java.util.TimeZone;

public class BuscadorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText fecha;
    private ImageButton calendario;
    private ImageButton buscadorFecha;
    private boolean pulsado;
    private Bundle bundle;
    private String nombrePista,date;
    private DatePickerDialog.OnDateSetListener setListener;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        pulsado=false;

        bundle=this.getIntent().getExtras();

        nombrePista=bundle.getString("nombrePista");


        bundle=new Bundle();

        bundle.putString("nombrePista",nombrePista);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        fecha=findViewById(R.id.fechaToolbar);
        calendario=findViewById(R.id.calendario);
        buscadorFecha=findViewById(R.id.buscador);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        /*calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();

                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(BuscadorActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                fecha.setText(date);
            }
        };*/


    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){

            case R.id.calendario:

                pulsado=true;

                mostrarDatePicker();

                break;

            case R.id.buscador:

                 if(!pulsado){

                     Toast.makeText(this,"Debes seleccionar una fecha",Toast.LENGTH_SHORT).show();

                 }else{

                     Intent i=new Intent(this,ListadoHorariosActivity.class);
                     i.putExtras(bundle);
                     startActivity(i);
                 }


        }
        return true;
    }

    private void mostrarDatePicker(){

        Calendar c = Calendar.getInstance();

        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(BuscadorActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                if(dayOfMonth<10){

                    date = "0"+dayOfMonth + "/" + (month + 1) + "/" + year;
                    fecha.setText(date);
                    bundle.putString("fecha_reserva",date);

                }else if(month<10){

                    date = dayOfMonth + "/" +"0"+(month + 1)+ "/" + year;
                    fecha.setText(date);
                    bundle.putString("fecha_reserva",date);

                }else if(dayOfMonth<10 && month<10){

                    date = "0"+dayOfMonth + "/" +"0"+(month + 1)+ "/" + year;
                    fecha.setText(date);
                    bundle.putString("fecha_reserva",date);

                } else{

                    date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    fecha.setText(date);
                    bundle.putString("fecha_reserva",date);
                }

            }
        };


    }

}

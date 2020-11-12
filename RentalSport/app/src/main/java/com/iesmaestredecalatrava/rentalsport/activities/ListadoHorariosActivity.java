package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorHorarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Horario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;
import java.util.ArrayList;

public class ListadoHorariosActivity extends AppCompatActivity {

    private Horario horario;
    private ArrayList<Horario> horarios;
    private ConexionBD conexionBD;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private int idUsuario;
    private Intent i;
    private String nombrePista,fechaReserva,horaInicio,horaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_horarios);

        horarios =new ArrayList<>();
        recyclerView=findViewById(R.id.RecyclerHorarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conexionBD=new ConexionBD(this);

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bundle=this.getIntent().getExtras();

        nombrePista=bundle.getString("nombrePista");
        fechaReserva=bundle.getString("fecha_reserva");

        bundle=new Bundle();

        llenarTarifas();

        AdaptadorHorarios adaptadorHorarios=new AdaptadorHorarios(horarios);

        adaptadorHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               horaInicio= horarios.get(recyclerView.getChildAdapterPosition(v)).getHoraInicio();
               horaFin= horarios.get(recyclerView.getChildAdapterPosition(v)).getHoraFin();

               bundle.putString("hora_inicio",horaInicio);
               bundle.putString("hora_fin",horaFin);
               bundle.putString("nombrePista",nombrePista);
               bundle.putString("fechaReserva",fechaReserva);

               i=new Intent(ListadoHorariosActivity.this,ReservaActivity.class);
               i.putExtras(bundle);
               startActivity(i);

            }
        });

        recyclerView.setAdapter(adaptadorHorarios);
    }

    private void llenarTarifas(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

       Cursor c=sqLiteDatabase.rawQuery("SELECT H.HORA_INICIO,H.HORA_FIN,H.PRECIO " +
               "FROM HORARIOS H,PISTAS P " +
               "WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"' " ,null);

        Toast.makeText(this,"Nombre pista: "+nombrePista,Toast.LENGTH_SHORT).show();

        while (c.moveToNext()){

            String horaInicio=c.getString(0);
            String horaFin=c.getString(1);
            double precio=c.getDouble(2);

            horarios.add(new Horario(horaInicio,horaFin,precio));

        }

    }

}

package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
=======
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
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

<<<<<<< HEAD
    private static final String CHANNEL_ID="NOTIFICACION";
    private static final int NOTIFICACION_ID=0;


=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
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

<<<<<<< HEAD
                if(!comprobarReserva()){

                    lanzarNotificacion();

                }else{

                    i=new Intent(ListadoHorariosActivity.this,ReservaActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
=======
               i=new Intent(ListadoHorariosActivity.this,ReservaActivity.class);
               i.putExtras(bundle);
               startActivity(i);
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

            }
        });

        recyclerView.setAdapter(adaptadorHorarios);
    }

    private void llenarTarifas(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

       Cursor c=sqLiteDatabase.rawQuery("SELECT H.HORA_INICIO,H.HORA_FIN,H.PRECIO " +
               "FROM HORARIOS H,PISTAS P " +
               "WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"' " ,null);

<<<<<<< HEAD
=======
        Toast.makeText(this,"Nombre pista: "+nombrePista,Toast.LENGTH_SHORT).show();

>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
        while (c.moveToNext()){

            String horaInicio=c.getString(0);
            String horaFin=c.getString(1);
            double precio=c.getDouble(2);

            horarios.add(new Horario(horaInicio,horaFin,precio));

        }

    }

<<<<<<< HEAD
    private boolean comprobarReserva(){

        boolean continuar=true;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT COUNT(*)"+
                "FROM RESERVAS R,PISTAS P,HORARIOS H,USUARIOS U"+
                " WHERE R.PISTA=R.ID AND R.HORARIO=H.ID "+
                "AND P.NOMBRE='"+nombrePista+"'"+
                "AND H.HORA_INICIO='"+horaInicio+"'"+
                "AND H.HORA_FIN='"+horaFin+"'"+
                "AND R.FECHA='"+fechaReserva+"'",null);

        int numeroReservas=cursor.getCount();

        if(numeroReservas>1){

            continuar=false;

        }

        return continuar;
    }

    private void lanzarNotificacion(){

        createNotification();
        createNotificationChannel();

    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_info);
        builder.setContentTitle("Notificacion RentalSport");
        builder.setContentText("¡Ya hay una reserva para el día "+fechaReserva+" a esa hora!");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());


    }


=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
}

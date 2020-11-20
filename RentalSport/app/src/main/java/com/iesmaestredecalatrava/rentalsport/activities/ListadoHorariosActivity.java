package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorHorarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Horario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListadoHorariosActivity extends AppCompatActivity {

    private Horario horario;
    private ArrayList<Horario> horarios;
    private AdaptadorHorarios adaptadorHorarios;
    private ConexionBD conexionBD;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private int idUsuario;
    private Intent i;
    private String nombrePista,fechaReserva,horaInicio,horaFin;
    private SharedPreferences sharedPreferences;
    private int posicion;

    private static final String CHANNEL_ID="NOTIFICACION";
    private static final int NOTIFICACION_ID=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_horarios);

        horarios =new ArrayList<>();
        recyclerView=findViewById(R.id.RecyclerHorarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences=getSharedPreferences("credenciales",MODE_PRIVATE);

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

        adaptadorHorarios=new AdaptadorHorarios(horarios);

        if(esAdmin()){

            Swipe swipe=new Swipe(ListadoHorariosActivity.this,recyclerView,200,false) {
                @Override
                public void instantiateMySwipe(final RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                    buffer.add(new MyButton(ListadoHorariosActivity.this,
                            "Eliminar", 0,
                            R.drawable.ic_delete, Color.parseColor("#E40000"),
                            new ButtomClickListener() {
                                @Override
                                public void onClick(int pos) {

                                    horaInicio=horarios.get(pos).getHoraInicio();
                                    horaFin=horarios.get(pos).getHoraFin();

                                    posicion=pos;

                                    eliminarHorario();

                                    Toast.makeText(ListadoHorariosActivity.this,"Se ha deshabilitado el horario",Toast.LENGTH_SHORT).show();

                                }
                            }));


                }
            };
        }


        adaptadorHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!esAdmin()){

                    horaInicio= horarios.get(recyclerView.getChildAdapterPosition(v)).getHoraInicio();
                    horaFin= horarios.get(recyclerView.getChildAdapterPosition(v)).getHoraFin();

                    bundle.putString("hora_inicio",horaInicio);
                    bundle.putString("hora_fin",horaFin);
                    bundle.putString("nombrePista",nombrePista);
                    bundle.putString("fechaReserva",fechaReserva);

                    if(!comprobarReserva()){

                        lanzarNotificacion();

                    }else{

                        i=new Intent(ListadoHorariosActivity.this,ReservaActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }


                }

            }
        });

        recyclerView.setAdapter(adaptadorHorarios);
    }

    private void eliminarHorario(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT H.ID FROM HORARIOS H,PISTAS P " +
                "WHERE H.PISTA=P.ID AND H.HORA_INICIO='"+horaInicio+"' AND H.HORA_FIN='"+horaFin+"' AND P.NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        sqLiteDatabase.execSQL("DELETE FROM HORARIOS WHERE ID="+id);

        sqLiteDatabase.close();

        horarios.remove(posicion);
        adaptadorHorarios.notifyDataSetChanged();

    }



    private void llenarTarifas(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

       Cursor c=sqLiteDatabase.rawQuery("SELECT H.HORA_INICIO,H.HORA_FIN,H.PRECIO " +
               "FROM HORARIOS H,PISTAS P " +
               "WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"' " ,null);

        while (c.moveToNext()){

            String horaInicio=c.getString(0);
            String horaFin=c.getString(1);
            double precio=c.getDouble(2);

            horarios.add(new Horario(horaInicio,horaFin,precio));

        }

    }

    private boolean esAdmin(){

        boolean administrador=false;
        int id=sharedPreferences.getInt("id_usuario",0);
        String admin="";

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT ES_ADMIN FROM USUARIOS WHERE ID="+id,null);

        while (cursor.moveToNext()){

            admin=cursor.getString(0);
        }

        if(admin!=null && admin.equals("S")){

            administrador=true;
        }

        return administrador;
    }

    private boolean comprobarReserva(){

        boolean continuar=true;

        int horario=getIdHorario();

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT R.ID"+
        " FROM RESERVAS R,PISTAS P,HORARIOS H"+
        " WHERE R.PISTA=P.ID AND R.HORARIO=H.ID"+
        " AND R.FECHA='"+fechaReserva+"' AND R.HORARIO="+horario,null);

        int numeroReservas=c.getCount();

        if(numeroReservas>=1){

            continuar=false;

        }

        return continuar;
    }

    private void lanzarNotificacion(){

        createNotification();
        createNotificationChannel();

    }

    private int getIdHorario(){

        int idHorario=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT H.ID FROM HORARIOS H,PISTAS P" +
                " WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"' AND H.HORA_INICIO='"+horaInicio+"' AND H.HORA_FIN='"+horaFin+"'",null);

        while (c.moveToNext()){

            idHorario=c.getInt(0);
        }

        return idHorario;

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


}

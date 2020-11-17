package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarActivity extends AppCompatActivity {

    private ArrayList<String> fechas;
    private ConexionBD conexionBD;
    private Date date;
    private MCalendarView calendarView;
    private MCalendarView formatoCelda;
    private SimpleDateFormat simpleDateFormat;
    private long fechaActual;
    private String dateString;
    private DateFormat df;
    private Calendar cal;
    private String fechaReserva;
    private int idUsuario;
    private SharedPreferences sharedPreferences;
    private ImageView imagen;
    private byte img [];
    private EditText nombrePista,horaInicio,horaFin,tiempoAlquiler,precio,direccion;

    private static final String CHANNEL_ID="NOTIFICACION";
    private static final int NOTIFICACION_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        fechas=new ArrayList<>();
        date=new Date();
        conexionBD=new ConexionBD(this);

        fechaActual=System.currentTimeMillis();

        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        dateString=simpleDateFormat.format(fechaActual);

        sharedPreferences=getSharedPreferences("credenciales",MODE_PRIVATE);

        idUsuario=sharedPreferences.getInt("id_usuario",0);

        cargarPreferencias();

        calendarView = findViewById(R.id.calendar);
        calendarView.setBackgroundColor(getResources().getColor(R.color.white));


        setFechas();

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                fechaReserva=date.getDayString()+"/"+date.getMonthString()+"/"+date.getYear();


                final AlertDialog.Builder builder=new AlertDialog.Builder(CalendarActivity.this);
                View v= getLayoutInflater().inflate(R.layout.activity_detalle_reserva,null);

                imagen=v.findViewById(R.id.imgFoto);
                nombrePista=v.findViewById(R.id.edPista);
                direccion=v.findViewById(R.id.edDireccion);
                horaInicio=v.findViewById(R.id.edhoraInicio);
                horaFin=v.findViewById(R.id.edhoraFin);
                precio=v.findViewById(R.id.edPrecio);
                tiempoAlquiler=v.findViewById(R.id.edTiempoAlquiler);

                cargarDetalleReserva(fechaReserva);


                builder.setView(v);
                builder.setTitle("Detalle reserva");
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

     private void cargarPreferencias(){

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        int id=sharedPreferences.getInt("id_usuario",0);

        cargarFechasReserva(id);

    }

   private void cargarFechasReserva(int id){

        Toast.makeText(this,"Pulsa sobre los marcadores para conocer el detalle de la reserva.",Toast.LENGTH_SHORT).show();

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT R.FECHA " +
                "FROM RESERVAS R,USUARIOS U,PISTAS P " +
                "WHERE R.USUARIO=U.ID AND R.PISTA=P.ID" +
                " AND R.USUARIO="+id,null);

        if(cursor==null){

            Toast.makeText(this,"No tienes ninguna reserva",Toast.LENGTH_SHORT).show();

        }else{

            while (cursor.moveToNext()){

                fechas.add(cursor.getString(0));

            }
        }
    }

    private void setFechas(){

        String fecha="";

        for(int i=0;i<fechas.size();i++){

            try {

                fecha=fechas.get(i);

                if(dateString.equals(fecha)){

                    createNotification();
                    createNotificationChannel();

                    }

                df = new SimpleDateFormat("dd/MM/yyyy");
                date = df.parse(fecha);
                cal = new GregorianCalendar();
                cal.setTime(date);

                int anio=cal.get(Calendar.YEAR);
                int mes=cal.get(Calendar.MONTH)+1;
                int dia=cal.get(Calendar.DAY_OF_MONTH);

                calendarView.markDate(anio,mes,dia);


                formatoCelda=calendarView.setMarkedStyle(0,Color.RED);
                formatoCelda.markDate(anio,mes,dia);


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

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
        builder.setContentText("¡Tienes una reserva para hoy!");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());


    }

    private void cargarDetalleReserva(String fecha){

        int idHorario=0;


        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT P.IMG,P.NOMBRE,P.DIRECCION,H.ID,printf('%.2f',H.PRECIO)\n" +
                "FROM RESERVAS R,USUARIOS U,PISTAS P,HORARIOS H\n" +
                "WHERE R.USUARIO=U.ID AND R.PISTA=P.ID AND R.HORARIO=H.ID AND " +
                "R.USUARIO="+idUsuario+" AND R.FECHA='"+fecha+"'",null);

        while (c.moveToNext()){

            img=c.getBlob(0);
            nombrePista.setText("Pista: "+c.getString(1));
            direccion.setText("Dirección: "+c.getString(2));
            precio.setText("Precio: "+c.getDouble(4)+"0 euros.");

            idHorario=c.getInt(3);
        }

        tiempoAlquiler.setText("Tiempo de alquiler: 1h.");
        imagen.setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));

        setHora(idHorario);

    }

    private void setHora(int id){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT HORA_INICIO,HORA_FIN FROM HORARIOS WHERE ID="+id,null);

        while (c.moveToNext()){

            horaInicio.setText("Hora inicio: "+c.getString(0)+" hrs.");
            horaFin.setText("Hora fin: "+c.getString(1)+" hrs.");
        }
    }


}

package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaReservasFragment;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;

public class ReservaActivity extends AppCompatActivity implements ListaReservasFragment.OnFragmentInteractionListener{

    private Bundle bundle;
    private Button confirmarReserva;
    private ImageView imagenPista;
    private EditText nombre,telefono,precio,horaInicio,horaFin;
    private String nombrePista,horainicio
            ,horafin,fechaReserva,tlf,nombreUsuario;
    private double precioReserva;
    private int idPista,idUsuario,idHorario;
    private ConexionBD conexionBD;
    private PendingIntent pendingIntent;
    private static final String CHANNEL_ID="NOTIFICACION";
    private static final int NOTIFICACION_ID=0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        conexionBD=new ConexionBD(this);

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bundle=this.getIntent().getExtras();

        nombrePista=bundle.getString("nombrePista");
        horainicio=bundle.getString("hora_inicio");
        horafin=bundle.getString("hora_fin");
        fechaReserva=bundle.getString("fechaReserva");

        imagenPista=findViewById(R.id.imgPista);
        nombre=findViewById(R.id.edtNombre);
        telefono=findViewById(R.id.edtTelefono);
        horaInicio=findViewById(R.id.edtHoraInicio);
        horaFin=findViewById(R.id.edtHoraFin);
        precio=findViewById(R.id.edtPrecio);

        byte [] imagen=getImagenPista();

        Bitmap bitmap= BitmapFactory.decodeByteArray(imagen,0,imagen.length);
        imagenPista.setImageBitmap(bitmap);

        nombreUsuario=getNombreUsuario();
        tlf=getTelefono();
        precioReserva=getPrecio();
        idPista=getIdPista();
        idHorario=getIdHorario();

        nombre.setText(nombreUsuario);
        telefono.setText(tlf);
        horaInicio.setText(horainicio+" hrs");
        horaFin.setText(horafin+" hrs");
<<<<<<< HEAD
        precio.setText(precioReserva+"0 euros");
=======
        precio.setText(precioReserva+" euros");
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

    }

    private byte [] getImagenPista(){

        byte [] imagen=null;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT IMG FROM PISTAS WHERE nombre='"+nombrePista+"'",null);

        while (c.moveToNext()){

            imagen=c.getBlob(0);
        }

        return imagen;
    }

    private String getNombreUsuario(){

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        idUsuario=sharedPreferences.getInt("id_usuario",0);

        String nombre="";
        String id=String.valueOf(idUsuario);

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT NOMBRE FROM USUARIOS WHERE ID="+id,null);

        while (c.moveToNext()){

            nombre=c.getString(0);
        }

        return nombre;
    }

    private String getTelefono(){

        String tlf="";
        String id=String.valueOf(idUsuario);

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT TELEFONO FROM USUARIOS WHERE ID="+id,null);

        while (c.moveToNext()){

            tlf=c.getString(0);
        }

        return tlf;
    }

    private double getPrecio(){

        double precio=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

<<<<<<< HEAD
        Cursor c=sqLiteDatabase.rawQuery("SELECT printf('%.2f',H.PRECIO) FROM HORARIOS H,PISTAS P WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"'",null);
=======
        Cursor c=sqLiteDatabase.rawQuery("SELECT H.PRECIO FROM HORARIOS H,PISTAS P WHERE H.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"'",null);
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

        while (c.moveToNext()){

            precio=c.getDouble(0);
        }

        return precio;

    }

    private int getIdHorario(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT H.ID FROM HORARIOS H,PISTAS P WHERE " +
                "H.PISTA=P.ID AND H.HORA_INICIO='"+horainicio+"' AND H.HORA_FIN='"+horafin+"' " +
                " AND P.NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

<<<<<<< HEAD
        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("hora_inicio",horainicio);
        editor.putString("hora_fin",horafin);

        editor.commit();


=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
        return id;
    }

    private int getIdPista(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM PISTAS P WHERE P.NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View view){

        SQLiteDatabase sqLiteDatabase=conexionBD.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("USUARIO",idUsuario);
        contentValues.put("PISTA",idPista);
        contentValues.put("HORARIO",idHorario);
        contentValues.put("FECHA",fechaReserva);

        sqLiteDatabase.insert("RESERVAS",null,contentValues);

        setPendingIntent();
        createNotificationChannel();
        createNotification();

        intent=new Intent(this,DrawerActivity.class);
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setPendingIntent(){

        Intent intent = new Intent(this, DrawerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DrawerActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

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
        builder.setSmallIcon(R.drawable.ic_reservas);
        builder.setContentTitle("Notificacion RentalSport");
        builder.setContentText("Tu reserva se ha realizado correctamente");
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

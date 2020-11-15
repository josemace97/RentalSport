package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import android.content.Context;
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;

public class DetalleReservaActivity extends AppCompatActivity {

<<<<<<< HEAD
    private EditText horaInicio, horaFin, tiempoAlquiler, precio;
=======
    private EditText horaInicio,horaFin,tiempoAlquiler,precio;
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
    private Button cancelar;
    private String fechaReserva;
    private ImageView imagenPista;
    private ConexionBD conexionBD;
    private int id;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);


<<<<<<< HEAD
        conexionBD = new ConexionBD(this);
        sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        id = sharedPreferences.getInt("id_usuario", 0);
=======
        conexionBD=new ConexionBD(this);
        sharedPreferences=getSharedPreferences("credenciales",MODE_PRIVATE);
        id=sharedPreferences.getInt("id_usuario",0);
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

<<<<<<< HEAD
    }

}
=======
        imagenPista=findViewById(R.id.imgFoto);
        horaInicio=findViewById(R.id.edhoraInicio);
        horaFin=findViewById(R.id.edhoraFin);
        tiempoAlquiler=findViewById(R.id.edTiempoAlquiler);
        precio=findViewById(R.id.edPrecio);
        cancelar=findViewById(R.id.btnCancelarReserva);

        if(getIntent().getExtras()!=null){

            fechaReserva=getIntent().getExtras().getString("fecha_reserva");

            SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

            Cursor c=sqLiteDatabase.rawQuery("SELECT P.IMG,P.NOMBRE,H.HORA_INICIO,H.HORA_FIN,printf('%.2f',H.PRECIO)" +
                    " FROM PISTAS P,HORARIOS H,RESERVAS R,USUARIOS U" +
                    " WHERE R.PISTA=P.ID AND R.HORARIO=H.ID AND R.USUARIO=U.ID",null);

            while(c.moveToNext()) {

               precio.setText(c.getString(4)+" euros.");
               horaInicio.setText(c.getString(2)+" hrs.");
               horaFin.setText(c.getString(3)+" hrs.");
               tiempoAlquiler.setText("1 hora");

            }

        }

    }

}
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c

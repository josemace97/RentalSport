package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorGaleria;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.recursos.BitmapUtils;

import com.iesmaestredecalatrava.rentalsport.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Galeria extends AppCompatActivity {

    private ImageView imagenSeleccionada;
    private Gallery gallery;
    private ConexionBD conexionBD;
    private String nombrePista;
    private Bitmap imagenesPista[];
    private final static int NUM_IMAGENES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        if (getIntent().getExtras() != null) {

            nombrePista = getIntent().getExtras().getString("nombre_pista");
        }

        conexionBD = new ConexionBD(this);

        imagenSeleccionada = (ImageView) findViewById(R.id.seleccionada);

        imagenesPista = new Bitmap[NUM_IMAGENES];

        getImagenes();

        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new AdaptadorGaleria(this, imagenesPista));
        //al seleccionar una imagen, la mostramos en el centro de la pantalla a mayor tamaño

        //con este listener, sólo se mostrarían las imágenes sobre las que se pulsa
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                imagenSeleccionada.setImageBitmap(imagenesPista[position]);
            }

        });

        /*gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                imagenSeleccionada.setImageBitmap(imagenesPista[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

    }

    private void getImagenes() {

        byte img[];
        int contador = 0;
        int imagen = 0;

        SQLiteDatabase sqLiteDatabase = conexionBD.getWritableDatabase();

        Toast.makeText(this, "Nombre pista: "+nombrePista, Toast.LENGTH_SHORT).show();

        Cursor c = sqLiteDatabase.rawQuery("SELECT F.FOTO\n" +
                "FROM PISTAS P,GALERIA_PISTA GP,FOTOS F\n" +
                "WHERE F.ID=GP.FOTO AND P.ID=GP.PISTA\n" +
                "AND P.NOMBRE='" + nombrePista + "';", null);

        if (c == null) {

            Toast.makeText(this,"El cursor es null",Toast.LENGTH_SHORT).show();

        } else {

            while (c.moveToNext()) {

                imagenesPista[contador] =BitmapFactory.decodeByteArray(c.getBlob(0),0,c.getBlob(0).length);

                contador++;

            }

        }

    }

    private int toInt(byte[] img) {


            int value=ByteBuffer.wrap(img).getInt();

            return value;
        }


}






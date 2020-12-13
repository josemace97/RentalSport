package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.fragments.MapaFragment;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

public class RecintosActivity extends AppCompatActivity implements MapaFragment.OnFragmentInteractionListener{

    private Bundle bundle;
    private ConexionBD conexionBD;
    private double latitud,longitud;
    private String nombrePista;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recintos);

        conexionBD=new ConexionBD(this);

        if(getIntent().getExtras()!=null){

            nombrePista=getIntent().getExtras().getString("nombre_pista");
            cargarDatos();
            Toast.makeText(this,"Nombre pista: "+nombrePista+",Latidud: "+latitud+",Longitud: "+longitud,Toast.LENGTH_SHORT).show();
        }

        bundle=new Bundle();
        bundle.putDouble("latitud",latitud);
        bundle.putDouble("longitud",longitud);

        Fragment fragment=new MapaFragment();

        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorMapa,fragment).commit();

    }

    private void cargarDatos(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT C.LATITUD,C.LONGITUD " +
                "FROM UBICACION_PISTA UP,COORDENADAS C,PISTAS P " +
                "WHERE UP.UBICACION=C.ID AND UP.PISTA=P.ID AND P.NOMBRE='"+nombrePista+"'",null);

        while (cursor.moveToNext()){

            latitud=cursor.getDouble(0);
            longitud=cursor.getDouble(1);
        }

        cursor.close();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

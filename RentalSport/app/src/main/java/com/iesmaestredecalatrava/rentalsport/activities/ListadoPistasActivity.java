package com.iesmaestredecalatrava.rentalsport.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorPistas;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListadoPistasActivity extends AppCompatActivity {

  private ArrayList<Pista> listaPistas;
  private RecyclerView recyclerPistas;
  private Bundle bundle;
  private String deporte;
  private String nombrePista;
  private double latitud,longitud;
  private ConexionBD conn;
  private Bundle bundle2;
  private Pista pista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_pistas);

        bundle=this.getIntent().getExtras();

        if(bundle!=null){

            deporte=bundle.getString("filtroDeporte");
        }

        listaPistas=new ArrayList<>();
        recyclerPistas=findViewById(R.id.RecyclerPistas);
        recyclerPistas.setLayoutManager(new LinearLayoutManager(this));

        conn=new ConexionBD(this);

        try {
            conn.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        llenarPistas(deporte);

        AdaptadorPistas adaptadorPistas=new AdaptadorPistas(listaPistas);

        adaptadorPistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombrePista=listaPistas.get(recyclerPistas.getChildAdapterPosition(v)).getNombre();

                bundle=new Bundle();

                bundle.putString("nombrePista",nombrePista);
                bundle.putDouble("latitud",latitud);
                bundle.putDouble("longitud",longitud);

                Intent i=new Intent(ListadoPistasActivity.this,BuscadorActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        Swipe swipe=new Swipe(this,recyclerPistas,200,false) {
            @Override
            public void instantiateMySwipe(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                buffer.add(new MyButton(ListadoPistasActivity.this,
                        "Galeria", 0,
                        R.drawable.ic_menu_gallery, Color.parseColor("#33D1FF"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {

                                nombrePista=listaPistas.get(pos).getNombre();

                                Intent i=new Intent(ListadoPistasActivity.this,Galeria.class);
                                bundle.putString("nombre_pista",nombrePista);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        }));

                buffer.add(new MyButton(ListadoPistasActivity.this,
                        "Ubicación", 0,
                        R.drawable.ic_place, Color.parseColor("#F6FF33"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {

                                nombrePista=listaPistas.get(pos).getNombre();
                                /*getCoordenadas();*/

                                /*Intent i=new Intent(ListadoPistasActivity.this,RecintosActivity.class);
                                bundle.putString("nombre_pista",nombrePista);
                                i.putExtras(bundle);
                                startActivity(i);*/

                                /*Intent i=new Intent(ListadoPistasActivity.this,RutaActivity.class);
                                bundle.putString("nombre_pista",nombrePista);
                                i.putExtras(bundle);
                               startActivity(i);*/
                            }
                        }));

            }
        };

        recyclerPistas.setAdapter(adaptadorPistas);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case 1:

                getCoordenadas();

               Intent i=new Intent(this,RecintosActivity.class);
               i.putExtras(bundle);
               startActivity(i);

                break;

            case 2:

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void llenarPistas(String deporte) {

        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT P.NOMBRE,P.IMG FROM PISTAS P,DEPORTES D WHERE P.DEPORTE=D.ID AND D.DEPORTE='"+deporte+"'", null);

        if(c==null){

            Toast.makeText(this,"El cursor es nulo",Toast.LENGTH_SHORT).show();

        }else{

            while (c.moveToNext()) {

                listaPistas.add(new Pista(c.getString(0),c.getBlob(1)));
            }
        }


        Toast.makeText(this, "Se han encontrado "+ listaPistas.size()+" resultado/resultados", Toast.LENGTH_SHORT).show();

    }

    private void getCoordenadas(){

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT U.LATITUD,U.LONGITUD " +
                "FROM UBICACION U,UBICACIONES_PISTA UP,PISTA P " +
                "WHERE UP.ID_UBICACION=U.ID AND UP.ID_PISTA=P.ID AND P.NOMBRE='"+nombrePista+"'",null);

       if(c==null){

           Toast.makeText(this,"El cursor es nulo",Toast.LENGTH_SHORT).show();
       }

        while (c.moveToNext()){

            latitud=c.getDouble(0);
            longitud=c.getDouble(1);

            Toast.makeText(this,"Latitud: "+latitud+",Longitud: "+longitud,Toast.LENGTH_SHORT).show();

        }
    }

   }



package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AniadirPistaActivity extends AppCompatActivity {

   private Spinner cmbDeportes,cmbHorasInicio,cmbHorasFin,cmbPistas;
   private ImageView imagenPista;
   private EditText eNombre,ePrecio,eHoraInicio,eHoraFin,eLatitud,eLongitud;
   private String nombrePista;
   private String deporte,horaInicio,horaFin;
   private ConexionBD conn;
   private Button aniadirHorario;
   private boolean limiteRegistros;
   private ArrayList<String> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_pista);

        cmbDeportes=findViewById(R.id.deportes);
        cmbHorasInicio=findViewById(R.id.horasinicio);
        cmbHorasFin=findViewById(R.id.horasfin);
        cmbPistas=findViewById(R.id.comboPistas);
        imagenPista=findViewById(R.id.imgPista);
        eNombre=findViewById(R.id.edNombrePista);
        ePrecio=findViewById(R.id.edPrecio);
        eHoraInicio=findViewById(R.id.edHoraInicio);
        eHoraFin=findViewById(R.id.edHoraFin);
        eLatitud=findViewById(R.id.edLatitud);
        eLongitud=findViewById(R.id.edLongitud);
        aniadirHorario=findViewById(R.id.btnAniadirHorario);

        datos=new ArrayList<>();

       conn=new ConexionBD(getApplicationContext(),"bd_rentalsport",null,2);

       cargarSpinnerDeportes();
       cargarSpinnerHorasInicio();
       cargarSpinnerHorasFin();
       cargarSpinnerPistas();

       cmbDeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    deporte=parent.getItemAtPosition(position).toString();
                    insertarTipo(deporte);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbHorasInicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                horaInicio=parent.getItemAtPosition(position).toString();
                eHoraInicio.setText(horaInicio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbHorasFin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                horaFin=parent.getItemAtPosition(position).toString();
                eHoraFin.setText(horaFin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbPistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nombrePista=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void cargarSpinnerDeportes(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.combo_deportes,android.R.layout.simple_spinner_item);

        cmbDeportes.setAdapter(adapter);
    }

    private void cargarSpinnerHorasInicio(){
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.combo_horas,android.R.layout.simple_spinner_item);

        cmbHorasInicio.setAdapter(adapter);

    }

    private void cargarSpinnerHorasFin(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.combo_horas,android.R.layout.simple_spinner_item);

        cmbHorasFin.setAdapter(adapter);
    }


    public void cargarImagen(View view){

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona una imagen"),10);
    }

    public void insertarPista(View view){

       String nombre=eNombre.getText().toString();
       byte imagen []=convertToByte(imagenPista);
       int tipo_pista=getTipoPista();

       SQLiteDatabase db=conn.getWritableDatabase();

       ContentValues contentValues=new ContentValues();

        contentValues.put("NOMBRE",nombre);
        contentValues.put("FOTO",imagen);
        contentValues.put("DEPORTE",tipo_pista);

       /*String insertar="INSERT INTO PISTAS (NOMBRE,DEPORTE,DIRECCION,FOTO) " +
                " VALUES ('"+nombre+"','"+deporte+"','"+direccion+"','"+imagen+"')";*/

       db.insert("PISTAS",null,contentValues);

       Toast.makeText(this,"Se ha habilitado la pista.",Toast.LENGTH_SHORT).show();

    }


    private void insertarUbicacionPista(){

        SQLiteDatabase sqLiteDatabase=conn.getWritableDatabase();

        ContentValues contentValues=new ContentValues();


        contentValues.put("ID_PISTA",0);
        contentValues.put("ID_UBICACION",0);

        sqLiteDatabase.insert("UBICACIONES_PISTA",null,contentValues);
    }

    private void insertarTipo(String deporte){

           limiteRegistros=comprobar();

            SQLiteDatabase db=conn.getWritableDatabase();

            if(!limiteRegistros){

                String insertar="INSERT INTO DEPORTES (DEPORTE) " +
                        " VALUES ('"+deporte+"')";

                db.execSQL(insertar);

                Toast.makeText(this,"Se ha insertado el tipo",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(this,"Límite de registros",Toast.LENGTH_SHORT).show();
            }

    }

    public void insertarTarifa(View view){

        double precio=Double.parseDouble(ePrecio.getText().toString());
        int tipo_pista=getTipoPista();
        int idPista=getIdPista();

        Toast.makeText(this,"Id pista: "+idPista,Toast.LENGTH_SHORT).show();

        SQLiteDatabase sqLiteDatabase=conn.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("ID_PISTA",idPista);
        contentValues.put("ID_DEPORTE",tipo_pista);
        contentValues.put("HORA_INICIO",horaInicio);
        contentValues.put("HORA_FIN",horaFin);
        contentValues.put("PRECIO",precio);

        sqLiteDatabase.insert("HORARIOS",null,contentValues);

        Toast.makeText(this,"Se ha insertado la tarifa",Toast.LENGTH_SHORT).show();

        mostrarTarifas();
    }

    private void mostrarTarifas(){


        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT P.NOMBRE,H.PRECIO FROM HORARIOS H,PISTAS P WHERE H.ID_PISTA=P.ID",null);

        while (c.moveToNext()){

            String nombre=c.getString(0);
            double precio=c.getDouble(1);

            Toast.makeText(this,"Nombre: "+nombre+",precio: "+precio,Toast.LENGTH_SHORT).show();
        }

    }

    public void mostrarCuadroEmergente(View view){

        FragmentManager fm=getSupportFragmentManager();
        CuadroUbicacion cuadroUbicacion=new CuadroUbicacion();
        cuadroUbicacion.show(fm,"tagAlerta");


    }

    private boolean comprobar(){

        int numeroRegistros=0;
        boolean limite=false;

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM DEPORTES",null);

        if(c==null){

            Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();

        }else{

            while (c.moveToNext()){

                numeroRegistros++;
            }
        }

        if(numeroRegistros==5){

            limite=true;
        }

        return limite;
    }

    private void mostrarId(){

        int id_tipo=0;
        String deporte="";

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM DEPORTES",null);

        if(c==null){

            Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){

            id_tipo=c.getInt(0);
            deporte=c.getString(1);

            Toast.makeText(this,"Id: "+id_tipo+",deporte: "+deporte,Toast.LENGTH_SHORT).show();

        }

    }

    private int getTipoPista(){

        int id_tipo=0;

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM DEPORTES WHERE DEPORTE='"+deporte+"'",null);

        if(c==null){

            Toast.makeText(this,"El cursor es nulo",Toast.LENGTH_SHORT).show();
        }

         while (c.moveToNext()){

             id_tipo=c.getInt(0);

             Toast.makeText(this,"Id deporte: "+id_tipo,Toast.LENGTH_SHORT).show();
         }

         return id_tipo;
    }

    private byte [] convertToByte(ImageView image){

        Bitmap bitmap= ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte [] array=stream.toByteArray();

        return array;

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==RESULT_OK){

            Uri path=data.getData();
            imagenPista.setImageURI(path);
        }
    }

    private void rellenarSpinnerAlertDialog(){

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT NOMBRE FROM PISTAS",null);

        datos.add("Seleccionar pista: ");

        while (c.moveToNext()){

            datos.add(c.getString(0));
        }
    }

    private void cargarSpinnerPistas(){

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT NOMBRE FROM PISTAS",null);

        if(c==null){

            Toast.makeText(this,"El cursor es nulo",Toast.LENGTH_SHORT).show();

        }else{

            while (c.moveToNext()){

                datos.add(c.getString(0));
            }

            ArrayAdapter<CharSequence> adapter=
                    new ArrayAdapter(this,android.R.layout.simple_spinner_item,datos);

            cmbPistas.setAdapter(adapter);

        }

    }

    private int getIdPista(){

        int id=0;

        nombrePista=eNombre.getText().toString();

        SQLiteDatabase sqLiteDatabase=conn.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM PISTAS WHERE NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;
    }


}

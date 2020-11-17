package com.iesmaestredecalatrava.rentalsport.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class CuadroPista extends DialogFragment {

    private ImageView img;
    private EditText nombre;
    private Button cargarImg,aniadirPista;
    private Spinner cmbDeportes;
    private String deporte;
    private ConexionBD conexionBD;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        /*conexionBD=new ConexionBD(getActivity(),"bd_rentalsport",null,2);*/

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.activity_dialogo_pista,null);

        img=v.findViewById(R.id.imgPista);
        nombre=v.findViewById(R.id.nombrepista);
        cargarImg=v.findViewById(R.id.btnCargarImagen);
        aniadirPista=v.findViewById(R.id.btnAniadirPista);
        cmbDeportes=v.findViewById(R.id.spinnerDeportes);

        cargarSpinnerDeportes();

        cmbDeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                deporte=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        aniadirPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarPista();
            }
        });

        builder.setView(v);

        return builder.create();
    }

    public void cargarImagen(){

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona una imagen"),10);
    }

    private void cargarSpinnerDeportes(){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                R.array.combo_deportes,android.R.layout.simple_spinner_item);

        cmbDeportes.setAdapter(adapter);
    }

    private byte [] convertToByte(ImageView image){

        Bitmap bitmap= ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte [] array=stream.toByteArray();

        return array;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==RESULT_OK){

            Uri path=data.getData();
            img.setImageURI(path);
        }
    }

    private void insertarPista(){

        String nombrePista=nombre.getText().toString();
        byte imagen []=convertToByte(img);
        int tipo_pista=getTipoPista();

        SQLiteDatabase db=conexionBD.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("NOMBRE",nombrePista);
        contentValues.put("FOTO",imagen);
        contentValues.put("DEPORTE",tipo_pista);

        db.insert("PISTAS",null,contentValues);

        Toast.makeText(getContext(),"Se ha habilitado la pista "+nombrePista,Toast.LENGTH_SHORT).show();
    }

    private int getTipoPista(){

        int id_tipo=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM DEPORTES WHERE DEPORTE='"+deporte+"'",null);

        if(c==null){

            Toast.makeText(getContext(),"El cursor es nulo",Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){

            id_tipo=c.getInt(0);

            Toast.makeText(getContext(),"Id deporte: "+id_tipo,Toast.LENGTH_SHORT).show();
        }

        return id_tipo;
    }

}

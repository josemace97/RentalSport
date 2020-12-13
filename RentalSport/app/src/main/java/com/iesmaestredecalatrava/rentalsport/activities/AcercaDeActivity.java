package com.iesmaestredecalatrava.rentalsport.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);


    }

}

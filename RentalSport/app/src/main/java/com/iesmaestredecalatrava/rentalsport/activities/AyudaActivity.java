package com.iesmaestredecalatrava.rentalsport.activities;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iesmaestredecalatrava.rentalsport.R;

public class AyudaActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        EditText mensaje=findViewById(R.id.txtMensajeAyuda);

        inicializarFirebase();

        databaseReference.child("Mensaje").setValue(mensaje.getText().toString());

    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

    }
}

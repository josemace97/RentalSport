package com.iesmaestredecalatrava.rentalsport.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;

public class RegistroActivity extends AppCompatActivity {

    private static FirebaseAuth fireBaseAuth=FirebaseAuth.getInstance();

    private EditText txtNombre,txtEmail,txtPass,txtPassRepetida,txtTelefono;
    private String nombre,email,pass,telefono;
    private ProgressDialog progressDialog;
    private ConexionBD conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre=findViewById(R.id.editTextNombre);
        txtEmail=findViewById(R.id.editTextEmail);
        txtPass=findViewById(R.id.editTextPassword1);
        txtPassRepetida=findViewById(R.id.editTextPassword2);
        txtTelefono=findViewById(R.id.editTextTelef);

        progressDialog=new ProgressDialog(this);

        conn=new ConexionBD(this);

        try {
            conn.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void confirmarRegistro(View view){

        if(TextUtils.isEmpty(txtNombre.getText().toString()) ||
        TextUtils.isEmpty(txtEmail.getText().toString()) ||
        TextUtils.isEmpty(txtPass.getText().toString()) ||
        TextUtils.isEmpty(txtPassRepetida.getText().toString()) ||
                TextUtils.isEmpty(txtTelefono.getText().toString())){

            Toast.makeText(this,"Debes rellenar todos los campos",Toast.LENGTH_SHORT).show();

        }else if(!txtPass.getText().toString().equals(txtPassRepetida.getText().toString())){

            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();

        }else{

            email=txtEmail.getText().toString().trim();
            pass=txtPass.getText().toString().trim();
            telefono=txtTelefono.getText().toString().trim();
            nombre=txtNombre.getText().toString().trim();

            progressDialog.setMessage("Realizando registro...");
            progressDialog.show();

            fireBaseAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(RegistroActivity.this,"¡Bienvenido a RentalSport "+txtNombre.getText().toString()+"!",Toast.LENGTH_SHORT).show();
                                insertarUsuarioDB(nombre,email,pass,telefono);


                            }else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                    Toast.makeText(RegistroActivity.this, "El usuario ya está registrado.", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(RegistroActivity.this, "No se ha podido realizar el registro.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            progressDialog.dismiss();
                        }
                    });
        }

    }

    private void insertarUsuarioDB(String nombre,String email,String pass,String telefono){

        SQLiteDatabase db=conn.getWritableDatabase();

       String insertarUsuario="INSERT INTO USUARIOS (NOMBRE,EMAIL,PASSWORD,TELEFONO) " +
               " VALUES ('"+nombre+"','"+email+"','"+pass+"','"+telefono+"')";

       db.execSQL(insertarUsuario);

    }

    public static FirebaseAuth getFireBaseAuth(){

        return fireBaseAuth;
    }

}

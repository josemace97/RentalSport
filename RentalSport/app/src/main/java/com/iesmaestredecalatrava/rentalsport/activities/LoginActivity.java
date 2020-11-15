package com.iesmaestredecalatrava.rentalsport.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity{

    private Button login,registro;
    private EditText txtEmail,txtPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth fireBaseAuth;
    private String email,pass;
    private Intent intent;
    private ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.btnLogin);
        registro=findViewById(R.id.btnRegistro);

        txtEmail=findViewById(R.id.editTextEmail);
        txtPass=findViewById(R.id.editTextPass);

        fireBaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        conexionBD=new ConexionBD(this);


        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View view){

        switch (view.getId()){

            case R.id.btnLogin:

                validarLogin();

                break;

            case R.id.btnRegistro:

                Intent registroActvity=new Intent(this, RegistroActivity.class);
                startActivity(registroActvity);


        }
    }


    private void validarLogin(){

        if(TextUtils.isEmpty(txtEmail.getText().toString())){

            Toast.makeText(this,"Debes introducir el email",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(txtPass.getText().toString())){

            Toast.makeText(this,"Debes introducir la contraseña",Toast.LENGTH_SHORT).show();

        }else{

            email=txtEmail.getText().toString();
            pass=txtPass.getText().toString();

            if(esAdmin()){

                intent=new Intent(this,DrawerAdmin.class);
                startActivity(intent);

            }else{

                comprobarUsuario();

                progressDialog.setMessage("Cargando...");
                progressDialog.show();

                fireBaseAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    guardarCredenciales();

                                    intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }else {

                                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();

                                }

                                progressDialog.dismiss();
                            }
                        });
            }

        }

    }

    private void comprobarUsuario(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM USUARIOS WHERE " +
                "EMAIL='"+email+"' AND PASSWORD='"+pass+"'" ,null);

        while (c.moveToNext()){

            email=c.getString(2);
            pass=c.getString(3);
        }


    }



    private int getId(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ID FROM USUARIOS WHERE EMAIL='"+email+"' AND PASSWORD='"+pass+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        return id;

    }

    private void guardarCredenciales(){

        int id=getId();

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("id_usuario",id);
        editor.putString("email",email);
        editor.putString("password",pass);

        editor.commit();
    }

    private boolean esAdmin(){

        boolean esAdmin=false;
        String codigo="";

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT ES_ADMIN FROM USUARIOS WHERE EMAIL='"+email+"' AND PASSWORD='"+pass+"'",null);

        while (c.moveToNext()){

            codigo=c.getString(0);

        }

        if(codigo.equals("S")){

            esAdmin=true;
        }

        return esAdmin;
    }


}

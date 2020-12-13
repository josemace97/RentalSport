package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaUsuariosFragment;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

public class ModificarUsrAdmin extends AppCompatActivity {

    private TextView id;
    private Button modificarDatos;
    private EditText nombre,password,email,tlf;
    private String nombreUsr,passUsr,emailUsr,tlfUsr;
    private int idUsr;

    private Bundle bundle;
    private ConexionBD conexionBD;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usr_admin);

        id = findViewById(R.id.txtIdAdmin);
        nombre = findViewById(R.id.edNombreAdmin);
        email = findViewById(R.id.edEmailAdmin);
        password = findViewById(R.id.edPassAdmin);
        tlf = findViewById(R.id.edTlfAdmin);

        modificarDatos = findViewById(R.id.btnModificarUsuario);

        bundle = new Bundle();
        conexionBD = new ConexionBD(this, "bd_rentalsport", null, 1);

        bundle = this.getIntent().getExtras();

        if (bundle != null) {

            idUsr = bundle.getInt("Id");
            nombreUsr = bundle.getString("nombre");
            passUsr = bundle.getString("password");
            emailUsr = bundle.getString("email");
            tlfUsr = bundle.getString("tlf");

            id.setText(String.valueOf(idUsr));
            nombre.setText(nombreUsr);
            email.setText(emailUsr);
            password.setText(passUsr);
            tlf.setText(tlfUsr);


        }

    }

    public void onClick(View view){

        SQLiteDatabase sqLiteDatabase=conexionBD.getWritableDatabase();

        if(sqLiteDatabase!=null){

            sqLiteDatabase.execSQL("UPDATE USUARIOS SET NOMBRE='"+nombre.getText().toString()+"',TELEFONO='"+tlf.getText().toString()+"',PASSWORD='"+password.getText().toString()+"',EMAIL='"+email.getText().toString()+"' WHERE ID="+idUsr);
            sqLiteDatabase.close();

           /* intent=new Intent(this,DrawerAdmin.class);
            startActivity(intent);*/

        }else{
            Toast.makeText(this,"La bd es nula",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this,"Se han modificado los datos del usuario "+id.getText().toString(),Toast.LENGTH_SHORT).show();

    }

}

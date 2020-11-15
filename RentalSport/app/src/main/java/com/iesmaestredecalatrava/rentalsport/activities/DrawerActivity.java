package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.constantes.Constantes;
import com.iesmaestredecalatrava.rentalsport.fragments.FragmentEnvioCorreo;
import com.iesmaestredecalatrava.rentalsport.fragments.FragmentInformacion;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaPistaFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaReservasAdminFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaReservasFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaUsuariosFragment;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.io.IOException;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ListaReservasFragment.OnFragmentInteractionListener,ListaUsuariosFragment.OnFragmentInteractionListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private SharedPreferences preferences;
    private ConexionBD conexionBD;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Intent llamada;
    private static int SOLICITUD_CALL_PHONE=1;

    private FirebaseAuth mAuth;

    private EditText destinatario,asunto,mensaje;
    private Button enviarCorreo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        conexionBD=new ConexionBD(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);

        mAuth= FirebaseAuth.getInstance();

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        llamada=new Intent(Intent.ACTION_CALL, Uri.parse("tel:926860268"));

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,new ListaReservasFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){


            case R.id.reservas:

                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new ListaReservasFragment());
                fragmentTransaction.commit();

                break;

            case R.id.preferencias:

                Intent i=new Intent(this,PreferenciasActivity.class);
                startActivity(i);

                break;

            case R.id.confirmarCambios:

                boolean confirmarDatos=preferences.getBoolean("cambiarDatos",false);

                if(confirmarDatos){

                   actualizarCredeciales();

                }else{

                    Toast.makeText(DrawerActivity.this,"No se han guardado los cambios",Toast.LENGTH_SHORT).show();
                }

                break;


            case R.id.paginaWeb:

                Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constantes.ACCESO_AYTO_PAG_WEB_ALMAGRO));
                startActivity(intent);

                break;

            case R.id.telefono:

                if(ActivityCompat.checkSelfPermission(DrawerActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){

                    startActivity(llamada);
                    Toast.makeText(DrawerActivity.this,"Permiso aceptado",Toast.LENGTH_SHORT).show();

                }else{

                    explicarUsoPermiso();
                    solicitarPermisoLlamada();
                }

                break;

            case R.id.envio:

                /*fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new FragmentEnvioCorreo());
                fragmentTransaction.commit();*/

                AlertDialog.Builder builder=new AlertDialog.Builder(DrawerActivity.this);
                View v= getLayoutInflater().inflate(R.layout.activity_envio_correo,null);

                destinatario=v.findViewById(R.id.destinatario);
                asunto=v.findViewById(R.id.asunto);
                mensaje=v.findViewById(R.id.mensaje);

                enviarCorreo=v.findViewById(R.id.enviarCorreo);

                enviarCorreo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent email=new Intent(Intent.ACTION_SEND);
                        email.setData(Uri.parse("mailto:"));
                        email.setType("text/plain");
                        email.putExtra(Intent.EXTRA_EMAIL,new String[]{destinatario.getText().toString()});
                        email.putExtra(Intent.EXTRA_SUBJECT,asunto.getText().toString());
                        email.putExtra(Intent.EXTRA_TEXT,mensaje.getText().toString());

                        startActivity(Intent.createChooser(email,"Envíar Gmail: "));

                    }
                });

                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.setTitle("CORREO ELECTRÓNICO");
                dialog.show();

                break;

            case R.id.informacion:

                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new FragmentInformacion());
                fragmentTransaction.commit();

                break;
        }

        return true;
    }

    private void actualizarCredeciales() {

        String email="",telefono="",password="";
        String nuevoEmail,nuevoTelefono,nuevaPassword;

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", MODE_PRIVATE);

        int id=sharedPreferences.getInt("id_usuario",0);

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT EMAIL,PASSWORD,TELEFONO " +
                "FROM USUARIOS WHERE ID="+id,null);


        while (c.moveToNext()){

            email=c.getString(0);
            password=c.getString(1);
            telefono=c.getString(2);

        }

        nuevoEmail=preferences.getString("correo",email);
        nuevoTelefono=preferences.getString("telefono",telefono);
        nuevaPassword=preferences.getString("contraseña",password);

        sqLiteDatabase.execSQL("UPDATE USUARIOS SET EMAIL='"+nuevoEmail+"'," +
                "TELEFONO='"+nuevoTelefono+"'," +
                "PASSWORD='"+nuevaPassword+"' " +
                "WHERE ID="+id);


        crearCuenta(nuevoEmail,nuevaPassword);

    }

    private void crearCuenta(String email,String password){

        Toast.makeText(DrawerActivity.this,"Nuevo email: "+email+",Pass: "+password,Toast.LENGTH_SHORT).show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(DrawerActivity.this,"Se ha creado un nuevo usuario",Toast.LENGTH_SHORT).show();

                        }else {



                        }
                    }
                });
    }


    private void explicarUsoPermiso(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){

            Toast.makeText(this,"Permiso concedido",Toast.LENGTH_SHORT).show();
            alertDialog();
        }

    }

    private void solicitarPermisoLlamada(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},SOLICITUD_CALL_PHONE);

        Toast.makeText(this,"Pedimos el permiso",Toast.LENGTH_SHORT).show();

    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==SOLICITUD_CALL_PHONE){

            startActivity(llamada);
            Toast.makeText(this,"Permiso confirmado",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"Permiso denegado",Toast.LENGTH_SHORT).show();
        }

    }

    private void alertDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setMessage("Sin el permiso no puedes realizar llamadas");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

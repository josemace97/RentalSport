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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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

    private EditText destinatario,asunto,mensaje;
    private Button enviarCorreo;
    private int id;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        conexionBD=new ConexionBD(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);

        mAuth= RegistroActivity.getFireBaseAuth();
        user=FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences=getSharedPreferences("credenciales", MODE_PRIVATE);

        id=sharedPreferences.getInt("id_usuario",0);

        getDatosUsuario();

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

                    Toast.makeText(DrawerActivity.this,"Debes configurar tus preferencias.",Toast.LENGTH_SHORT).show();
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
        boolean emailActualizado=false;
        boolean passActualizado=false;

        SharedPreferences sharedPreferences=getSharedPreferences("credenciales", MODE_PRIVATE);

        id=sharedPreferences.getInt("id_usuario",0);

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


        if(!email.equals(nuevoEmail)){

            sqLiteDatabase.execSQL("UPDATE USUARIOS SET EMAIL='"+nuevoEmail+"' WHERE ID="+id);

            cambiarEmail(email,nuevoEmail,password);

            emailActualizado=true;

        }

        if(!password.equals(nuevaPassword)){

            sqLiteDatabase.execSQL("UPDATE USUARIOS SET PASSWORD='"+nuevaPassword+"' WHERE ID="+id);

            cambiarPassword(email,password,nuevaPassword);

            passActualizado=true;
        }

        if(!telefono.equals(nuevoTelefono)){

            sqLiteDatabase.execSQL("UPDATE USUARIOS SET TELEFONO='"+nuevoTelefono+"' WHERE ID="+id);

        }

        if(!email.equals(nuevoEmail) && !password.equals(nuevaPassword)){

            if (!(emailActualizado && passActualizado)){

                crearNuevaCuenta(nuevoEmail,nuevaPassword);

                borrarCuenta(email,password);
            }

        }


    }

    private void cambiarEmail(String email, final String nuevoEmail, String password){

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(nuevoEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(DrawerActivity.this,"Se ha actualizado el email",Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(DrawerActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {

                            Toast.makeText(DrawerActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void cambiarPassword(String email, String password, final String nuevaPassword){

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(nuevaPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(DrawerActivity.this,"Se ha cambiado la pass",Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(DrawerActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {

                            Toast.makeText(DrawerActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void crearNuevaCuenta(String email,String password){

        /*Toast.makeText(DrawerActivity.this,"Nuevo email: "+email+",Pass: "+password,Toast.LENGTH_SHORT).show();*/

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(DrawerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(DrawerActivity.this, "Se ha creado el usuario.", Toast.LENGTH_SHORT).show();

                        }else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                Toast.makeText(DrawerActivity.this, "Error", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(DrawerActivity.this, "No se ha podido realizar el registro.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });
    }

    private void getDatosUsuario(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT EMAIL,PASSWORD,TELEFONO FROM USUARIOS WHERE ID="+id,null);

        while (c.moveToNext()){

            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("cambiarDatos",false);
            editor.putString("correo",c.getString(0));
            editor.putString("contraseña",c.getString(1));
            editor.putString("telefono",c.getString(2));
            editor.commit();
        }



        sqLiteDatabase.close();
        c.close();
    }

    private void borrarCuenta(String email,String password){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

         user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(DrawerActivity.this,"Se ha borrado la cuenta",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

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

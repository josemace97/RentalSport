package com.iesmaestredecalatrava.rentalsport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.fragments.FragmentInformacion;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaPistaFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaReservasAdminFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaUsuariosFragment;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;

import java.util.ArrayList;

public class DrawerAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
ListaUsuariosFragment.OnFragmentInteractionListener,
        ListaPistaFragment.OnFragmentInteractionListener,
ListaReservasAdminFragment.OnFragmentInteractionListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_admin);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawerAdmin);
        navigationView=findViewById(R.id.navigationViewAdmin);

        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,new ListaUsuariosFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if(item.getItemId()==R.id.usuarios){

            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new ListaUsuariosFragment());
            fragmentTransaction.commit();

        }

        else if(item.getItemId()==R.id.pistas){

            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new ListaPistaFragment());
            fragmentTransaction.commit();

        }else{

            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new ListaReservasAdminFragment());
            fragmentTransaction.commit();

        }

        return false;
    }

    private void borrarUsuario(){


    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

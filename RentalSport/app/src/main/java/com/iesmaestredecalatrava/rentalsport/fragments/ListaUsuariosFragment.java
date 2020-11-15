package com.iesmaestredecalatrava.rentalsport.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.activities.ListadoPistasActivity;
import com.iesmaestredecalatrava.rentalsport.activities.ModificarUsrAdmin;
import com.iesmaestredecalatrava.rentalsport.activities.RecintosActivity;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaUsuariosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaUsuariosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerUsuarios;
    private ArrayList<Usuario> listaUsuarios;
    private ConexionBD conexionBD;
    private AdaptadorUsuarios adaptadorUsuarios;
    private Bundle bundle;

    private int id;
    private String nombre,password,email,tlf;

    public ListaUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaUsuariosFragment newInstance(String param1, String param2) {
        ListaUsuariosFragment fragment = new ListaUsuariosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_lista_usuarios, container, false);
        recyclerUsuarios=view.findViewById(R.id.fragmentRecyclerUsuarios);
        listaUsuarios=new ArrayList<>();
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));

        conexionBD=new ConexionBD(getContext());

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM USUARIOS",null);

        while (c.moveToNext()){

            String nombre=c.getString(1);

            Toast.makeText(getContext(),"Nombre: "+nombre,Toast.LENGTH_SHORT).show();
        }

        bundle=new Bundle();

        adaptadorUsuarios=new AdaptadorUsuarios(listaUsuarios);

        adaptadorUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id=listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(v)).getId();
                nombre=listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(v)).getNombre();
                email=listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(v)).getEmail();
                password=listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(v)).getPassword();
                tlf=listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(v)).getTelefono();

                bundle.putInt("Id",id);
                bundle.putString("nombre",nombre);
                bundle.putString("email",email);
                bundle.putString("password",password);
                bundle.putString("tlf",tlf);


            }
        });

        Swipe swipe=new Swipe(getContext(),recyclerUsuarios,200,true) {
            @Override
            public void instantiateMySwipe(final RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                buffer.add(new MyButton(ListaUsuariosFragment.this,
                        "", 0,
                        R.drawable.ic_delete, Color.parseColor("#33D1FF"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {
                                borrarCuenta(email,password);
                                id=listaUsuarios.get(pos).getId();
<<<<<<< HEAD

=======
                                listaUsuarios.remove(viewHolder.getPosition());
>>>>>>> 789b2de8a4e4a8077bc993002556efb43e51c93c
                                borrarUsuario(id);
                            }
                        }));

            }
        };

        recyclerUsuarios.setAdapter(adaptadorUsuarios);

        cargarUsuarios();


        return view;
    }

    private void borrarUsuario(int id) {

        SQLiteDatabase sqLiteDatabase=conexionBD.getWritableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM USUARIOS WHERE ID="+id);

        Toast.makeText(getContext(),
                "Se ha eliminado el usuario "+id,Toast.LENGTH_SHORT).show();
    }

    private void borrarCuenta(String email,String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Toast.makeText(getContext(),"Email:"+user.getEmail(),Toast.LENGTH_SHORT).show();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }

    private void cargarUsuarios(){

        int id;
        String nombre,email,password,telefono;

        SQLiteDatabase db=conexionBD.getReadableDatabase();

        Usuario usuario;

        Cursor cursor=db.rawQuery("SELECT * FROM USUARIOS",null);

        while (cursor.moveToNext()){

            id=cursor.getInt(0);
            nombre=cursor.getString(1);
            email=cursor.getString(2);
            password=cursor.getString(3);
            telefono=cursor.getString(4);

            usuario=new Usuario(id,nombre,email,password,telefono);

            listaUsuarios.add(usuario);

        }

    }


    /*public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case 1:

                Intent i=new Intent(getContext(), ModificarUsrAdmin.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

            case 2:

               eliminarUsuario(item.getGroupId());

                break;
        }
        return super.onContextItemSelected(item);
    }*/

     private void eliminarUsuario(int id){

        SQLiteDatabase sqLiteDatabase=conexionBD.getWritableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM USUARIOS WHERE ID="+id);

        Toast.makeText(getContext(),"Id usuario: "+id,Toast.LENGTH_SHORT).show();

     }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

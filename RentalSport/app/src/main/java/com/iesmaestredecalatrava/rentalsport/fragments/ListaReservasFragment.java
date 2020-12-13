package com.iesmaestredecalatrava.rentalsport.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import java.time.*;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.TileOverlay;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.activities.Galeria;
import com.iesmaestredecalatrava.rentalsport.activities.ListadoPistasActivity;
import com.iesmaestredecalatrava.rentalsport.activities.ListadoReservasActivity;
import com.iesmaestredecalatrava.rentalsport.activities.MainActivity;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorReservas;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaReservasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReservasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerReservas;
    private ArrayList<Reserva> listarReservas;
    private ConexionBD conexionBD;
    private AdaptadorReservas adaptadorReservas;
    private int idUsuario,posicion;
    private String nombrePista;
    private int idPista;
    private String fecha;
    private SimpleDateFormat simpleDateFormat;
    private Date fechaActual;
    private Date fechaReserva;
    private FloatingActionButton menuPrincipal;
    private Intent i;


    private SharedPreferences sharedPreferences;


    public ListaReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaReservasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaReservasFragment newInstance(String param1, String param2) {
        ListaReservasFragment fragment = new ListaReservasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {             mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_lista_reservas, container, false);
        recyclerReservas=view.findViewById(R.id.fragmentRecyclerReservas);
        listarReservas=new ArrayList<>();
        recyclerReservas.setLayoutManager(new LinearLayoutManager(getContext()));
        menuPrincipal=view.findViewById(R.id.fabMenuPrincipal);
        conexionBD=new ConexionBD(getContext());

        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");


        sharedPreferences= getContext().getSharedPreferences("credenciales", MODE_PRIVATE);

        adaptadorReservas=new AdaptadorReservas(listarReservas);

        recyclerReservas.setAdapter(adaptadorReservas);

        idUsuario=sharedPreferences.getInt("id_usuario",0);

        menuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i=new Intent(ListaReservasFragment.this.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        cargarReservas();

        Swipe swipe=new Swipe(getContext(),recyclerReservas,200,true) {
            @Override
            public void instantiateMySwipe(final RecyclerView.ViewHolder viewHolder, final List<MyButton> buffer) {

                buffer.add(new MyButton(ListaReservasFragment.this,
                        "", 0,
                        R.drawable.ic_delete, Color.parseColor("#E40026"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {

                                posicion=pos;

                                nombrePista=listarReservas.get(pos).getNombrePista();
                                fecha=listarReservas.get(pos).getFechaReserva();

                                Toast.makeText(getContext(),"Fecha seleccionada: "+fecha,Toast.LENGTH_SHORT).show();

                                idPista=getIdPista();

                                final AlertDialog.Builder cancelarReserva=new AlertDialog.Builder(getContext());
                                        cancelarReserva.setMessage("¿Quieres cancelar tu reserva?")
                                                .setCancelable(false)
                                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        try {
                                                            borrarReserva();
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog cuadroDialogo= cancelarReserva.create();
                                        cuadroDialogo.show();


                                    }


                        }));

                buffer.add(new MyButton(ListaReservasFragment.this,
                        "Realizar pago", 0,
                        R.drawable.ic_euro, Color.parseColor("#0A00E4"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {

                                Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bizum.es/"));
                                startActivity(intent);

                            }
                        }));

            }
        };

        return view;
    }

    private void borrarReserva() throws ParseException {

       SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        fechaActual=new Date();
        fechaReserva=simpleDateFormat.parse(fecha);

        String fecha1=simpleDateFormat.format(fechaActual);
        String fecha2=simpleDateFormat.format(fechaReserva);

        if(fecha1.equals(fecha2)){

           Toast.makeText(getContext(),"No se puede cancelar la reserva.Tienes una asignada para hoy",Toast.LENGTH_SHORT).show();

        }else if(fechaActual.after(fechaReserva)){

            listarReservas.remove(posicion);
            adaptadorReservas.notifyDataSetChanged();

            sqLiteDatabase.execSQL("DELETE FROM RESERVAS WHERE USUARIO="+idUsuario+" AND PISTA="+idPista+" AND FECHA='"+fecha+"'");

            sqLiteDatabase.close();

            Toast.makeText(getContext(),"Se ha cancelado tu reserva.",Toast.LENGTH_SHORT).show();

        }else if(fechaActual.before(fechaReserva)){

                listarReservas.remove(posicion);
                adaptadorReservas.notifyDataSetChanged();

                sqLiteDatabase.execSQL("DELETE FROM RESERVAS WHERE USUARIO="+idUsuario+" AND PISTA="+idPista+" AND FECHA='"+fecha+"'");

                sqLiteDatabase.close();

                Toast.makeText(getContext(),"Se ha eliminado la reserva de tu lista.",Toast.LENGTH_SHORT).show();
        }


    }

    private int getIdPista(){

        int id=0;

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT ID FROM PISTAS WHERE NOMBRE='"+nombrePista+"'",null);

        while (cursor.moveToNext()){

            id=cursor.getInt(0);
        }

        return id;
    }

    private void cargarReservas(){

        SQLiteDatabase sqLiteDatabase = conexionBD.getReadableDatabase();

        String id=String.valueOf(idUsuario);

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT P.IMG,P.NOMBRE,R.FECHA " +
                "FROM RESERVAS R,USUARIOS U,PISTAS P" +
                " WHERE R.USUARIO=U.ID AND R.PISTA=P.ID AND R.USUARIO="+idUsuario,null);

        while (cursor.moveToNext()) {

            listarReservas.add(new Reserva(cursor.getString(1), cursor.getString(2),cursor.getBlob(0)));

        }

    }

    /*private void getIdHorario(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT H.ID" +
              "FROM RESERVAS R,USUARIOS U,PISTAS P,HORARIOS H\n" +
              "WHERE R.USUARIO=U.ID AND R.PISTA=P.ID AND R.HORARIO=H.ID AND " +
              "R.USUARIO="+idUsuario+" AND R.FECHA='"+fecha+"'",null);)

        while (cursor.moveToNext()){

            idHorario=cursor.getInt(0);
        }
    }*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences sharedPreferences=context.getSharedPreferences("credenciales",0);
        idUsuario=sharedPreferences.getInt("id_usuario",0);

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

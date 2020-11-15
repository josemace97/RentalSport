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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorReservas;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.util.ArrayList;
import java.util.List;

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
    private int idUsuario,idHorario,posicion;
    private Bundle bundle;
    private byte [] imagen;
    private Bitmap bitmap;
    private String nombrePista;

    private EditText horaInicio,horaFin,precio,tiempoAlquiler;
    private ImageView img;
    private FloatingActionButton fabEliminarReserva,fabCancelar;
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
        conexionBD=new ConexionBD(getContext());
        bundle=new Bundle();

        sharedPreferences= getContext().getSharedPreferences("credenciales", MODE_PRIVATE);

        adaptadorReservas=new AdaptadorReservas(listarReservas);

        recyclerReservas.setAdapter(adaptadorReservas);

        idUsuario=sharedPreferences.getInt("id_usuario",0);

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

                                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                View v= getLayoutInflater().inflate(R.layout.activity_detalle_reserva,null);

                                img=v.findViewById(R.id.imgFoto);
                                horaInicio=v.findViewById(R.id.edhoraInicio);
                                horaFin=v.findViewById(R.id.edhoraFin);
                                precio=v.findViewById(R.id.edPrecio);
                                tiempoAlquiler=v.findViewById(R.id.edTiempoAlquiler);
                                fabEliminarReserva=v.findViewById(R.id.fabCancelarReserva);

                                cargarDatosReserva();

                                builder.setView(v);
                                final AlertDialog dialog = builder.create();
                                dialog.show();


                                fabEliminarReserva.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        dialog.cancel();

                                        final AlertDialog.Builder cancelarReserva=new AlertDialog.Builder(getContext());
                                        cancelarReserva.setMessage("¿Quieres cancelar tu reserva?")
                                                .setCancelable(false)
                                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                          listarReservas.remove(posicion);
                                                          adaptadorReservas.notifyDataSetChanged();
                                                          borrarReserva();
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
                                });

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

    private void borrarReserva(){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM RESERVAS WHERE USUARIO="+idUsuario+" AND HORARIO="+idHorario);

        sqLiteDatabase.close();
    }

    private String getFechaReserva(String nombrePista){

        String fechaReserva="";

        int idHorario=getIdHorario(nombrePista);

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT R.FECHA\n" +
                "FROM RESERVAS R,USUARIOS U,PISTAS P\n" +
                "WHERE R.USUARIO=U.ID AND R.PISTA=P.ID AND R.HORARIO="+idHorario,null);


        while (c.moveToNext()){

            fechaReserva=c.getString(0);

        }


        return fechaReserva;
    }

    private int getIdHorario(String nombrePista){

        int id=0;

        String horainicio=sharedPreferences.getString("hora_inicio","");
        String horafin=sharedPreferences.getString("hora_fin","");

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT H.ID FROM HORARIOS H,PISTAS P WHERE " +
                "H.PISTA=P.ID AND H.HORA_INICIO='"+horainicio+"' AND H.HORA_FIN='"+horafin+"' " +
                " AND P.NOMBRE='"+nombrePista+"'",null);

        while (c.moveToNext()){

            id=c.getInt(0);
        }

        idHorario=id;


        return id;

    }


    private void cargarDatosReserva(){

        String fechaReserva=getFechaReserva(nombrePista);


        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT P.IMG,H.HORA_INICIO,H.HORA_FIN,printf('%.2f',H.PRECIO)"+
                " FROM PISTAS P,HORARIOS H,RESERVAS R,USUARIOS U"+
                " WHERE P.ID=R.PISTA AND P.NOMBRE='"+nombrePista+"' AND R.USUARIO=U.ID AND R.HORARIO=H.ID AND R.HORARIO="+idHorario+" AND R.FECHA='"+fechaReserva+"'",null);


        while (c.moveToNext()){

           imagen=c.getBlob(0);
           horaInicio.setText("Hora inicio: "+c.getString(1)+" horas");
           horaFin.setText("Hora fin: "+c.getString(2)+" horas");
           precio.setText("Precio: "+c.getDouble(3)+"0 euros");
           tiempoAlquiler.setText("Tiempo alquiler: 1h");
        }

        if(imagen!=null){

            Bitmap bitmap=BitmapFactory.decodeByteArray(imagen,0,imagen.length);
            img.setImageBitmap(bitmap);
        }
    }


    private void cargarReservas(){

        SQLiteDatabase sqLiteDatabase = conexionBD.getReadableDatabase();

        String id=String.valueOf(idUsuario);

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT P.IMG,P.NOMBRE,R.FECHA " +
                "FROM RESERVAS R,USUARIOS U,PISTAS P" +
                " WHERE R.USUARIO=U.ID AND R.PISTA=P.ID",null);

        while (cursor.moveToNext()) {

            listarReservas.add(new Reserva(cursor.getString(1), cursor.getString(2),cursor.getBlob(0)));


        }

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

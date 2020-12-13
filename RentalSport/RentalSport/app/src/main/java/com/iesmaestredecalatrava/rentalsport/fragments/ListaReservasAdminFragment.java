package com.iesmaestredecalatrava.rentalsport.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorReservas;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorReservasAdmin;
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.swipe.ButtomClickListener;
import com.iesmaestredecalatrava.rentalsport.swipe.Swipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaReservasAdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaReservasAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReservasAdminFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerReservas;
    private ArrayList<Reserva> listaReservas;
    private ConexionBD conexionBD;
    private AdaptadorReservasAdmin adaptadorReservas;
    private Bundle bundle;
    private int posicion;


    public ListaReservasAdminFragment() {


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaReservasAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaReservasAdminFragment newInstance(String param1, String param2) {
        ListaReservasAdminFragment fragment = new ListaReservasAdminFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_lista_reservas_admin, container, false);
        recyclerReservas=view.findViewById(R.id.fragmentRecyclerReservasAdmin);
        listaReservas=new ArrayList<>();
        recyclerReservas.setLayoutManager(new LinearLayoutManager(getContext()));
        bundle=new Bundle();

        conexionBD=new ConexionBD(getContext());

        adaptadorReservas=new AdaptadorReservasAdmin(listaReservas);
        recyclerReservas.setAdapter(adaptadorReservas);

        cargarReservas();


        Swipe swipe=new Swipe(getContext(),recyclerReservas,200,true) {
            @Override
            public void instantiateMySwipe(final RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                buffer.add(new MyButton(ListaReservasAdminFragment.this,
                        "", 0,
                        R.drawable.ic_delete, Color.parseColor("#EB073B"),
                        new ButtomClickListener() {
                            @Override
                            public void onClick(int pos) {

                                int id=listaReservas.get(pos).getId();

                                borrarReserva(id);
                            }
                        }));

            }
        };

        return view;
    }

    private void cargarReservas(){

        int id;
        String cliente,pista,horaInicio,horaFin,fecha;

        SQLiteDatabase db=conexionBD.getReadableDatabase();

        Reserva reserva;

        Cursor cursor=db.rawQuery("SELECT R.ID,U.NOMBRE,P.NOMBRE,H.HORA_INICIO,H.HORA_FIN,R.FECHA " +
                "FROM RESERVAS R,USUARIOS U,PISTAS P,HORARIOS H" +
                " WHERE R.USUARIO=U.ID AND R.PISTA=P.ID AND R.HORARIO=H.ID",null);

        while (cursor.moveToNext()){

            id=cursor.getInt(0);
            cliente=cursor.getString(1);
            pista=cursor.getString(2);
            horaInicio=cursor.getString(3);
            horaFin=cursor.getString(4);
            fecha=cursor.getString(5);

            reserva=new Reserva(id,cliente,pista,horaInicio,horaFin,fecha);

            listaReservas.add(reserva);

        }

    }

    private void borrarReserva(int id){

        SQLiteDatabase sqLiteDatabase=conexionBD.getReadableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM RESERVAS WHERE ID="+id);

        sqLiteDatabase.close();

        listaReservas.remove(posicion);
        adaptadorReservas.notifyDataSetChanged();

        Toast.makeText(getContext(),"Se ha eliminado la reserva",Toast.LENGTH_SHORT).show();
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

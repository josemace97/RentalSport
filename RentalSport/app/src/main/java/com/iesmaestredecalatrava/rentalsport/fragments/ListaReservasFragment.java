package com.iesmaestredecalatrava.rentalsport.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.iesmaestredecalatrava.rentalsport.adaptadores.AdaptadorUsuarios;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.util.ArrayList;

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
    private int idUsuario;
    private Bundle bundle;


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
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

        adaptadorReservas=new AdaptadorReservas(listarReservas);

        recyclerReservas.setAdapter(adaptadorReservas);

        cargarReservas();

        return view;
    }

    private void cargarReservas(){

        SQLiteDatabase sqLiteDatabase = conexionBD.getReadableDatabase();

        String id=String.valueOf(idUsuario);

        Toast.makeText(this.getActivity(),id+" se ha recuperado el id",Toast.LENGTH_SHORT).show();

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

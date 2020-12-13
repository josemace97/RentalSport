package com.iesmaestredecalatrava.rentalsport.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.activities.AniadirPistaActivity;
import com.iesmaestredecalatrava.rentalsport.activities.CuadroDeportes;
import com.iesmaestredecalatrava.rentalsport.activities.CuadroHorarios;
import com.iesmaestredecalatrava.rentalsport.activities.CuadroPista;
import com.iesmaestredecalatrava.rentalsport.activities.CuadroUbicacion;
import com.iesmaestredecalatrava.rentalsport.activities.ListadoPistasActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaPistaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaPistaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPistaFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton aniadirUbicacion,aniadirHorario,
            aniadirPista,aniadirDeporte,aniadir;
    private Intent i;
    private CardView futbol,basket,futsal,tenis,padel;
    private String deporte;
    private Bundle bundle;

    private OnFragmentInteractionListener mListener;

    public ListaPistaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaPistaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaPistaFragment newInstance(String param1, String param2) {
        ListaPistaFragment fragment = new ListaPistaFragment();
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
        View v=inflater.inflate(R.layout.fragment_lista_pista, container, false);
       /* aniadir=v.findViewById(R.id.fabAniadirPista);*/

        aniadirUbicacion=v.findViewById(R.id.fab2);
        aniadirPista=v.findViewById(R.id.fab3);
        aniadirHorario=v.findViewById(R.id.fab4);

        futbol=v.findViewById(R.id.futbol);
        basket=v.findViewById(R.id.basket);
        futsal=v.findViewById(R.id.futsal);
        tenis=v.findViewById(R.id.tenis);
        padel=v.findViewById(R.id.padel);

        /*futbol.setOnClickListener((View.OnClickListener) ListaPistaFragment.this);
        basket.setOnClickListener((View.OnClickListener) ListaPistaFragment.this);
        futsal.setOnClickListener((View.OnClickListener) ListaPistaFragment.this);
        tenis.setOnClickListener((View.OnClickListener) ListaPistaFragment.this);
        padel.setOnClickListener((View.OnClickListener) ListaPistaFragment.this);*/





        /*aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getContext(),AniadirPistaActivity.class);
                startActivity(i);
            }
        });*/


        aniadirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fm=getFragmentManager().beginTransaction();
                CuadroUbicacion cuadroUbicacion=new CuadroUbicacion();
                cuadroUbicacion.show(fm,"tagAlerta");

            }
        });

        aniadirPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fm=getFragmentManager().beginTransaction();
                CuadroPista cuadroPista=new CuadroPista();
                cuadroPista.show(fm,"tagAlerta");
            }
        });

        aniadirHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fm=getFragmentManager().beginTransaction();
                CuadroHorarios cuadroHorarios=new CuadroHorarios();
                cuadroHorarios.show(fm,"tagAlerta");
            }
        });

        futbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bundle=new Bundle();

                deporte="Fútbol";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(ListaPistaFragment.this.getContext(),ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        futsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle=new Bundle();

                deporte="Futsal";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(ListaPistaFragment.this.getContext(),ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle=new Bundle();

                deporte="Basket";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(ListaPistaFragment.this.getContext(),ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        tenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle=new Bundle();

                deporte="Tenis";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(ListaPistaFragment.this.getContext(),ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        padel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle=new Bundle();

                deporte="Pádel";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(ListaPistaFragment.this.getContext(),ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        return v;
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

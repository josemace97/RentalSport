package com.iesmaestredecalatrava.rentalsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.constantes.Constantes;

public class FragmentInformacion extends Fragment {

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceStated) {

        View view = layoutInflater.inflate(R.layout.fragment_informacion, container, false);

        final TextView descripcion = view.findViewById(R.id.txtDescripcion);

        descripcion.setText(Constantes.DESCRIPCION);

        return view;

    }

}



package com.iesmaestredecalatrava.rentalsport.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.iesmaestredecalatrava.rentalsport.R;

public class FragmentEnvioCorreo extends Fragment {

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceStated){

        View view= layoutInflater.inflate(R.layout.fragment_envio_correo,container,false);

        final EditText direccionCorreo=view.findViewById(R.id.correo);
        final EditText asunto=view.findViewById(R.id.asunto);
        final EditText mensaje=view.findViewById(R.id.mensaje);
        Button enviar=view.findViewById(R.id.enviarMensaje);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email=new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{direccionCorreo.getText().toString()});
                email.putExtra(Intent.EXTRA_REFERRER_NAME,"deporte@almagro.es");
                email.putExtra(Intent.EXTRA_SUBJECT,asunto.getText().toString());
                email.putExtra(Intent.EXTRA_TEXT,mensaje.getText().toString());

                startActivity(Intent.createChooser(email,"Envíar Gmail: "));


            }
        });

        return view;
    }
}

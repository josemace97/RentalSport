package com.iesmaestredecalatrava.rentalsport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.iesmaestredecalatrava.rentalsport.R;

public class DeportesActivity extends AppCompatActivity implements View.OnClickListener{

    CardView futbol,basket,futsal,tenis,padel;
    private String deporte;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deportes);

        futbol=findViewById(R.id.futbol);
        basket=findViewById(R.id.basket);
        futsal=findViewById(R.id.futsal);
        tenis=findViewById(R.id.tenis);
        padel=findViewById(R.id.padel);

        futbol.setOnClickListener(this);
        basket.setOnClickListener(this);
        futsal.setOnClickListener(this);
        tenis.setOnClickListener(this);
        padel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch(v.getId()){

            case R.id.futbol:

                bundle=new Bundle();

                deporte="Fútbol";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(this,ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

            case R.id.futsal:

                bundle=new Bundle();

                deporte="Futsal";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(this,ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

            case R.id.basket:

                bundle=new Bundle();

                deporte="Basket";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(this,ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

            case R.id.padel:

                bundle=new Bundle();

                deporte="Pádel";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(this,ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

            case R.id.tenis:

                bundle=new Bundle();

                deporte="Tenis";

                bundle.putString("filtroDeporte",deporte);

                i=new Intent(this,ListadoPistasActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                break;

        }
    }
}

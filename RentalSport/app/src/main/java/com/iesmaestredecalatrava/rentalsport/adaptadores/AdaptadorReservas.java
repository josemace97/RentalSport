package com.iesmaestredecalatrava.rentalsport.adaptadores;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.activities.DetalleReservaActivity;
import com.iesmaestredecalatrava.rentalsport.activities.RecintosActivity;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;

import java.util.ArrayList;

public class AdaptadorReservas extends RecyclerView.Adapter<AdaptadorReservas.ViewHolderReservas> {
    @NonNull
    ArrayList<Reserva> listaReservas;

    public AdaptadorReservas(ArrayList<Reserva> listaReservas){

        this.listaReservas=listaReservas;
    }

    @Override
    public ViewHolderReservas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_reserva_usuario,null,false);

        return new ViewHolderReservas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderReservas holder, int position) {

        final AdaptadorReservas.ViewHolderReservas viewHolderReservas=holder;
        final Reserva reserva=listaReservas.get(position);

        holder.nombre.setText(listaReservas.get(position).getNombrePista());

        byte [] imagenPista=listaReservas.get(position).getFoto();

        Bitmap bitmap=BitmapFactory.decodeByteArray(imagenPista,0,imagenPista.length);
        holder.foto.setImageBitmap(bitmap);
        holder.fecha.setText(listaReservas.get(position).getFechaReserva());
        holder.iconoIzq.setImageResource(R.drawable.ic_chevron_left);

        /*viewHolderReservas.infoReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(viewHolderReservas.infoReserva.getContext(), DetalleReservaActivity.class);

                i.putExtra(viewHolderReservas.infoReserva.getContext().
                        getString(R.string.fecha_reserva),reserva.getFechaReserva());

                viewHolderReservas.infoReserva.getContext().startActivity(i);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static Bitmap convertirByteToImg(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public class ViewHolderReservas extends RecyclerView.ViewHolder {

        TextView nombre,fecha;
        ImageView foto,iconoIzq;
        CardView cardView;

        public ViewHolderReservas(@NonNull View itemView) {
            super(itemView);

            nombre= itemView.findViewById(R.id.txtNombrePista);
            foto=itemView.findViewById(R.id.idImagen);
            fecha=itemView.findViewById(R.id.txtFecha);
            iconoIzq=itemView.findViewById(R.id.imgIzq);

            cardView=itemView.findViewById(R.id.cardPistas);
        }
    }
}

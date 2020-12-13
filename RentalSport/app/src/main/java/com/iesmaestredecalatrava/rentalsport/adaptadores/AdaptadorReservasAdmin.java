package com.iesmaestredecalatrava.rentalsport.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.modelo.Reserva;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;

import java.util.ArrayList;

public class AdaptadorReservasAdmin extends RecyclerView.Adapter<AdaptadorReservasAdmin.ViewHolderDatos>
implements View.OnClickListener{

    private ArrayList<Reserva> reservas;
    private View.OnClickListener listener;

    public AdaptadorReservasAdmin(ArrayList<Reserva> reservas){

        this.reservas=reservas;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_reserva_admin,null,false);

        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.id.setText(String.valueOf(reservas.get(position).getId()));
        holder.cliente.setText(reservas.get(position).getCliente());
        holder.nombrePista.setText(reservas.get(position).getNombrePista());
        holder.horaInicio.setText(reservas.get(position).getHoraInicio());
        holder.horaFin.setText(reservas.get(position).getHoraFin());
        holder.fechaReserva.setText(reservas.get(position).getFechaReserva());
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView id,cliente,horaInicio,horaFin,fechaReserva,nombrePista;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            id=itemView.findViewById(R.id.txtId);
            cliente=itemView.findViewById(R.id.txtCliente);
            nombrePista=itemView.findViewById(R.id.txtInstalacion);
            horaInicio=itemView.findViewById(R.id.txtHoraInicio);
            horaFin=itemView.findViewById(R.id.txtHoraFin);
            fechaReserva=itemView.findViewById(R.id.txtFecha);
        }
    }
}

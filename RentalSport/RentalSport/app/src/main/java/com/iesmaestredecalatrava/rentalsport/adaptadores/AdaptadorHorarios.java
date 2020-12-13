package com.iesmaestredecalatrava.rentalsport.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.modelo.Horario;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorHorarios extends RecyclerView.Adapter<AdaptadorHorarios.ViewHolderHorarios>
implements View.OnClickListener{

    private ArrayList<Horario> horarios;
    private View.OnClickListener listener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<CardView> cardViews=new ArrayList<>();

    public AdaptadorHorarios(ArrayList<Horario> horarios){
        this.horarios = horarios;
    }
    @NonNull
    @Override
    public AdaptadorHorarios.ViewHolderHorarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_horario,null,false);

        view.setOnClickListener(this);

        return new ViewHolderHorarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorHorarios.ViewHolderHorarios holder, int position) {

        holder.horaInicio.setText(horarios.get(position).getHoraInicio()+" hrs");
        holder.horaFin.setText(horarios.get(position).getHoraFin()+" hrs.");

    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;
    }

    @Override
    public void onClick(View v) {

        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderHorarios extends RecyclerView.ViewHolder {

        TextView horaInicio,horaFin;
        CardView cardView;

        public ViewHolderHorarios(@NonNull final View itemView) {
            super(itemView);

            horaInicio=itemView.findViewById(R.id.textHoraInicio);
            horaFin=itemView.findViewById(R.id.textHoraFin);

           /* cardView=itemView.findViewById(R.id.cardHorario);*/

        }
    }
}

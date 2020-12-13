package com.iesmaestredecalatrava.rentalsport.adaptadores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.modelo.Usuario;

import java.util.ArrayList;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolderUsuarios>
implements View.OnClickListener{

    private ArrayList<Usuario> usuarios;
    private View.OnClickListener listener;

    public AdaptadorUsuarios(ArrayList<Usuario> usuarios){

        this.usuarios=usuarios;
    }

    @NonNull
    @Override
    public ViewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_usuario,null,false);

        view.setOnClickListener(this);

        return new ViewHolderUsuarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsuarios holder, int position) {

        holder.id.setText(String.valueOf(usuarios.get(position).getId()));
        holder.nombre.setText(usuarios.get(position).getNombre());
        holder.email.setText(usuarios.get(position).getEmail());
        holder.password.setText(usuarios.get(position).getPassword());
        holder.telefono.setText(usuarios.get(position).getTelefono());

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

   @Override
    public void onClick(View v) {

        if (listener!=null){
            listener.onClick(v);
        }
    }


    public static class ViewHolderUsuarios extends RecyclerView.ViewHolder {

        TextView id,nombre,email,telefono,password;
        CardView cardView;

        public ViewHolderUsuarios(@NonNull View itemView) {
            super(itemView);

            id=itemView.findViewById(R.id.txtId);
            nombre=itemView.findViewById(R.id.txtNombre);
            email= itemView.findViewById(R.id.txtEmail);
            password=itemView.findViewById(R.id.txtPass);
            telefono=itemView.findViewById(R.id.txtTlf);

            cardView=itemView.findViewById(R.id.cardUsuarios);

        }


    }

}

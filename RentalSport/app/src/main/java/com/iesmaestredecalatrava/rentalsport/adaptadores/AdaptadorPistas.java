package com.iesmaestredecalatrava.rentalsport.adaptadores;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.activities.MainActivity;
import com.iesmaestredecalatrava.rentalsport.activities.RecintosActivity;
import com.iesmaestredecalatrava.rentalsport.modelo.Pista;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;

import java.util.ArrayList;

public class AdaptadorPistas extends RecyclerView.Adapter<AdaptadorPistas.ViewHolderPistas>
implements View.OnClickListener{

    ArrayList<Pista> listaPistas;

    private View.OnClickListener listener;

    public AdaptadorPistas(ArrayList<Pista> listaPistas) {

        this.listaPistas = listaPistas;
    }

    @Override
    public ViewHolderPistas onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pistas,null,false);

        view.setOnClickListener(this);

        return new ViewHolderPistas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPistas holder, int position) {


        holder.nombre.setText(listaPistas.get(position).getNombre());

        byte [] imagenPista=listaPistas.get(position).getFoto();

        Bitmap bitmap=BitmapFactory.decodeByteArray(imagenPista,0,imagenPista.length);
        holder.foto.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return listaPistas.size();
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

    public static Bitmap convertirByteToImg(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public class ViewHolderPistas extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener{

        TextView nombre;
        ImageView foto;

        CardView cardView;

        public ViewHolderPistas(View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.idNombre);
            foto=itemView.findViewById(R.id.idImagen);

            cardView=itemView.findViewById(R.id.cardPistas);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Opciones: ");
            menu.add(this.getAdapterPosition(),1,0,"Cómo llegar");
            menu.add(this.getAdapterPosition(),2,1,"Galería");

        }
    }

}

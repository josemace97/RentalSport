package com.iesmaestredecalatrava.rentalsport.adaptadores;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iesmaestredecalatrava.rentalsport.R;
import com.iesmaestredecalatrava.rentalsport.persistencia.ConexionBD;
import com.iesmaestredecalatrava.rentalsport.recursos.BitmapUtils;

import java.io.IOException;

public class AdaptadorGaleria extends BaseAdapter {

    Context context;
    Bitmap[] imagenes;
    int background;
    ConexionBD conexionBD;
    SparseArray<Bitmap> imagenesEscaladas = new SparseArray<Bitmap>(7);

    public AdaptadorGaleria(Context context,Bitmap [] imagenes)
    {
        super();
        this.imagenes = imagenes;
        this.context = context;
        this.conexionBD=new ConexionBD(context);

        try {
            conexionBD.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Gallery1);
        background = typedArray.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        typedArray.recycle();
    }

    @Override
    public int getCount()
    {
        return imagenes.length;
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imagen = new ImageView(context);


        if (imagenesEscaladas.get(position) == null)
        {
            imagenesEscaladas.put(position, imagenes[position]);

        }

        imagen.setImageBitmap(imagenesEscaladas.get(position));

        imagen.setBackgroundResource(background);

        return imagen;
    }
}

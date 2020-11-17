package com.iesmaestredecalatrava.rentalsport.swipe;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.iesmaestredecalatrava.rentalsport.activities.ListadoPistasActivity;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaReservasFragment;
import com.iesmaestredecalatrava.rentalsport.fragments.ListaUsuariosFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Swipe extends ItemTouchHelper.SimpleCallback {

    int boton;
    private RecyclerView recyclerView;
    private List<MyButton> botonLista;
    private GestureDetector detector;
    private int posicion=-1;
    private float deslizador=0.5f;
    private Map<Integer, List<MyButton>> botonBuffer;
    private Queue<Integer> removeQueue;
    private ListaUsuariosFragment listaUsuariosFragment;
    private ListaReservasFragment listaReservasFragment;
    private static boolean esFragment;

    private GestureDetector.SimpleOnGestureListener listener=new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            for(MyButton boton:botonLista){

                if(boton.onClick(e.getX(),e.getY()))

                    break;

            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener=new View.OnTouchListener(){

        public boolean onTouch(View view,MotionEvent motionEvent){

            if(posicion<0) return false;
            Point point=new Point((int)motionEvent.getRawX(),(int)motionEvent.getRawY());

            RecyclerView.ViewHolder swipeViewHolder=recyclerView.findViewHolderForAdapterPosition(posicion);

            if(swipeViewHolder!=null){

                View swipedItem=swipeViewHolder.itemView;



                Rect rect=new Rect();
                swipedItem.getGlobalVisibleRect(rect);

                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN ||
                        motionEvent.getAction()==MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                {
                    if(rect.top<point.y && rect.bottom>point.y)
                        detector.onTouchEvent(motionEvent);
                    else{

                        removeQueue.add(posicion);
                        posicion=-1;
                        recoverSwipedItem();
                    }
                }
            }

            return false;
        }
    };

    private synchronized void recoverSwipedItem(){

        while (!removeQueue.isEmpty()){

            int pos=removeQueue.poll();
            if(pos>-1)

                recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }

    public Swipe(Context context, RecyclerView recyclerView,int buttomWidth,boolean esFragment) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView=recyclerView;
        this.botonLista=new ArrayList<>();
        this.detector=new GestureDetector(context,listener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.botonBuffer=new HashMap<>();
        this.boton=buttomWidth;
        this.esFragment=esFragment;

        removeQueue=new LinkedList<Integer>(){

            @Override
            public boolean add(Integer integer) {

                if(contains(integer))

                    return false;

                else

                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe() {

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public static class MyButton {

        private String texto;
        private int imagenId,tamanioTexto,color,posicion;
        private RectF region;
        private ButtomClickListener listener;
        private Context context;
        private Resources recursos;
        private ListaUsuariosFragment listaUsuariosFragment;
        private ListaReservasFragment listaReservasFragment;

        public MyButton(Context context,String texto,int tamanioTexto,int imagenId,int color,ButtomClickListener listener) {
            this.texto = texto;
            this.imagenId = imagenId;
            this.tamanioTexto = tamanioTexto;
            this.color = color;
            this.listener = listener;
            this.context = context;
            this.recursos = context.getResources();
        }

        public MyButton(ListaUsuariosFragment listaUsuariosFragment, String texto, int tamanioTexto, int ic_delete, int color, ButtomClickListener listener) {

            this.texto = texto;
            this.imagenId = ic_delete;
            this.tamanioTexto = tamanioTexto;
            this.color = color;
            this.listener = listener;
            this.listaUsuariosFragment = listaUsuariosFragment;
            this.recursos = listaUsuariosFragment.getResources();
        }

        public MyButton(ListaReservasFragment listaReservasFragment, String texto, int tamanioTexto, int ic_delete, int color, ButtomClickListener listener) {

            this.texto = texto;
            this.imagenId = ic_delete;
            this.tamanioTexto = tamanioTexto;
            this.color = color;
            this.listener = listener;
            this.listaReservasFragment = listaReservasFragment;
            this.recursos = listaReservasFragment.getResources();
        }

        public boolean onClick(float x,float y){

            if(region!=null && region.contains(x,y)){

                listener.onClick(posicion);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c,RectF rectF,int pos){

            Paint paint=new Paint();

            paint.setColor(color);
            c.drawRect(rectF,paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(tamanioTexto);
            Rect r=new Rect();
            float  height=rectF.height();
            float width=rectF.width();

            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(texto,0,texto.length(),r);
            float x=0,y=0;

            if(imagenId==0){

                x=width/2f-r.width()/2f-r.left;
                y=height/2f+r.height()/2f-r.bottom;
                c.drawText(texto,rectF.left+x,rectF.top+y,paint);

            }

            else if(esFragment){

                if(listaUsuariosFragment instanceof ListaUsuariosFragment){

                    Drawable d= ContextCompat.getDrawable(listaUsuariosFragment.getContext(),imagenId);
                    Bitmap bitmap=drawableToBipmap(d);
                    c.drawBitmap(bitmap,(rectF.left+rectF.right)/2,(rectF.top+rectF.bottom)/2,paint);

                }else if(listaReservasFragment instanceof ListaReservasFragment){

                    Drawable d= ContextCompat.getDrawable(listaReservasFragment.getContext(),imagenId);
                    Bitmap bitmap=drawableToBipmap(d);
                    c.drawBitmap(bitmap,(rectF.left+rectF.right)/2,(rectF.top+rectF.bottom)/2,paint);

                }else{


                }



            }else{

                Drawable d= ContextCompat.getDrawable(context,imagenId);
                Bitmap bitmap=drawableToBipmap(d);
                c.drawBitmap(bitmap,(rectF.left+rectF.right)/2,(rectF.top+rectF.bottom)/2,paint);
            }

            region=rectF;
            this.posicion=pos;

        }
    }

    public static Bitmap drawableToBipmap(Drawable d){

        if(d instanceof BitmapDrawable)

            return ((BitmapDrawable) d).getBitmap();

        Bitmap bitmap=Bitmap.createBitmap(d.getIntrinsicWidth(),
                d.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);
        d.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        d.draw(canvas);
        return bitmap;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int pos=viewHolder.getAdapterPosition();

        if(posicion!=pos)

            removeQueue.add(posicion);

        posicion=pos;
        if(botonBuffer.containsKey(posicion))

            botonLista=botonBuffer.get(posicion);

        else

            botonLista.clear();

        botonBuffer.clear();
        deslizador=0.5f*botonLista.size()*boton;
        recoverSwipedItem();
    }

   /* @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {

        return deslizador;
    }*/

    /*@Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }*/

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return deslizador;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f*defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f*defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        int pos=viewHolder.getAdapterPosition();
        float translationX=dX;
        View itemView=viewHolder.itemView;

        if(pos < 0){

            posicion=pos;
            return;
        }

        if(actionState== ItemTouchHelper.ACTION_STATE_SWIPE){

            if(dX<0){

                List<MyButton> buffer=new ArrayList<>();

                if(!botonBuffer.containsKey(pos)){

                    instantiateMySwipe(viewHolder,buffer);
                    botonBuffer.put(pos,buffer);
                }else{

                    buffer=botonBuffer.get(pos);
                }

                translationX=dX*buffer.size()*boton/itemView.getWidth();
                drawButtom(c,itemView,buffer,pos,translationX);
            }
        }

        super.onChildDraw(c,recyclerView,viewHolder,translationX,dY,actionState,isCurrentlyActive);
    }

    private void drawButtom(Canvas c, View itemView, List<MyButton> buffer, int pos, float translationX) {

        float right=itemView.getRight();
        float dButtomWidth=-1*translationX/buffer.size();
        for(MyButton button:buffer){

            float left=right-dButtomWidth;
            button.onDraw(c,new RectF(left,itemView.getTop(),right,itemView.getBottom()),pos);
            right=left;
        }

    }

    public abstract void instantiateMySwipe(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer);
}


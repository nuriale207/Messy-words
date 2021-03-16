package com.example.practica1_desordenadas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderNivel extends RecyclerView.ViewHolder {
    public TextView eltexto;
    public ImageView laimagen;
    public boolean[] seleccion;
    public int id;
    public ViewHolderNivel(@NonNull View itemView) {
        super(itemView);
        eltexto=itemView.findViewById(R.id.texto);
        laimagen=itemView.findViewById(R.id.foto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    seleccion[getAdapterPosition()] = false;
                    Intent i=new Intent(itemView.getContext(),PantallaJuego.class);
                    i.putExtra("id",id);
                    Log.i("MYAPP", String.valueOf(laimagen.getId()));
                    ((Activity)view.getContext()).finish();
                    itemView.getContext().startActivity(i);

            }

        });
    }
}

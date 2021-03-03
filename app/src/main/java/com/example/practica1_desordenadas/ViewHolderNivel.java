package com.example.practica1_desordenadas;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderNivel extends RecyclerView.ViewHolder {
    public TextView eltexto;
    public ImageView laimagen;
    public boolean[] seleccion;
    public ViewHolderNivel(@NonNull View itemView) {
        super(itemView);
        eltexto=itemView.findViewById(R.id.texto);
        laimagen=itemView.findViewById(R.id.foto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seleccion[getAdapterPosition()]==true) {
                    seleccion[getAdapterPosition()] = false;
                    laimagen.setColorFilter(Color.WHITE);
                }
                else{

                    seleccion[getAdapterPosition()]=true;
                    laimagen.setColorFilter(Color.BLUE);
                }
            }

        });
    }
}

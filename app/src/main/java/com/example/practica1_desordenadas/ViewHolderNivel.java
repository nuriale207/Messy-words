package com.example.practica1_desordenadas;

import android.content.Intent;
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

                    seleccion[getAdapterPosition()] = false;
                    Intent i=new Intent(itemView.getContext(),PantallaJuego.class);
                    i.putExtra("Letras","eltexto");
                    i.putExtra("imagen","laimagen");
                    itemView.getContext().startActivity(i);


            }

        });
    }
}

package com.example.practica1_desordenadas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderNivel extends RecyclerView.ViewHolder {
    //View holder para mostrar los datos de cada nivel
    public TextView eltexto;
    public ImageView laimagen;
    public boolean[] seleccion;
    public int id;

    public ViewHolderNivel(@NonNull View itemView) {
        super(itemView);
        eltexto = itemView.findViewById(R.id.texto);
        laimagen = itemView.findViewById(R.id.foto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                seleccion[getAdapterPosition()] = false;

                Intent i2 = new Intent(itemView.getContext(), PantallaJuego.class);
                i2.putExtra("id", id);
                Log.i("MYAPP", String.valueOf(laimagen.getId()));
               // ((Activity) view.getContext()).finish();
                itemView.getContext().startActivity(i2);

            }

        });
    }
}

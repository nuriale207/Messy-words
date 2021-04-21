package com.example.practica1_desordenadas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
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
        eltexto=itemView.findViewById(R.id.texto);
        laimagen=itemView.findViewById(R.id.foto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    seleccion[getAdapterPosition()] = false;
//                //Establecer alarma
//                AlarmManager am = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
//                Intent intent3 = new Intent(view.getContext(), AlarmaJuegoBroadcastReceiver.class);
//                PendingIntent pi = PendingIntent.getBroadcast(view.getContext(), 20, intent3, 0);
//                //am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 60000, pi);
//                am.set(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime()+60000,pi);
                Intent i=new Intent(itemView.getContext(),PantallaJuego.class);
                    i.putExtra("id",id);
                    Log.i("MYAPP", String.valueOf(laimagen.getId()));
                    ((Activity)view.getContext()).finish();
                    itemView.getContext().startActivity(i);

            }

        });
    }
}

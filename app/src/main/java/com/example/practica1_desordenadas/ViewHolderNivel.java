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
////              Se establece una alarma para que termine la partida al de 5 minutos de empezar la partida
//                final AlarmManager manager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
//                final Intent i = new Intent(view.getContext(), ServicioTerminarJuego.class);
//                PendingIntent service = PendingIntent.getService(view.getContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
//                /**
//                 * Código obtenido de: https://stackoverflow.com/questions/41525944/alarm-manager-not-working-in-my-code
//                 * Autor: https://stackoverflow.com/users/4393601/pedro-sim%c3%b5es
//                 * **/
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    ///manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
//                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, service);
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    //manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
//                    manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, service);
//                } else {
//                    //manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
//                    manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, service);
//                }
                Intent i2 = new Intent(itemView.getContext(), PantallaJuego.class);
                i2.putExtra("id", id);
                Log.i("MYAPP", String.valueOf(laimagen.getId()));
               // ((Activity) view.getContext()).finish();
                itemView.getContext().startActivity(i2);

            }

        });
    }
}

package com.example.practica1_desordenadas;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class UpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MYAPP","Alarma parada");

        Toast toast=Toast.makeText(getApplicationContext(),"Se ha terminado el tiempo...", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
        toast.show();

        //((Activity)getApplicationContext()).finish();

        Intent intent3=new Intent(getApplicationContext(),ActividadSeleccionarNivel.class);
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent3.putExtra("finTiempo","abrirDialogo");
                startActivity(intent3);

        return super.onStartCommand(intent, flags, startId);
    }
}
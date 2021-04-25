package com.example.practica1_desordenadas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //En caso de venir del servidor php la notificación viene en un json
        String titulo;
        String body;
        if(remoteMessage.getNotification()==null) {
            Log.i("MYAPP","El mensaje es nulo");

            titulo=remoteMessage.getData().get("titulo");
            body=remoteMessage.getData().get("body");
            Log.i("FIREBASE",  titulo+"+"+body);

            sendNotification(titulo,body);

            Log.i("FIREBASE",  remoteMessage.getData().toString());
        }
        else{
            //En caso contrario se puede obtener directamente la información
            titulo=remoteMessage.getNotification().getTitle();


            body=remoteMessage.getNotification().getBody();
            Log.i("FIREBASE", body);

            sendNotification(titulo,body);
        }

    }

    private void sendNotification(String titulo,String messageBody) {

        //Método que muestra la notificación recibida
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(getApplicationContext(), "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Intent i = new Intent(getApplicationContext(),ActividadSeleccionarNivel.class);
            PendingIntent intentEnNot = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
            elManager.createNotificationChannel(elCanal);
            elBuilder.setSmallIcon(R.drawable.logo)
                    .setContentTitle(titulo)
                    .setContentText(messageBody)
                    .setSubText(titulo)
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true).setContentIntent(intentEnNot);
            elManager.notify(1, elBuilder.build());
        }
    }

}
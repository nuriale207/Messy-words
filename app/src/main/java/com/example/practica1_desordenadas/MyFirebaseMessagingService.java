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

//            try {
//                remoteMessage.getData().get("body");
//                JSONObject jsonObject=new JSONObject(remoteMessage.getData().toString());
//                titulo=jsonObject.getString("titulo");
//                body=jsonObject.getString("body");
//                Log.i("FIREBASE",  titulo+"+"+body);
//
//                sendNotification(titulo,body);
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            titulo=remoteMessage.getData().get("titulo");
            body=remoteMessage.getData().get("body");
            Log.i("FIREBASE",  titulo+"+"+body);

            sendNotification(titulo,body);

            Log.i("FIREBASE",  remoteMessage.getData().toString());
        }
        else{
            titulo=remoteMessage.getNotification().getTitle();


            body=remoteMessage.getNotification().getBody();
            Log.i("FIREBASE", body);

            sendNotification(titulo,body);
        }

    }

    private void sendNotification(String titulo,String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = "notificacionFireBase";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setContentTitle("Mensaje FCM")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        //La notificación muestra la puntuación del jugador
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(getApplicationContext(), "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Intent i = new Intent(getApplicationContext(),ActividadSeleccionarNivel.class);
            PendingIntent intentEnNot = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
            elManager.createNotificationChannel(elCanal);
            elBuilder.setSmallIcon(android.R.drawable.star_big_on)
                    .setContentTitle(titulo)
                    .setContentText(messageBody)
                    .setSubText(titulo)
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true).setContentIntent(intentEnNot);
            elManager.notify(1, elBuilder.build());
        }
    }

}
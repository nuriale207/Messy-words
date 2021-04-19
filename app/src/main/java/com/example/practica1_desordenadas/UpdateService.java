package com.example.practica1_desordenadas;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import java.util.Random;

public class UpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // generates random number
        Random random = new Random();
        int randomInt = random.nextInt(100);
        String lastUpdate = "R: "+randomInt;
        // Reaches the view on widget and displays the number
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_automatico);
        view.setTextViewText(R.id.texto_palabra_aleatoria, lastUpdate);
        ComponentName theWidget = new ComponentName(this, Widget_automatico.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);

        return super.onStartCommand(intent, flags, startId);
        // generates random number
//        Random random = new Random();
//        int randomInt = random.nextInt(100);
//        String lastUpdate = "R: "+randomInt;
//        // Reaches the view on widget and displays the number
//        RemoteViews view = new RemoteViews(getPackageName(), R.layout.updating_widget);
//        view.setTextViewText(R.id.tvWidget, lastUpdate);
//        ComponentName theWidget = new ComponentName(this, UpdatingWidget.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        manager.updateAppWidget(theWidget, view);
//        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
//        //Diccionario.getDiccionario().cargar(this);
//        //String palabra="chocolate";
//        String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();
//        views.setTextViewText(R.id.palabraAleatoria, palabra);
//
//        //Se crea un nuevo intent al botón de buscar significado para que busque la palabra nueva
//        String url = "https://dle.rae.es/"+palabra;
//        views.setTextViewText(R.id.palabraAleatoria, palabra);
////        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////        final Intent i = new Intent(context, UpdateService.class);
////
////        if (service == null) {
////            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
////        }
////        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, service);
//
//        Intent intent2 = new Intent(Intent.ACTION_VIEW);
//        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent2.setData(Uri.parse(url));
//        intent2.setAction("CAMBIAR PALABRA");
//        PendingIntent pending = PendingIntent.getActivity(this, 0,intent2, 0);
//        views.setOnClickPendingIntent(R.id.buscarEnDic, pending);
//
//
//        ComponentName tipowidget = new ComponentName(this, Widget_diccionario.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        manager.updateAppWidget(tipowidget, views);
//
//        return super.onStartCommand(intent, flags, startId);

    }
}

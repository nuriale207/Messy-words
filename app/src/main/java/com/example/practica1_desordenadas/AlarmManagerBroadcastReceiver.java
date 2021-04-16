package com.example.practica1_desordenadas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        //Diccionario.getDiccionario().cargar(context);
        String palabra="chocolate";
        views.setTextViewText(R.id.palabraAleatoria, palabra);

        //Se crea un nuevo intent al botón de buscar significado para que busque la palabra nueva
        String url = "https://dle.rae.es/"+palabra;
        views.setTextViewText(R.id.palabraAleatoria, palabra);

        Intent intent2 = new Intent(Intent.ACTION_VIEW);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.setData(Uri.parse(url));
        intent2.setAction("CAMBIAR PALABRA");
        PendingIntent pending = PendingIntent.getActivity(context, 0,intent2, 0);
        views.setOnClickPendingIntent(R.id.buscarEnDic, pending);


        ComponentName tipowidget = new ComponentName(context, Widget_diccionario.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(tipowidget, views);
    }
}

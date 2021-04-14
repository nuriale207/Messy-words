package com.example.practica1_desordenadas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Se obtiene la palabra aleatoriamente del diccionario
        Diccionario.getDiccionario().cargar(context);
        String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        //Añadir el intent al boton de busqueda en el diccionario
        String url = "https://dle.rae.es/"+palabra;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
        views.setOnClickPendingIntent(R.id.buscarEnDic, pending);

        //Añadir el intent para generar otra palabra al pulsar el boton Otra palabra

        views.setTextViewText(R.id.palabraAleatoria,palabra);
        views.setOnClickPendingIntent(R.id.generarOtra,getPendingSelfIntent(context, "clickOtra"));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);



    }
    static protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, context.getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);//add this line
        if (intent.getAction().equals("clickOtra")) {
            // your onClick action is here
            Diccionario.getDiccionario().cargar(context);
            String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setTextViewText(R.id.palabraAleatoria,palabra);

        }
    }



}
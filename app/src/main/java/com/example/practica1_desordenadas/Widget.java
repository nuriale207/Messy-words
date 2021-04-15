package com.example.practica1_desordenadas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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
    private static final String ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE";
    private static String palabra = "";
    /** Código obtenido de Github: https://github.com/yerenutku/WidgetExamples/blob/master/BroadcastWidgetExample/app/src/main/java/com/erenutku/broadcastwidgetexample/BroadcastWidget.java
     * Autor:github.com/yerenutku
     * Adaptado por Nuria Lebeña para esta app**/
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);

        //Añadir el intent al boton de busqueda en el diccionario
        Diccionario.getDiccionario().cargar(context);
        String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();
        String url = "https://dle.rae.es/"+palabra;
        views.setTextViewText(R.id.palabraAleatoria, palabra);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
        views.setOnClickPendingIntent(R.id.buscarEnDic, pending);

        // Construct an Intent which is pointing this class.
        Intent intent2 = new Intent(context, Widget.class);
        intent2.setAction(ACTION_SIMPLEAPPWIDGET);
        // And this time we are sending a broadcast with getBroadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.generarOtra, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /** Código obtenido de Github: https://github.com/yerenutku/WidgetExamples/blob/master/BroadcastWidgetExample/app/src/main/java/com/erenutku/broadcastwidgetexample/BroadcastWidget.java
        * Autor:github.com/yerenutku
         * Adaptado por Nuria Lebeña para esta app**/
        if (ACTION_SIMPLEAPPWIDGET.equals(intent.getAction())) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Diccionario.getDiccionario().cargar(context);
            String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();
            views.setTextViewText(R.id.palabraAleatoria, palabra);

            //Add again another intent
            String url = "https://dle.rae.es/"+palabra;
            views.setTextViewText(R.id.palabraAleatoria, palabra);

            Intent intent2 = new Intent(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setData(Uri.parse(url));
            PendingIntent pending = PendingIntent.getActivity(context, 0,intent2, 0);
            views.setOnClickPendingIntent(R.id.buscarEnDic, pending);

            // This time we dont have widgetId. Reaching our widget with that way.
            ComponentName appWidget = new ComponentName(context, Widget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidget, views);
        }
    }

}
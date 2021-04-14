package com.example.practica1_desordenadas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent = new Intent(context,Widget.class);
        intent.setAction("ACTUALIZAR_WIDGET");
        intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,7768, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.generarOtra,pendingIntent);

        Diccionario.getDiccionario().cargar(context);
        String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();

        views.setTextViewText(R.id.palabraAleatoria,palabra);
        //views.setOnClickPendingIntent(R.id.generarOtra,getPendingSelfIntent(context, "clickOtra"));
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

        if (intent.getAction().equals("ACTUALIZAR_WIDGET")){
            //your onClick action is here
            int widgetId = intent.getIntExtra( AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                actualizarWidget(context, widgetManager, widgetId);
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Diccionario.getDiccionario().cargar(context);
            String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();

            views.setTextViewText(R.id.palabraAleatoria,palabra);
            views.setOnClickPendingIntent(R.id.generarOtra,getPendingSelfIntent(context, "clickOtra"));

        }
    }

    private void actualizarWidget(Context context, AppWidgetManager widgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Diccionario.getDiccionario().cargar(context);
        String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();

        views.setTextViewText(R.id.palabraAleatoria,palabra);
    }

    ;
}
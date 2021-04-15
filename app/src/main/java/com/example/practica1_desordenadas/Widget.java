package com.example.practica1_desordenadas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {
    private static final String ACCION_OTRA_PALABRA = "CAMBIAR_PALABRA";
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

        // Añadir el intent al boton de cambiar la palabra
        Intent intent2 = new Intent(context, Widget.class);
        intent2.setAction(ACCION_OTRA_PALABRA);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.generarOtra, pendingIntent);
        // Se actualiza el widget
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
        if (ACCION_OTRA_PALABRA.equals(intent.getAction())) {
            // Se genera una nueva palabra aleatoria
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Diccionario.getDiccionario().cargar(context);
            String palabra=Diccionario.getDiccionario().obtenerPalabraAleatoria();
            views.setTextViewText(R.id.palabraAleatoria, palabra);

            //Se crea un nuevo intent al botón de buscar significado para que busque la palabra nueva
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
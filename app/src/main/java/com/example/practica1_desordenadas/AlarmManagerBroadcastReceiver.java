package com.example.practica1_desordenadas;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);


        ComponentName tipowidget = new ComponentName(context, Widget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(tipowidget, remoteViews);
    }
}

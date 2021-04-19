package com.example.practica1_desordenadas;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Scheduler;
//CLASE PARA GESTIONAR LAS PETICIONES A LA API DIRECTIONS DE GOOGLE MAPS
//NO SE HA UTILIZADO PORQUE LA API HA RESULTADO SER DE PAGO
public class ConexionMaps extends Worker {
    public ConexionMaps(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("MYAPP","en do work");

        String direccion = getInputData().getString("url");;
        Log.i("MYAPP",direccion);



        HttpURLConnection urlConnection = null;
        String result="";
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);

            Log.i("MYAPP", "vAMOS A HACER EL POST");

            int statusCode=urlConnection.getResponseCode();
            Log.i("MYAPP",urlConnection.getResponseMessage());
            Log.i("MYAPP", String.valueOf(statusCode));
            if (statusCode == 200) {
                Log.i("MYAPP","CONEXION ESTABLECIDA");

                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("MYAPP",result);
        Data resultados = new Data.Builder()
                .putString("resultado",result)
                .build();
        return Result.success(resultados);
    }
}

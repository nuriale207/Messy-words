package com.example.practica1_desordenadas;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import rx.Scheduler;

public class ConexionBD extends Worker {



    public ConexionBD(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String fichero=getInputData().getString("fichero");
        String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/nlebena001/WEB/"+fichero;
        Log.i("MYAPP",direccion);

        String parametros=getInputData().getString("parametros");
        Log.i("MYAPP",parametros);

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
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("MYAPP", "vAMOS A HACER EL POST");
            int statusCode=urlConnection.getResponseCode();
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

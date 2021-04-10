package com.example.practica1_desordenadas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class AccesoBD {
    private static AccesoBD miAccesoBD = new AccesoBD();


    private AccesoBD() {

    }

    public static AccesoBD getAccesoBD() {
        return miAccesoBD;
    }

    //Método que se comunica con la base de datos para comprobar que el usuario existe y la contraseña es correcta
//    private void comprobarInicioSesion(String nombre, String contraseña, Context contexto) {
//        Log.i("MYAPP", "comprobando contraseña");
//
//        Data datos = new Data.Builder()
//                .putString("fichero", "usuarios.php")
//                .putString("parametros", "funcion=comprobarContrasena&nombreUsuario=" + nombre + "&contrasena=" + contraseña)
//                .build();
//        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("comprobarInicio").build();
//        WorkManager.getInstance(contexto).getWorkInfoByIdLiveData(requesContrasena.getId())
//                .observe(context, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        if (workInfo != null && workInfo.getState().isFinished()) {
//                            String resultado = workInfo.getOutputData().getString("resultado");
//                            Log.i("MYAPP", "inicio realizado");
//
//                            Log.i("MYAPP", resultado);
//
//                            if (resultado == "") {
//                                Toast toast = Toast.makeText(contexto, "El usuario es incorrecto o no existe", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                                toast.show();
//                            } else if (resultado.contains("0")) {
//                                Toast toast = Toast.makeText(contexto, "La contraseña es incorrecta", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                                toast.show();
//                            } else {
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString("nombreUsuario", nombre);
//                                Intent i = new Intent(contexto, MostrarPerfil.class);
//                                //startActivity(i);
//                                editor.apply();
//                            }
//                        }
//                    }
//                });
//        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
//        WorkManager.getInstance(contexto).enqueueUniqueWork("comprobarInicio", ExistingWorkPolicy.REPLACE, requesContrasena);
//    }
}

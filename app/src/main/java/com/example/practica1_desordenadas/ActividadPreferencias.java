package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ActividadPreferencias extends AppCompatActivity {
    //Actividad que incluye el fragment de las preferencias
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_preferencias);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentPreferencias, new Preferencias())
                .commit();
    }
}
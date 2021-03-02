package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements DialogoSalir.ListenerdelDialogoSalir {
    Button botonJugar;
    Button botonRanking;
    Button botonInstrucciones;
    Button botonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Paso X: Obtener los elementos del layout
        botonJugar=findViewById(R.id.botonJugar);
        botonRanking=findViewById(R.id.botonRanking);
        botonInstrucciones=findViewById(R.id.botonInstrucciones);
        botonSalir=findViewById(R.id.botonSalir);



        //Paso X: Añadir los listeners de cada boton



        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSalir dialogoSalir=new DialogoSalir();
                dialogoSalir.show(getSupportFragmentManager(), "etiqueta");
            }
        });






    }

    @Override
    public void alpulsarOK() {
        finish();
    }
}
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();
        Diccionario.getDiccionario().cargar(this);
        //Paso X: se definen los intent que se van a utilizar despues
        Intent i=new Intent(this,ActividadSeleccionarNivel.class);
        Intent iRanking=new Intent(this,PantallaRanking.class);

        //Paso X: Añadir los listeners de cada boton
        botonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(i);
            }
        });

        botonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(iRanking);

            }
        });
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
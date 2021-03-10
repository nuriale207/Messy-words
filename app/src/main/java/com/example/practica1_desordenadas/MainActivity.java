package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class MainActivity extends BarraMenu implements DialogoSalir.ListenerdelDialogoSalir {
    Button botonJugar;
    Button botonRanking;
    Button botonInstrucciones;
    Button botonSalir;

    Toolbar menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Paso X: Obtener los elementos del layout
        botonJugar=findViewById(R.id.botonJugar);
        setSupportActionBar(findViewById(R.id.labarra));
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
        //En el botón ranking en caso de que el dispositivo esté en apaisado el fragment se carga a la derecha
        botonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration. ORIENTATION_LANDSCAPE ){
                    FragmentRanking fragment= ( FragmentRanking) getSupportFragmentManager().findFragmentById(R.id.fragment);
                    fragment.cargarRanking();
                }
                else{
                    startActivity(iRanking);

                }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_layout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.opcion1:{

            }
            case R.id.opcion2:{
                Intent iRegistro=new Intent(this,Registro.class);
                startActivity(iRegistro);

            }

        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends BarraMenu implements DialogoSalir.ListenerdelDialogoSalir {
    Button botonJugar;
    Button botonRanking;
    Button botonInstrucciones;
    Button botonSalir;
    String idioma;

    Toolbar menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Paso 1: Obtener los elementos del layout
        botonJugar=findViewById(R.id.botonJugar);
        setSupportActionBar(findViewById(R.id.labarra));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        botonRanking=findViewById(R.id.botonRanking);
        botonInstrucciones=findViewById(R.id.botonInstrucciones);
        botonSalir=findViewById(R.id.botonSalir);
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();
        Diccionario.getDiccionario().cargar(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Paso 2: Gestión del idioma
                //Paso 1: miro el idioma de las preferencias
                    SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
                    String idiomaConfigurado=preferencias.getString("idioma","castellano");
                    String sufijoIdioma="es";
                    if (idiomaConfigurado.equals("Euskera")){
                        sufijoIdioma="eu";
                    }
                    Log.i("MYAPP",sufijoIdioma);
                    //Paso 2: miro la localización del dispositivo
                    String localizacionActual= getResources().getString(R.string.localizacion);

                    Log.i("MYAPP",localizacionActual);
                    if(!localizacionActual.equals(sufijoIdioma)){
                        Locale nuevaloc = new Locale(sufijoIdioma);

                        Locale.setDefault(nuevaloc);
                        Configuration configuration =
                                getBaseContext().getResources().getConfiguration();
                        configuration.setLocale(nuevaloc);
                        configuration.setLayoutDirection(nuevaloc);
                        Context context =
                                getBaseContext().createConfigurationContext(configuration);
                        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                        finish();
                        Log.i("MYAPP",Locale.getDefault().getLanguage());
                        startActivity(getIntent());

                    }






//        Bundle extras=getIntent().getExtras();
//        boolean cambiado=false;
//        if(extras!=null){
//            cambiado= extras.getBoolean("IdiomaCambiado");
//            Log.i("MYAPP","hay que cambiar el idioma");
//
//        }
//        idioma=prefs.getString("idioma","castellano");
//        if (idioma.equals("Euskera") && !cambiado){
//            Locale nuevaloc = new Locale("eu");
//            Log.i("MYAPP","cambiando idioma");
//
//            Locale.setDefault(nuevaloc);
//            Configuration configuration =
//                    getBaseContext().getResources().getConfiguration();
//            configuration.setLocale(nuevaloc);
//            configuration.ge
//            configuration.setLayoutDirection(nuevaloc);
//            Context context =
//                    getBaseContext().createConfigurationContext(configuration);
//            getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
//            finish();
//            Intent i=new Intent(this,MainActivity.class);
//            i.putExtra("IdiomaCambiado",true);
//            startActivity(i);
//        }

        botonJugar.setText(R.string.jugar);
        botonRanking.setText(R.string.ranking);
        botonInstrucciones.setText(R.string.instrucciones);
        botonSalir.setText(R.string.salir);




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

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
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
        Log.i("MYAPP", String.valueOf(id));
        Log.i("MYAPP", String.valueOf(R.id.opcion1));
        Log.i("MYAPP", String.valueOf(R.id.opcion2));
        if(id==R.id.opcion1){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs.contains("nombreUsuario")){
                //Se muestra el perfil
                Intent i=new Intent(this,MostrarPerfil.class);
                startActivity(i);
                MenuItem item2=findViewById(id+1);
                item2.setTitle("Cerrar sesión");
            }
            else{
                //Se muestra la pantalla de inicio de sesión
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        else if (id==R.id.opcion2){
            Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
            startActivity(iIniciarSesion);
        }

        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);
            finish();
            startActivity(iPreferencias);

        }

        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        savedInstanceState.putString("idioma",idioma);

    }
}
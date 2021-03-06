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
import android.widget.ScrollView;
import android.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends  AppCompatActivity implements DialogoSalir.ListenerdelDialogoSalir, DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {
    Button botonJugar;
    Button botonRanking;
    Button botonInstrucciones;
    Button botonSalir;
    String idioma;

    Toolbar menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paso 0: Se mira el tema que tiene que tener la actividad
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String tema=preferencias.getString("tema","Greenish blue");
        /**
         Codigo obtenido de StackOverflow:
         Pregunta:https://stackoverflow.com/questions/2482848/how-to-change-current-theme-at-runtime-in-android
         Usuario:https://stackoverflow.com/users/243782/pentium10
         **/
        if(tema.equals("Greenish blue")){
            setTheme(R.style.TemaDesordenadasGreen);
        }
        else if(tema.equals("Pinkish purple")){
            setTheme(R.style.TemaDesordenadasPurple);
        }
        else{
            setTheme(R.style.TemaDesordenadasHighContrast);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration. ORIENTATION_LANDSCAPE ){
            ScrollView scroll=findViewById(R.id.scroll);
            scroll.setVerticalScrollBarEnabled(false);

        }
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            if(extras.getBoolean("ranking")){
                FragmentRanking fragment= ( FragmentRanking) getSupportFragmentManager().findFragmentById(R.id.fragment);
                fragment.cargarRanking();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
        }
        //Paso 1: Se obtienen los elementos del layout
        botonJugar=findViewById(R.id.botonJugar);
        setSupportActionBar(findViewById(R.id.labarra));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        botonRanking=findViewById(R.id.botonRanking);
        botonSalir=findViewById(R.id.botonSalir);
        Diccionario.getDiccionario().cargar(this);

        //Paso 2: Gesti??n del idioma
                //Paso 1: miro el idioma de las preferencias
                    String idiomaConfigurado=preferencias.getString("idioma","castellano");
                    String sufijoIdioma="es";
                    if (idiomaConfigurado.equals("Euskera")){
                        sufijoIdioma="eu";
                    }

                    //Paso 2: miro la localizaci??n del dispositivo
                    String localizacionActual= getResources().getString(R.string.localizacion);

                    //Paso 3: si la localizaci??n no coincide con el idioma de las preferencias se
                    //cambia al idioma correspondiente
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

        //Se establece el texto de los elementos de la actividad
        botonJugar.setText(R.string.jugar);
        botonRanking.setText(R.string.ranking);
        botonSalir.setText(R.string.salir);


        //Paso 3: A??adir los listeners de cada boton
        botonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),ActividadSeleccionarNivel.class);
                startActivity(i);


            }
        });
        //En el bot??n ranking en caso de que el dispositivo est?? en apaisado el fragment se carga a la derecha
        //En caso contrario se llama a una nueva actividad
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
                    Intent iRanking=new Intent(v.getContext(),PantallaRanking.class);
                    startActivity(iRanking);
                }


            }
        });
        //Bot??n que genera el di??logo de salir al pulsarlo
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSalir dialogoSalir=new DialogoSalir();
                dialogoSalir.show(getSupportFragmentManager(), "etiqueta");
            }
        });






    }
    //La clase main activity implementa los m??todos de la interfaz de DialogoSalir
    @Override
    public void alpulsarOK() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_layout,menu);
        return true;
    }
    //La clase main activity implementa los m??todos de la barra de tareas
    //Se comentan en esta clase, las dem??s tambi??n los implementan de la misma manera.
    //La excepci??n es la clase pantalla juego que implementa una barra de tareas diferente
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        //Si se elige la primera opci??n (mostrar perfil) en caso de que haya un usuario registrado
        //Se muestra su informaci??n
        //En caso contrario se abre la actividad de iniciar sesi??n
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(id==R.id.opcion1){
            if (prefs.contains("nombreUsuario")){
                //Se muestra el perfil
                Intent i=new Intent(this,MostrarPerfil.class);
                startActivity(i);

            }
            else{
                //Se abre la actividad de iniciar sesi??n
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        //Si se pulsa sobre iniciar sesi??n, en caso de que haya un usuario registrado se muestra el
        //di??logo de inicio de sesi??n
        //En caso contrario se abre la actividad de inicio de sesi??n
        else if (id==R.id.opcion2){

            if (prefs.contains("nombreUsuario")){
                DialogoIniciarSesion dialogoIniciarSesion=new DialogoIniciarSesion();
                dialogoIniciarSesion.show(getSupportFragmentManager(), "etiqueta");

            }
            else{
                //Se abre la actividad de iniciar sesi??n
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        //En este caso se abre la actividad de las preferencias
        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);
            finish();
            startActivity(iPreferencias);
        }

        return super.onOptionsItemSelected(item);
    }

    //Se implementan los m??todos de la interfaz del di??logo de iniciar sesi??n
    @Override
    public void alpulsarCerrarSesion() {
        //Se elimina el usuario logeado de las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
    }

    @Override
    public void alpulsarCambiarUsuario() {
        //Se abre la ventana de inicio de sesi??n
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        savedInstanceState.putString("idioma",idioma);

    }
}
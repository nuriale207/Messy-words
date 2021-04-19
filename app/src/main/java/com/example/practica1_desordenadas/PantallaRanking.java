package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class PantallaRanking extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {

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
        setContentView(R.layout.activity_pantalla_ranking);
        setSupportActionBar(findViewById(R.id.toolbar6));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FragmentRanking fragmento= ( FragmentRanking) getSupportFragmentManager().findFragmentById(R.id.fragmento);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmento, fragmento)
                .commit();


        fragmento.cargarRanking();
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration. ORIENTATION_LANDSCAPE ){
            Intent iMainActivity=new Intent(this,MainActivity.class);
            iMainActivity.putExtra("ranking",true);
            startActivity(iMainActivity);

        }
        //Paso 2: Gestión del idioma
        //Paso 1: miro el idioma de las preferencias
        String idiomaConfigurado=preferencias.getString("idioma","castellano");
        String sufijoIdioma="es";
        if (idiomaConfigurado.equals("Euskera")){
            sufijoIdioma="eu";
        }

        //Paso 2: miro la localización del dispositivo
        String localizacionActual= getResources().getString(R.string.localizacion);

        //Paso 3: si la localización no coincide con el idioma de las preferencias se
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

    }
    //Implementa los métodos del nivel
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

            }
            else{
                //Se abre el diálogo





                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        else if (id==R.id.opcion2){
            DialogoIniciarSesion dialogoIniciarSesion=new DialogoIniciarSesion();
            dialogoIniciarSesion.show(getSupportFragmentManager(), "etiqueta");
//            Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
//            startActivity(iIniciarSesion);
        }

        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);
            finish();
            startActivity(iPreferencias);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void alpulsarCerrarSesion() {
        //Se elimina el usuario logeado de las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        prefs.edit().remove("email").apply();
        prefs.edit().remove("puntuacion").apply();
        prefs.edit().remove("pistas").apply();
    }

    @Override
    public void alpulsarCambiarUsuario() {
        //Se abre la ventana de inicio de sesión
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        prefs.edit().remove("email").apply();
        prefs.edit().remove("puntuacion").apply();
        prefs.edit().remove("pistas").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }
}
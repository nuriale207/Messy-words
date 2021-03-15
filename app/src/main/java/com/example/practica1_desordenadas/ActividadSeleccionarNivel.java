package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ActividadSeleccionarNivel extends BarraMenu implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paso 0: Mirar el tema que tiene que tener la app
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String tema=preferencias.getString("tema","Greenish blue");
        if(tema.equals("Greenish blue")){
            setTheme(R.style.TemaDesordenadasGreen);
        }
        else{
            setTheme(R.style.TemaDesordenadasPurple);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccionar_nivel);
        // CONFIGURACIÓN DE LA VISTA DEL RECYCLER VIEW
        RecyclerView elRecyclerView=findViewById(R.id.recyclerView);
        setSupportActionBar(findViewById(R.id.barraSeleccionarNivel));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Lista con las imágenes y las letras
        ArrayList<Integer> imagenes=new ArrayList<Integer>();
        ArrayList<String> letras=new ArrayList<String>();
        ArrayList<Integer> ids=new ArrayList<Integer>();
        ListaNiveles listaNiveles=new ListaNiveles();
        Log.i("MYAPP","cargandoNiveles");
        listaNiveles.cargarNiveles(this);

        imagenes=listaNiveles.getImagenesNiveles();
        letras=listaNiveles.getNombresNiveles();
        ids=listaNiveles.getIdNiveles();
        TextView idTitulo=findViewById(R.id.idTitulo);
        idTitulo.setText(R.string.seleccionarNivel);
        Button boton=findViewById(R.id.botonVolver2);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        boton.setText(R.string.anadirNivel);
        //Paso 2: Gestión del idioma
        //Paso 1: miro el idioma de las preferencias
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
        AdaptadorRecyclerNivel elAdaptaador=new AdaptadorRecyclerNivel(letras,imagenes,ids);
        elRecyclerView.setAdapter(elAdaptaador);
        StaggeredGridLayoutManager elLayoutRejillaDesigual =
                new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        elRecyclerView.setLayoutManager(elLayoutRejillaDesigual);


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
            startActivity(iPreferencias);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void alpulsarCerrarSesion() {
        //Se elimina el usuario logeado de las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
    }

    @Override
    public void alpulsarCambiarUsuario() {
        //Se abre la ventana de inicio de sesión
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }
}
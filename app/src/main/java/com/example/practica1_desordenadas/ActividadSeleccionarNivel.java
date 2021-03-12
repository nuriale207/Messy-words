package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class ActividadSeleccionarNivel extends BarraMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccionar_nivel);
        // CONFIGURACIÓN DE LA VISTA DEL RECYCLER VIEW
        RecyclerView elRecyclerView=findViewById(R.id.recyclerView);

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
        AdaptadorRecyclerNivel elAdaptaador=new AdaptadorRecyclerNivel(letras,imagenes,ids);
        elRecyclerView.setAdapter(elAdaptaador);
        StaggeredGridLayoutManager elLayoutRejillaDesigual =
                new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        elRecyclerView.setLayoutManager(elLayoutRejillaDesigual);


    }
}
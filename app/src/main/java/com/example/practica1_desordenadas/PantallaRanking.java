package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class PantallaRanking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ranking);
        ListView lista=findViewById(R.id.listaRanking);
        ArrayList<String> contenidoLista=new ArrayList<String>();
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
        //Se obtienen los usuarios de la base de datos
        String[] campos = new String[]{"NombreUsuario", "Puntuacion"};
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        Cursor cu = db.query("Usuarios", campos, null, null, null, null, null);
        int i=0;
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");
            String nombreUsuario = cu.getString(0);
            int puntuacion = cu.getInt(1);
            contenidoLista.add("Usuario: "+nombreUsuario+"\t Puntuación: "+puntuacion);

        }
        cu.close();
        db.close();
        lista.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contenidoLista));

    }
}
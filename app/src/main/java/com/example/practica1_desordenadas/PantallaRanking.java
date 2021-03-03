package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PantallaRanking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ranking);
        ListView lista=findViewById(R.id.listaRanking);
        ArrayList<String> contenidoLista=new ArrayList<String>();
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
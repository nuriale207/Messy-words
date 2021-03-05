package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class PantallaJuego extends AppCompatActivity {
    String letras;
    int idImagen;
    Nivel nivel;
    JugarPartida jugarPartida=JugarPartida.getPartida();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        Bundle extras=getIntent().getExtras();
        //Coger el nivel que hay que cargar
        if (extras!=null){

            if (extras.getInt("id")!=0){
                idImagen=extras.getInt("id");
                Log.i("MYapp", String.valueOf(idImagen));

            }
        }


        //Se obtiene el nivel de la base de datos
        String[] campos = new String[]{"Codigo", "Letras","IdImagen"};
        String[] argumentos = new String[] {String.valueOf(idImagen)};
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        Cursor cu = db.query("Niveles", campos, "Codigo=?", argumentos, null, null, null);
        int i=0;
        cu.moveToNext();

        Log.i("MYAPP","Obteniendo elemento");

        Integer Cod = cu.getInt(0);

        String letrasNivel = cu.getString(1);
        String idImagen = cu.getString(2);
        Log.i("MYAPP",idImagen);
        cu.close();
        db.close();

        //Se dibuja la imagen correspondiente
        ImageView imagen= findViewById(R.id.imageView);
        imagen.setImageResource(getResources().getIdentifier(idImagen,"drawable",getPackageName()));
        //Se crea el nivel partiendo de los datos obtenidos
        nivel=new Nivel(letrasNivel);





    }
}
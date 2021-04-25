package com.example.practica1_desordenadas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //La base de datos local se compone de una tabla de niveles

//        db.execSQL("CREATE TABLE Usuarios ('NombreUsuario'  VARCHAR(255) PRIMARY KEY NOT NULL, " +
//                "'Email'VARCHAR(255) NOT NULL," +
//                "'Contraseña'VARCHAR(255) NOT NULL," +
//                "'Puntuacion' INTEGER," +
//                "'Imagen' BLOB )");


        db.execSQL("CREATE TABLE Niveles ('Codigo'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'Letras' VARCHAR(255) NOT NULL," +
                "'IdImagen' VARCHAR(255) NOT NULL)");

        //Se añaden los niveles
        ContentValues nuevo = new ContentValues();
        nuevo.put("Letras", "ivev");
        nuevo.put("IdImagen", "ivev");
        db.insert("Niveles", null, nuevo);

        ContentValues nuevo2 = new ContentValues();
        nuevo.put("Letras", "hlao");
        nuevo.put("IdImagen", "hlao");
        db.insert("Niveles", null, nuevo);

        nuevo.put("Letras", "ilem");
        nuevo.put("IdImagen", "ilem");
        db.insert("Niveles", null, nuevo);

        nuevo.put("Letras", "irae");
        nuevo.put("IdImagen", "irae");
        db.insert("Niveles", null, nuevo);

        nuevo.put("Letras", "nsio");
        nuevo.put("IdImagen", "nsio");
        db.insert("Niveles", null, nuevo);

        nuevo.put("Letras", "bcoa");
        nuevo.put("IdImagen", "bcoa");
        db.insert("Niveles", null, nuevo);

        nuevo.put("Letras", "eiad");
        nuevo.put("IdImagen", "eiad");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "oorj");
        nuevo.put("IdImagen", "oorj");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "sdio");
        nuevo.put("IdImagen", "sdio");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "sqeuo");
        nuevo.put("IdImagen", "sqeuo");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "ysou");
        nuevo.put("IdImagen", "ysou");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "haroa");
        nuevo.put("IdImagen", "hraoa");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "modun");
        nuevo.put("IdImagen", "modun");
        db.insert("Niveles", null, nuevo);

        nuevo = new ContentValues();
        nuevo.put("Letras", "mxtoi");
        nuevo.put("IdImagen", "mxtoi");
        db.insert("Niveles", null, nuevo);




        //Pruebas realizadas para comprobar el correcto funcionamiento de la BD
        String[] campos = new String[]{"Codigo", "Letras","IdImagen"};

        Cursor cu = db.query("Niveles", campos, null, null, null, null, null);
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");

            int Cod = cu.getInt(0);
            String Letras = cu.getString(1);
            String IdImagen = cu.getString(2);
            Log.i("MYAPP",Cod+" "+Letras+" "+IdImagen);
        }


        cu.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}

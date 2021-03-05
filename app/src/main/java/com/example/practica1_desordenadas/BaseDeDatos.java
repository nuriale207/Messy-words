package com.example.practica1_desordenadas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("MYAPP","Creando BD");

        db.execSQL("CREATE TABLE Usuarios ('NombreUsuario'  VARCHAR(255) PRIMARY KEY NOT NULL, " +
                "'Nombre' VARCHAR(255) NOT NULL," +
                "'Email'VARCHAR(255) NOT NULL," +
                "'Contraseña'VARCHAR(255) NOT NULL," +
                "'Puntuacion' INTEGER )");


        db.execSQL("CREATE TABLE Niveles ('Codigo'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'Letras' VARCHAR(255) NOT NULL," +
                "'IdImagen' VARCHAR(255) NOT NULL)");

        db.execSQL("CREATE TABLE PuntuacionNivel ('NombreUsuario'  VARCHAR(255) NOT NULL REFERENCES Usuario('NombreUsuario') ," +
                "'Codigo' INTEGER NOT NULL REFERENCES Niveles('Codigo') ," +
                "'Puntuacion' INTEGER," +
                "PRIMARY KEY ('NombreUsuario','Codigo') )");
        Log.i("MYAPP","Insertando elemento");
        ContentValues nuevo = new ContentValues();
        nuevo.put("Letras", "abc");
        nuevo.put("IdImagen", "letras");
        db.insert("Niveles", null, nuevo);

        ContentValues nuevo2 = new ContentValues();
        nuevo.put("Letras", "abcd");
        nuevo.put("IdImagen", "letras2");
        db.insert("Niveles", null, nuevo);

        String[] campos = new String[]{"Codigo", "Letras","IdImagen"};

        Cursor cu = db.query("Niveles", campos, null, null, null, null, null);
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");

            int Cod = cu.getInt(0);
            String Letras = cu.getString(1);
            String IdImagen = cu.getString(2);
            Log.i("MYAPP",Cod+" "+Letras+" "+IdImagen);
        }

        ContentValues nuevoUsuario = new ContentValues();
        nuevoUsuario.put("NombreUsuario", "primero");
        nuevoUsuario.put("Nombre", "Agustin");
        nuevoUsuario.put("Email", "agustin@gmail.com");
        nuevoUsuario.put("Contraseña", "1234");
        nuevoUsuario.put("Puntuacion", 10000);

        db.insert("Usuarios", null, nuevoUsuario);

        cu.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

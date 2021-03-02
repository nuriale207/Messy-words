package com.example.practica1_desordenadas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class baseDeDatos extends SQLiteOpenHelper {
    public baseDeDatos(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios ('NombreUsuario'  VARCHAR(255) PRIMARY KEY NOT NULL, " +
                "'Nombre' VARCHAR(255) NOT NULL," +
                "'Email'VARCHAR(255) NOT NULL," +
                "'Contraseña'VARCHAR(255) NOT NULL." +
                "'Puntuacion' INTEGER )");


        db.execSQL("CREATE TABLE Niveles ('Codigo'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'Letras' VARCHAR(255) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

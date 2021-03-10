package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;

public class MostrarPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_perfil);

        Button volver=findViewById(R.id.botonVolver);
        Button editar=findViewById(R.id.botonEditar);

        TextView nombreUsuario=findViewById(R.id.textNombreDeUsuario);
        TextView email=findViewById(R.id.textEmail2);
        TextView puntuacion=findViewById(R.id.textPuntuación);

        ImageView imagen=findViewById(R.id.imageView2);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.contains("nombreUsuario")){
            String nombre=prefs.getString("nombreUsuario",null);
            BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
            String[] campos = new String[]{"NombreUsuario","Email","Puntuacion","Imagen"};
            String[] argumentos = new String[] {nombre};
            SQLiteDatabase db = GestorDB.getWritableDatabase();
            Cursor cu = db.query("Usuarios", campos, "NombreUsuario=?", argumentos, null, null, null);
            Log.i("MYAPP",nombre);
            if(cu.moveToNext()){
                nombreUsuario.setText("Nombre de usuario: "+cu.getString(0));
                email.setText("Email: "+cu.getString(1));
                puntuacion.setText("Puntuacion: "+cu.getString(2));
                byte[] image=cu.getBlob(3);
                Bitmap bmp = BitmapFactory.decodeByteArray(image, 0,image.length);
                imagen.setImageBitmap(bmp);

            }

        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


}
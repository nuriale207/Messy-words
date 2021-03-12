package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IniciarSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        EditText nombreUsuario=findViewById(R.id.nombreUsuarioEdit);
        EditText contraseña=findViewById(R.id.contraseñaEdit);

        Button botonIniciarSesion=findViewById(R.id.botonIniciarSesion);
        botonIniciarSesion.setText(R.string.iniciarSesion);
        Button botonRegistro=findViewById(R.id.botonRegistrarse);
        botonRegistro.setText(R.string.registrarse);

        TextView tagNombreUsuario=findViewById(R.id.nombreUsuario);
        TextView tagContraseña=findViewById(R.id.contraseña);
        tagNombreUsuario.setText(R.string.nombreUsuario);
        tagContraseña.setText(R.string.contrasena);



        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDeDatos GestorDB = new BaseDeDatos (v.getContext(), "NombreBD", null, 1);
                String[] campos = new String[]{"NombreUsuario","Contraseña"};
                String[] argumentos = new String[] {nombreUsuario.getText().toString()};
                SQLiteDatabase db = GestorDB.getWritableDatabase();
                Cursor cu = db.query("Usuarios", campos, "NombreUsuario=?", argumentos, null, null, null);
                if(cu.moveToNext()){
                    String contraseñaReal=cu.getString(1);
                    if (contraseñaReal.equals(contraseña.getText().toString())){
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                        SharedPreferences.Editor editor= prefs.edit();
                        editor.putString("nombreUsuario",cu.getString(0));
                        Intent i =new Intent(v.getContext(),MostrarPerfil.class);
                        startActivity(i);
                        editor.apply();
                    }
                    else{
                        Toast toast=Toast.makeText(getApplicationContext(),"La contraseña es incorrecta", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
                else{
                    Toast toast=Toast.makeText(getApplicationContext(),"No hay ningún usuario registrado con ese nombre, regístrate", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),Registro.class);
                startActivity(i);



            }
        });


    }
}
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class IniciarSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        EditText nombreUsuario=findViewById(R.id.nombreUsuarioEdit);
        EditText contraseña=findViewById(R.id.contraseñaEdit);

        Button botonIniciarSesion=findViewById(R.id.botonIniciarSesion);
        Button botonRegistro=findViewById(R.id.botonRegistrarse);



    }
}
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.util.Locale;

public class MostrarPerfil extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paso 0: Mirar el tema que tiene que tener la app
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String tema=preferencias.getString("tema","Greenish blue");
        if(tema.equals("Greenish blue")){
            setTheme(R.style.TemaDesordenadasGreen);
        }
        else if(tema.equals("Pinkish purple")){
            setTheme(R.style.TemaDesordenadasPurple);
        }
        else{
            setTheme(R.style.TemaDesordenadasHighContrast);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_perfil);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button volver=findViewById(R.id.botonVolver);
        Button editar=findViewById(R.id.botonEditar);
        volver.setText(R.string.volver);
        editar.setText(R.string.editar);

        TextView nombreUsuario=findViewById(R.id.textNombreDeUsuario);
        TextView email=findViewById(R.id.textEmail2);
        TextView puntuacion=findViewById(R.id.textPuntuación);

        ImageView imagen=findViewById(R.id.imageView2);

        EditText textNombreUsuario=findViewById(R.id.editTextTextNombresuario);
        EditText textEmail=findViewById(R.id.editTextTextEmail);
        EditText textPuntuacion=findViewById(R.id.editTextTextPuntuacion);

        //Paso 2: Gestión del idioma
        //Paso 1: miro el idioma de las preferencias
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
                nombreUsuario.setText(getResources().getString(R.string.nombreUsuario));
                email.setText(getResources().getString(R.string.email));
                puntuacion.setText(getResources().getString(R.string.puntuacion));
                textNombreUsuario.setText(nombre);
                textNombreUsuario.setEnabled(false);
                textEmail.setText(cu.getString(1));
                textEmail.setEnabled(false);
                textPuntuacion.setText(cu.getString(2));
                textPuntuacion.setEnabled(false);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_layout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        Log.i("MYAPP", String.valueOf(id));
        Log.i("MYAPP", String.valueOf(R.id.opcion1));
        Log.i("MYAPP", String.valueOf(R.id.opcion2));
        if(id==R.id.opcion1){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs.contains("nombreUsuario")){
                //Se muestra el perfil
                Intent i=new Intent(this,MostrarPerfil.class);
                startActivity(i);

            }
            else{
                //Se abre el diálogo





                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        else if (id==R.id.opcion2){
            DialogoIniciarSesion dialogoIniciarSesion=new DialogoIniciarSesion();
            dialogoIniciarSesion.show(getSupportFragmentManager(), "etiqueta");
//            Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
//            startActivity(iIniciarSesion);
        }

        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);
            finish();
            startActivity(iPreferencias);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void alpulsarCerrarSesion() {
        //Se elimina el usuario logeado de las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
    }

    @Override
    public void alpulsarCambiarUsuario() {
        //Se abre la ventana de inicio de sesión
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }


}
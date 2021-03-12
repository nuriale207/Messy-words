package com.example.practica1_desordenadas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.Locale;

public class Registro extends AppCompatActivity {

    EditText nombreUsuario;
    EditText email;
    EditText contraseña1;
    EditText contraseña2;
    Button añadirImagen;
    Button registrarse;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombreUsuario=findViewById(R.id.editNombreUsuario);
        email=findViewById(R.id.editEmail);
        contraseña1=findViewById(R.id.editContraseña1);
        contraseña2=findViewById(R.id.editContraseña2);
        añadirImagen=findViewById(R.id.añadirImagen);
        imagen=findViewById(R.id.imageView);
        registrarse=findViewById(R.id.registrarse);
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

        añadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MYAPP","CLICADO");
                solicitarPermiso();
            }});
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirUsuario(nombreUsuario.getText().toString(),email.getText().toString(),
                        contraseña1.getText().toString(),contraseña2.getText().toString());
            }
        });



    }

    private void añadirUsuario(String nombreUsuario, String email, String  contraseña1,String  contraseña2) {
        //Se obtienen los niveles de la base de datos
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);

        String[] campos = new String[]{"NombreUsuario"};
        String[] argumentos = new String[] {nombreUsuario};
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        Cursor cu = db.query("Usuarios", campos, "NombreUsuario=?", argumentos, null, null, null);
        if(cu.moveToNext()){
            Toast toast=Toast.makeText(getApplicationContext(),"Ya existe un usuario con ese nombre", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
            toast.show();
        }
        else{
            if (!contraseña1.equals(contraseña2)){
                Toast toast=Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                toast.show();
            }
            else{
                ContentValues nuevo = new ContentValues();
                nuevo.put("NombreUsuario", nombreUsuario);
                nuevo.put("Email", email);

                nuevo.put("Contraseña", contraseña1);
                Bitmap bitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap .compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] img = bos.toByteArray();
                nuevo.put("Imagen",img);
                db.insert("Usuarios",null,nuevo);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor= prefs.edit();
                editor.putString("nombreUsuario",nombreUsuario);
                Intent i =new Intent(this,MostrarPerfil.class);
                startActivity(i);
                editor.apply();
            }

        }
        cu.close();
        db.close();


    }

    public void solicitarPermiso(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO


            } else {
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                //QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);

        } else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, EJECUTAR LA FUNCIONALIDAD


                } else {
                    // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD O EJECUTAR ALTERNATIVA
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imagen.setImageBitmap(photo);
        }
    }
}
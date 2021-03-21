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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;


public class Registro extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {

    EditText nombreUsuario;
    EditText email;
    EditText contraseña1;
    EditText contraseña2;
    Button añadirImagen;
    Button registrarse;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paso 0: Mirar el tema que tiene que tener la app
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String tema=preferencias.getString("tema","Greenish blue");
        /**
         Codigo obtenido de StackOverflow:
         Pregunta:https://stackoverflow.com/questions/2482848/how-to-change-current-theme-at-runtime-in-android
         Usuario:https://stackoverflow.com/users/243782/pentium10
         **/
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
        setContentView(R.layout.activity_registro);

        setSupportActionBar(findViewById(R.id.toolbar3));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nombreUsuario=findViewById(R.id.editNombreUsuario);

        email=findViewById(R.id.editEmail);
        contraseña1=findViewById(R.id.editContraseña1);
        contraseña2=findViewById(R.id.editContraseña2);

        añadirImagen=findViewById(R.id.añadirImagen);
        añadirImagen.setText(R.string.anadirImagen);
        imagen=findViewById(R.id.imageView);
        registrarse=findViewById(R.id.registrarse);

        registrarse.setText(R.string.registrarse);
        if(savedInstanceState!=null){
            EditText nUsuario=(EditText)findViewById(R.id.editNombreUsuario);
            nUsuario.setText(savedInstanceState.getString("nombreUsuario"));
            email.setText(savedInstanceState.getString("email"));
            contraseña1.setText(savedInstanceState.getString("contraseña1"));
            contraseña2.setText(savedInstanceState.getString("contraseña2"));

        }
        TextView textNombreUsuario=findViewById(R.id.textNombreUsuario);
        textNombreUsuario.setText(R.string.nombreUsuario);
        TextView textContraseña=findViewById(R.id.textContraseña);
        textContraseña.setText(R.string.contrasena);

        TextView textEmail=findViewById(R.id.textEmail2);
        textEmail.setText(R.string.email);

        TextView textRepContraseña=findViewById(R.id.textRepContraseña);
        textRepContraseña.setText(R.string.repetirContrasena);


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

        añadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MYAPP","CLICADO");
                solicitarPermisoGaleria();

            }});
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirUsuario(nombreUsuario.getText().toString(),email.getText().toString(),
                        contraseña1.getText().toString(),contraseña2.getText().toString());
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPermisoCamara();

            }
        });



    }

    private void solicitarPermisoGaleria() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO


            } else {
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                //QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        } else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 2);

        }
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

    public void solicitarPermisoCamara(){
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
            //Código de:https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imagen.setImageBitmap(photo);
        }
        else{
            final Uri imageUri = data.getData();
            final InputStream imageStream;
            try {
                //Código de:https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imagen.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nombreUsuario",nombreUsuario.getText().toString());
        savedInstanceState.putString("email",email.getText().toString());
        savedInstanceState.putString("contraseña1",contraseña1.getText().toString());
        savedInstanceState.putString("contraseña2",contraseña2.getText().toString());
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
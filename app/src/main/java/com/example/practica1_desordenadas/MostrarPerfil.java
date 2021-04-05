package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.Locale;

public class MostrarPerfil extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {

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

        //Se obtienen los elementos de a vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_perfil);

        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button volver=findViewById(R.id.botonVolver);
        volver.setText(R.string.volver);

        TextView nombreUsuario=findViewById(R.id.textNombreDeUsuario);
        TextView email=findViewById(R.id.textEmail2);
        TextView puntuacion=findViewById(R.id.textPuntuación);

        ImageView imagen=findViewById(R.id.imageView2);

        EditText textNombreUsuario=findViewById(R.id.editTextTextNombresuario);
        EditText textEmail=findViewById(R.id.editTextTextEmail);
        EditText textPuntuacion=findViewById(R.id.editTextTextPuntuacion);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Paso 2: Gestión del idioma
        //Paso 1: miro el idioma de las preferencias
        String idiomaConfigurado=preferencias.getString("idioma","castellano");
        String sufijoIdioma="es";
        if (idiomaConfigurado.equals("Euskera")){
            sufijoIdioma="eu";
        }
        //Paso 2: miro la localización del dispositivo
        String localizacionActual= getResources().getString(R.string.localizacion);
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

        //En caso de que el intent contenga un nombre de usuario se mostrará el indicado, en caso contrario
        //se mostraŕá el almacenado en las preferencias
        Bundle extras=getIntent().getExtras();
        String nombre="";
        if(extras!=null){
            nombre=extras.getString("usuario");
        }
        else{
            //Si el intent no tiene extras se coge de las preferencias
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            nombre=prefs.getString("nombreUsuario",null);
        }

        //Se obtienen de la base de datos los datos del usuario a mostrar
            /*BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
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
*/
        Data datos = new Data.Builder()
                .putString("fichero","usuarios.php")
                .putString("parametros","funcion=datosUsuario&nombreUsuario="+nombre)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("perfil").build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            String resultado=workInfo.getOutputData().getString("resultado");

                            try {

                                Log.i("MYAPP",resultado);
                                JSONObject jsonObject = new JSONObject(resultado);

                                nombreUsuario.setText(getResources().getString(R.string.nombreUsuario));
                                email.setText(getResources().getString(R.string.email));
                                puntuacion.setText(getResources().getString(R.string.puntuacion));
                                textNombreUsuario.setText(jsonObject.getString("NombreUsuario"));
                                textNombreUsuario.setEnabled(false);
                                textEmail.setText(jsonObject.getString("Email"));
                                textEmail.setEnabled(false);
                                textPuntuacion.setText(jsonObject.getString("Puntuacion"));
                                textPuntuacion.setEnabled(false);

                                /**
                                 * Código obtenido de stackoverflow
                                 * Link a la pregunta:https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
                                 * Perfil del usuario:https://stackoverflow.com/users/432209/user432209
                                 */
                                String img64=jsonObject.getString("Imagen");
                                byte[] fotoEnBytes= Base64.decode(img64,Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(fotoEnBytes, 0,fotoEnBytes.length);
                                imagen.setImageBitmap(decodedByte);

                                   /* String img64=jsonObj.getString("Imagen");
                                Log.i("MYAPP",img64);

                                byte[] fotoEnBytes= Base64.decode(img64,Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(fotoEnBytes, 0,fotoEnBytes.length);
                                imagen.setImageBitmap(decodedByte);*/


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("MYAPP","Error con el JSON");
                            }
                            Log.i("MYAPP",resultado);
                        }
                    }
                });
        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("perfil", ExistingWorkPolicy.REPLACE,otwr);



    }
   //La clase mostrar perfil implementa los métodos de la barra de tareas
    //Se comentan en esta clase, las demás también los implementan de la misma manera.
    //La excepción es la clase pantalla juego que implementa una barra de tareas diferente
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_layout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        //Si se elige la primera opción (mostrar perfil) en caso de que haya un usuario registrado
        //Se muestra su información
        //En caso contrario se abre la actividad de iniciar sesión
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(id==R.id.opcion1){
            if (prefs.contains("nombreUsuario")){
                //Se muestra el perfil
                Intent i=new Intent(this,MostrarPerfil.class);
                startActivity(i);

            }
            else{
                //Se abre la actividad de iniciar sesión
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        //Si se pulsa sobre iniciar sesión, en caso de que haya un usuario registrado se muestra el
        //diálogo de inicio de sesión
        //En caso contrario se abre la actividad de inicio de sesión
        else if (id==R.id.opcion2){

            if (prefs.contains("nombreUsuario")){
                DialogoIniciarSesion dialogoIniciarSesion=new DialogoIniciarSesion();
                dialogoIniciarSesion.show(getSupportFragmentManager(), "etiqueta");

            }
            else{
                //Se abre la actividad de iniciar sesión
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        //En este caso se abre la actividad de las preferencias
        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);
            finish();
            startActivity(iPreferencias);
        }

        return super.onOptionsItemSelected(item);
    }

    //Se implementan los métodos de la interfaz del diálogo de iniciar sesión
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
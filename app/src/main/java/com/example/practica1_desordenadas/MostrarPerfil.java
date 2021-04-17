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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MostrarPerfil extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {
    Button volver;
    Button añadirAContactos;

    TextView nombreUsuario;
    TextView email;
    TextView puntuacion;
    TextView pistas;

    ImageView imagen;

    EditText textNombreUsuario;
    EditText textEmail;
    EditText textPuntuacion;
    EditText textPistas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paso 0: Mirar el tema que tiene que tener la app
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String tema=preferencias.getString("tema","Greenish blue");
//        preferencias.edit().remove("email").apply();
//        preferencias.edit().remove("puntuacion").apply();
//        preferencias.edit().remove("pistas").apply();
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

        volver=findViewById(R.id.botonVolver);
        volver.setText(R.string.volver);
        añadirAContactos=findViewById(R.id.anadirContacto);
        nombreUsuario=findViewById(R.id.textNombreDeUsuario);
        email=findViewById(R.id.textEmail2);
         puntuacion=findViewById(R.id.textoPistas2);
         pistas=findViewById(R.id.textPistas);

        imagen=findViewById(R.id.imageView2);

        textNombreUsuario=findViewById(R.id.editTextTextNombresuario);
        textEmail=findViewById(R.id.editTextTextEmail);
        textPuntuacion=findViewById(R.id.editTextTextPuntuacion);
        textPistas=findViewById(R.id.editTextPistas);

        nombreUsuario.setText(getResources().getString(R.string.nombreUsuario));
        email.setText(getResources().getString(R.string.email));
        puntuacion.setText(getResources().getString(R.string.puntuacion));
        pistas.setText(getResources().getString(R.string.pistas));

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
            pistas.setVisibility(View.INVISIBLE);
            textPistas=findViewById(R.id.editTextPistas);

            textPistas.setVisibility(View.INVISIBLE);
            nombre=extras.getString("usuario");
            obtenerDatos(nombre,false);
        }
        else{
            //Si el intent no tiene extras se coge de las preferencias
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            nombre=prefs.getString("nombreUsuario",null);
            //Se elimina el botón añadir a contactos:
            añadirAContactos.setVisibility(View.INVISIBLE);
            añadirAContactos.setEnabled(false);
            if (prefs.contains("email")){
                String textoEmail=prefs.getString("email",null);
                Integer puntuacionUsuario=prefs.getInt("puntuacion",0);
                Integer pistas=prefs.getInt("pistas",0);
                textEmail.setText(textoEmail);
                textPuntuacion.setText(puntuacionUsuario.toString());
                textNombreUsuario.setText(nombre);
                textPistas=findViewById(R.id.editTextPistas);

                textPistas.setText(pistas.toString());

            }
            else{
                obtenerDatos(nombre,true);
            }
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




    }

    private void obtenerDatos(String nombre, boolean almacenar) {
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
                                Log.i("MYAPP", String.valueOf(jsonObject));


                                textNombreUsuario.setText(jsonObject.getString("NombreUsuario"));
                                textNombreUsuario.setEnabled(false);
                                textEmail.setText(jsonObject.getString("Email"));
                                textEmail.setEnabled(false);
                                textPuntuacion.setText(jsonObject.getString("Puntuacion"));
                                textPuntuacion.setEnabled(false);
                                int pistas=jsonObject.getInt("Pistas");
                                if (almacenar){
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getBaseContext());
                                    SharedPreferences.Editor editor = prefs.edit();

                                    editor.putString("email",textEmail.getText().toString());
                                    editor.putInt("puntuacion", Integer.parseInt(textPuntuacion.getText().toString()));
                                    editor.putInt("pistas",pistas);
                                    editor.apply();

                                }
                                /**
                                 * Código obtenido de stackoverflow
                                 * Link a la pregunta:https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
                                 * Perfil del usuario:https://stackoverflow.com/users/432209/user432209
                                 */
                                //String img64=jsonObject.getString("Imagen");
//                                byte[] fotoEnBytes= Base64.decode(img64,Base64.DEFAULT);
//                                Bitmap decodedByte = BitmapFactory.decodeByteArray(fotoEnBytes, 0,fotoEnBytes.length);
//                                imagen.setImageBitmap(decodedByte);

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
        prefs.edit().remove("email").apply();
        prefs.edit().remove("puntuacion").apply();
        prefs.edit().remove("pistas").apply();
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
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MostrarPerfil extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion, DialogoExisteContacto.ListenerdelDialogExisteContacto {
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
        añadirAContactos.setText(R.string.anadirContactos);
        nombreUsuario=findViewById(R.id.textNombreDeUsuario);
        email=findViewById(R.id.textEmail2);
         puntuacion=findViewById(R.id.textoPistas2);
         pistas=findViewById(R.id.textPistas);

        imagen=findViewById(R.id.imageView2);

        textNombreUsuario=findViewById(R.id.editTextTextNombresuario);
        textNombreUsuario.setEnabled(false);
        textEmail=findViewById(R.id.editTextTextEmail);
        textEmail.setEnabled(false);
        textPuntuacion=findViewById(R.id.editTextTextPuntuacion);
        textPuntuacion.setEnabled(false);
        textPistas=findViewById(R.id.editTextPistas);
        textPistas.setEnabled(false);

        nombreUsuario.setText(getResources().getString(R.string.nombreUsuario));
        email.setText(getResources().getString(R.string.email));
        puntuacion.setText(getResources().getString(R.string.puntuacion));
        pistas.setText(getResources().getString(R.string.pistas));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(i);
                finish();
            }
        });

        añadirAContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPermisoAñadirContactos();
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
            cargarImagen(nombre);

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
                cargarImagen(nombre);


            }
            else{
                obtenerDatos(nombre,true);
                cargarImagen(nombre);
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

    private void cargarImagen(String nombre) {
        //Metodo que carga la imagen de Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("images/"+nombre+".jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imagen);
            }
        });
    }


    //Método que comprueba si ya existe un contacto con el nombre indicado
    private boolean existeContacto(String nombreContacto){
        ContentResolver contentResolver =getApplicationContext().getContentResolver();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.PhoneLookup._ID };
        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] selectionArguments = { nombreContacto };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArguments, null);
        boolean existe=false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                existe=true;
            }
        }
        return existe;
    }


    //Método que solicita el permiso para escribir contactos y poder acceder a ellos
    private void solicitarPermisoAñadirContactos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO


            } else {
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                //QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS},
                    1);

        } else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD

           solicitarPermisoLeerContactos();

        }
    }

    //Método que solicita el permiso para leer contactos
    private void solicitarPermisoLeerContactos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO


            } else {
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                //QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    1);

        } else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD

            crearNuevoContacto(textNombreUsuario.getText().toString()+"_messy_words");

        }
    }

    //Método encargado de añadir a contactos el usuario mostrado
    private void crearNuevoContacto(String nombreContacto) {
        /** Parte del código obtenido de Github: https://gist.github.com/tonyshkurenko/1787ce39fa61fec41c25800741bde934
         * Autor:https://gist.github.com/tonyshkurenko
         * Adaptado por Nuria Lebeña para esta app**/
        //String nombreContacto=textNombreUsuario.getText().toString();
        String email=textEmail.getText().toString();
        //nombreContacto=nombreContacto+"_messy_words";
        int i=2;
        //Si ya existe un contacto con el mismo nombre se informa de ello
        if (existeContacto(nombreContacto)){
            DialogoExisteContacto dialogoExisteContacto=new DialogoExisteContacto();
            dialogoExisteContacto.show(getSupportFragmentManager(), "etiqueta");
            return;
        }
        //Si no, se guarda

        final ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        nombreContacto).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        // Asking the Contact provider to create a new contact
        try {
            getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.i("MYAPP","Contacto añadido");
            Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.contactoAñadido)+nombreContacto, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Metodo que obtiene los datos del usuario de la BD y los muestra en la app
    //En caso de que se haya inciado sesión se almacenan en las preferencias para permitir jugar sin conexión
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
                                    textPistas.setText(String.valueOf(pistas));
                                    editor.putString("email",textEmail.getText().toString());
                                    editor.putInt("puntuacion", Integer.parseInt(textPuntuacion.getText().toString()));
                                    editor.putInt("pistas",pistas);
                                    editor.apply();

                                }



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
        finish();
    }

    @Override
    public void alpulsarCambiarUsuario() {
        //Se abre la ventana de inicio de sesión
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        prefs.edit().remove("email").apply();
        prefs.edit().remove("puntuacion").apply();
        prefs.edit().remove("pistas").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }

    @Override
    public void alpulsarAñadir() {
        String nombreSinSufijo=textNombreUsuario.getText().toString()+"_messy_words";
        int i=2;
        String nombre=nombreSinSufijo+i;
        while (existeContacto(nombre)){
            i=i+1;
            nombre=nombreSinSufijo+i;
        }
        crearNuevoContacto(nombre);

    }
}
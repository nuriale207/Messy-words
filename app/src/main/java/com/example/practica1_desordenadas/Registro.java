package com.example.practica1_desordenadas;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
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
                    2);

        } else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 2);

        }
    }
    private boolean comprobarFormulario(String nombreUsuario, String email, String contraseña1, String contraseña2){
        //Método que contempla los distintos errores que puede haber al rellenar el formulario
        boolean valido=true;
        if(nombreUsuario.length()<2){
            valido=false;
            Toast toast = Toast.makeText(getApplicationContext(), "El nombre de usuario tener al menos dos caracteres", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if(email.length()<5){
            valido=false;
            Toast toast = Toast.makeText(getApplicationContext(), "El email debe tener al menos 5 caracteres", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if(!email.contains("@")||!email.contains(".")){
            valido=false;
            Toast toast = Toast.makeText(getApplicationContext(), "El email debe tener la forma example@example.com", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if(contraseña1.length()<8){
            valido=false;
            Toast toast = Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if(!contraseña1.equals(contraseña2)){
            valido=false;
            Toast toast = Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }




        return valido;
    }

    private void añadirUsuario(String nombreUsuario, String email, String contraseña1, String contraseña2) {
        //Método que añade el usuario a la BD remota haciendo una solicitud a un Worker
        boolean valido=
                comprobarFormulario(nombreUsuario, email, contraseña1, contraseña2);
        if (valido) {

//            Toast toast = Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//            toast.show();
//        } else {
            Data datos = new Data.Builder()
                    .putString("fichero", "usuarios.php")
                    .putString("parametros", "funcion=insertarUsuario&nombreUsuario=" + nombreUsuario + "&email=" + email + "&contrasena=" + contraseña1)
                    .build();
            OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("existeUsuario").build();
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                String resultado = workInfo.getOutputData().getString("resultado");
                                Log.i("MYAPP", "inicio realizado");

                                Log.i("MYAPP", resultado);
                                if (resultado.contains("error")){
                                    Toast toast=Toast.makeText(getApplicationContext(),"Ya existe un usuario con ese nombre", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                else{
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor= prefs.edit();
                                    editor.putString("nombreUsuario", nombreUsuario);
                                    editor.apply();
                                    guardarImagen();
                                    Intent i = new Intent(getApplicationContext(), MostrarPerfil.class);
                                    startActivity(i);
                                }


                            }
                        }
                    });
            //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
            WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("existeUsuario", ExistingWorkPolicy.REPLACE, requesContrasena);
        }




    }
    public byte[] getByteArray(){
        // Get the data from an ImageView as bytes
        this.imagen.setDrawingCacheEnabled(true);
        imagen.buildDrawingCache();
        Bitmap bitmap = imagen.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    private void guardarImagen() {
        //Método que almacena la imagen en Firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference imageRef = storageReference.child("images" + "/" + nombreUsuario.getText().toString() + ".jpg");

        imageRef.putBytes(getByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if the upload is successful
                        //hiding the progress dialog
                        //and displaying a success toast
                        //String profilePicUrl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(getApplication(), "La imagen se ha subido con éxito", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //if the upload is not successful
                        //hiding the progress dialog
                        //and displaying error message
                        Toast.makeText(getApplication(), exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
//                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }
//
//    private void insertarUsuario(String nombreUsuario, String email, String  contraseña1) {
//
//        Data datos = new Data.Builder()
//                .putString("fichero","usuarios.php")
//                .putString("parametros","funcion=insertarUsuario&nombreUsuario="+nombreUsuario+"&email="+email+"&contrasena="+contraseña1)
//                .build();
//        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("existeUsuario").build();
//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
//                .observe(this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        if (workInfo != null && workInfo.getState().isFinished()) {
//                            String resultado = workInfo.getOutputData().getString("resultado");
//                            Log.i("MYAPP","inicio realizado");
//
//                            Log.i("MYAPP",resultado);
//
//
//
//                        }
//                    }
//                });
//        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
//        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("existeUsuario", ExistingWorkPolicy.REPLACE,requesContrasena);
//
//    }
//
//    private void existeUsuario(String nombreUsuario) {
//        final boolean[] existe = {false};
//        Data datos = new Data.Builder()
//                .putString("fichero","usuarios.php")
//                .putString("parametros","funcion=datosUsuario&nombreUsuario="+nombreUsuario)
//                .build();
//        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("existeUsuario").build();
//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
//                .observe(this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        if (workInfo != null && workInfo.getState().isFinished()) {
//                            String resultado = workInfo.getOutputData().getString("resultado");
//                            Log.i("MYAPP","inicio realizado");
//
//                            Log.i("MYAPP",resultado);
//
//                            if(resultado!=""){
//                                existe[0] =true;
//
//                            }
//
//                        }
//
//                    }
//                });
//        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
//        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("existeUsuario", ExistingWorkPolicy.REPLACE,requesContrasena);
//
//
//    }

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
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);


                } else {
                    // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD O EJECUTAR ALTERNATIVA
                    Toast toast = Toast.makeText(getApplicationContext(), "Acepta el permiso para poder usar la cámara", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return;
            }
            case 2: {
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 2);


                } else {
                    // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD O EJECUTAR ALTERNATIVA
                    Toast toast = Toast.makeText(getApplicationContext(), "Acepta el permiso para poder acceder a la galería", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
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
        prefs.edit().remove("email").apply();
        prefs.edit().remove("puntuacion").apply();
        prefs.edit().remove("pistas").apply();    }

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
}
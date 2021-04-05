package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IniciarSesion extends AppCompatActivity implements DialogoIniciarSesion.ListenerdelDialogoIniciarSesion {
    EditText nombreUsuario;
    EditText contraseña;

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
        setContentView(R.layout.activity_iniciar_sesion);

        //Se obtienen los elementos de la vista y se configuran sus textos
        nombreUsuario=findViewById(R.id.nombreUsuarioEdit);
        contraseña=findViewById(R.id.contraseñaEdit);

        setSupportActionBar(findViewById(R.id.toolbar4));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Button botonIniciarSesion=findViewById(R.id.botonIniciarSesion);
        botonIniciarSesion.setText(R.string.iniciarSesion);
        Button botonRegistro=findViewById(R.id.botonRegistrarse);
        botonRegistro.setText(R.string.registrarse);

        TextView tagNombreUsuario=findViewById(R.id.nombreUsuario);
        TextView tagContraseña=findViewById(R.id.contraseña);
        tagNombreUsuario.setText(R.string.nombreUsuario);
        tagContraseña.setText(R.string.contrasena);

        //En caso de haber girado la pantalla se añaden los valores escritos previamente
        if(savedInstanceState!=null){
            nombreUsuario.setText(savedInstanceState.getString("nombreUsuario"));
            contraseña.setText(savedInstanceState.getString("contraseña"));
        }

        //Al pulsar el botón de inicio de sesión se comprueban con la base de datos los datos introducidos
        //En caso de haber algun problema se indica mediante toasts
        //Al iniciar sesión se muestra el perfil del usuario
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BaseDeDatos GestorDB = new BaseDeDatos (v.getContext(), "NombreBD", null, 1);
//                String[] campos = new String[]{"NombreUsuario","Contraseña"};
//                String[] argumentos = new String[] {nombreUsuario.getText().toString()};
//                SQLiteDatabase db = GestorDB.getWritableDatabase();
//                Cursor cu = db.query("Usuarios", campos, "NombreUsuario=?", argumentos, null, null, null);
//                if(cu.moveToNext()){
//                    String contraseñaReal=cu.getString(1);
//                    if (contraseñaReal.equals(contraseña.getText().toString())){
//                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
//                        SharedPreferences.Editor editor= prefs.edit();
//                        editor.putString("nombreUsuario",cu.getString(0));
//                        Intent i =new Intent(v.getContext(),MostrarPerfil.class);
//                        startActivity(i);
//                        editor.apply();
//                    }
//                    else{
//                        Toast toast=Toast.makeText(getApplicationContext(),"La contraseña es incorrecta", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//
//                }
//                else{
//                    Toast toast=Toast.makeText(getApplicationContext(),"No hay ningún usuario registrado con ese nombre, regístrate", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
                Log.i("MYAPP","pulsado");
                String contraseñaReal=contraseña.getText().toString();
                String nombre=nombreUsuario.getText().toString();
                comprobarInicioSesion(nombre,contraseñaReal);
            }
        });

        //Al pulsar en registro se abre la actividad de registro
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),Registro.class);
                startActivity(i);



            }
        });


    }
    //Método que se comunica con la base de datos para comprobar que el usuario existe y la contraseña es correcta
    private void comprobarInicioSesion(String nombre, String contraseña) {
        Log.i("MYAPP","comprobando contraseña");

        Data datos = new Data.Builder()
                .putString("fichero","usuarios.php")
                .putString("parametros","funcion=comprobarContrasena&nombreUsuario="+nombre+"&contrasena="+contraseña)
                .build();
        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("comprobarInicio").build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            String resultado = workInfo.getOutputData().getString("resultado");
                            Log.i("MYAPP","inicio realizado");

                            Log.i("MYAPP",resultado);

                            if(resultado==""){
                                Toast toast=Toast.makeText(getApplicationContext(),"El usuario es incorrecto o no existe", Toast.LENGTH_LONG);
                              toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                              toast.show();
                            }
                            else if(resultado.contains("0")){
                                Toast toast=Toast.makeText(getApplicationContext(),"La contraseña es incorrecta", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getBaseContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("nombreUsuario", nombre);
                                Intent i = new Intent(getApplication().getBaseContext(), MostrarPerfil.class);
                                startActivity(i);
                                editor.apply();
                            }
                        }
                    }
                });
        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("comprobarInicio", ExistingWorkPolicy.REPLACE,requesContrasena);



    }

    //En caso de que la aplicación se detenga se almacenan los nombres escritos hasta el momento
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nombreUsuario",nombreUsuario.getText().toString());
        savedInstanceState.putString("contraseña",contraseña.getText().toString());
    }

    //La clase iniciar sesión implementa los métodos de la barra de tareas
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
        //Se abre la ventana de inicio de sesión y se elimina el usuario logeado actualmente
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("nombreUsuario").apply();
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);

    }
}
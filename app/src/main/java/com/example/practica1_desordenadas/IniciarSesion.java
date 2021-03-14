package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

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

public class IniciarSesion extends AppCompatActivity {
    EditText nombreUsuario;
    EditText contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

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

        if(savedInstanceState!=null){
            nombreUsuario.setText(savedInstanceState.getString("nombreUsuario"));
            contraseña.setText(savedInstanceState.getString("contraseña"));
        }

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
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nombreUsuario",nombreUsuario.getText().toString());
        savedInstanceState.putString("contraseña",contraseña.getText().toString());
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
                MenuItem item2=findViewById(id+1);
                item2.setTitle("Cerrar sesión");
            }
            else{
                //Se muestra la pantalla de inicio de sesión
                Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
                startActivity(iIniciarSesion);
            }
        }
        else if (id==R.id.opcion2){
            Intent iIniciarSesion=new Intent(this,IniciarSesion.class);
            startActivity(iIniciarSesion);
        }

        else if (id==R.id.preferencias){
            Intent iPreferencias=new Intent(this,ActividadPreferencias.class);

            startActivity(iPreferencias);

        }

        return super.onOptionsItemSelected(item);
    }
}
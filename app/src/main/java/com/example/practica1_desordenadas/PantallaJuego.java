package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class PantallaJuego extends AppCompatActivity implements DialogoFinNivel.ListenerdelDialogoFinNivel, DialogoSalir.ListenerdelDialogoSalir{
    //Se establecen las variables que se van a emplear durante la partida
    int idNivel;
    int idImagen;
    Nivel nivel;
    ArrayAdapter adapter;
    ArrayList<String> lista=new ArrayList<String>();
    ListView intentosAnteriores;
    ListaNiveles listaNiveles = ListaNiveles.getListaNiveles();
    BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
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
        setContentView(R.layout.activity_pantalla_juego);
        Bundle extras=getIntent().getExtras();
        setSupportActionBar(findViewById(R.id.toolbarJuego));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Paso 1: se gestiona el nivel que hay que cargar cuyo id es proporcionado por la actividad
        //precedente
        if (extras!=null){
            if (extras.getInt("id")!=0){
                idNivel=extras.getInt("id"); }
        }

        //Paso 2: Gestión del idioma tal y como se realiza en la actividad principal
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

        };

        //Paso3: Gestión de la pérdida de información. En caso de que en el medio de la partida se
        //gire la pantalla o algo interrumpa la actividad esta se almacena
        //Se emplean distintos métodos de la clase nivel y listaNiveles para establecer el nivel con
        //los parámetros que se quiere
        if (savedInstanceState!=null){
            ListaNiveles.getListaNiveles().cargarNiveles(this);
            String nNivel=savedInstanceState.getString("nombreNivel");
            int idNivel=savedInstanceState.getInt("idNivel");
            int pPuntuacion=savedInstanceState.getInt("nivelPuntuacion");
            int nAciertos=savedInstanceState.getInt("nivelAciertos");
            int nIntentos=savedInstanceState.getInt("nivelIntentos");

            ArrayList<String> arrayAcertadas=savedInstanceState.getStringArrayList("nivelAcertadas");
            lista=arrayAcertadas;
            HashSet<String> acertadas= new HashSet<>(arrayAcertadas);
            if (acertadas==null){
                acertadas=new HashSet<String>();
            }

            ListaNiveles.getListaNiveles().setNivel(idNivel,pPuntuacion,nAciertos,acertadas,nIntentos);
            nivel=ListaNiveles.getListaNiveles().getNivel(idNivel);
            ListaNiveles.getListaNiveles().setNivelAct(nivel);
            idImagen=nivel.getIdImagen();

        }
        else{
            //Si la partida no se había iniciado previamente se obtiene el nivel directamente
            ListaNiveles.getListaNiveles().cargarNiveles(this);
            nivel=ListaNiveles.getListaNiveles().getNivel(idNivel);
            ListaNiveles.getListaNiveles().setNivelAct(nivel);
            idImagen=nivel.getIdImagen();
        }




        //Se dibuja la imagen correspondiente
        ImageView imagen= findViewById(R.id.imageView);
        imagen.setImageResource(idImagen);

        //Se cargan los elementos de la pantalla y se establecen sus textos
        intentosAnteriores=findViewById(R.id.lista);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        intentosAnteriores.setAdapter(adapter);
        Button boton=findViewById(R.id.botonAñadir);
        EditText texto=(EditText) findViewById(R.id.textoPalabra);
        TextView tagPuntuacion=findViewById(R.id.tagPuntuacion);
        TextView tagIntentos=findViewById(R.id.tagIntentos);
        tagPuntuacion.setText(getString(R.string.puntuacion)+": "+nivel.getPuntuacion());
        tagIntentos.setText(getString(R.string.intentos)+": "+nivel.getIntentos());

        texto.setText(R.string.escribePalabra);
        boton.setText(R.string.anadir);

        //Listeners del boton añadir palabra
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String palabra=texto.getText().toString();
                texto.getText().clear();

                //El botón comprueba dicha palabra empleando el nivel y actua en base de si ha sido un
                //acierto o no
                if (nivel.jugar(palabra)!=null){
                    lista.add(palabra);
                    ArrayAdapter adapter= (ArrayAdapter) intentosAnteriores.getAdapter();
                    adapter.notifyDataSetChanged();
                    int palabrasRestantes=nivel.getPalabrasRestantes();
                    if (palabrasRestantes>0 && nivel.getIntentos()>0){
                        //Si la palabra ha sido un acierto muestra un toast con las palabras que quedan
                        //por acertar
                        Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.palabrasRestantes)+": "+palabrasRestantes, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    else if (palabrasRestantes==0){
                        //Si ha sifo acertada y no quedan más palabras se termina la partida y se muestra
                        //tanto un toast como una notificación
                        Toast toastGanado=Toast.makeText(getApplicationContext(),getString(R.string.hasGanado), Toast.LENGTH_LONG);
                        toastGanado.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
                        int puntuacion=registrarPuntuacion();

                        //Se genera el diálogo de fin de nivel
                        DialogoFinNivel dialogoFinNivel=new DialogoFinNivel();
                        dialogoFinNivel.show(getSupportFragmentManager(), "etiqueta");
                        String nombre=preferencias.getString("nombreUsuario",null);
                        if (nombre!=null){
                            //La notificación muestra la puntuación del jugador
                            NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(v.getContext(), "IdCanal");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                Intent i = new Intent(v.getContext(),ActividadSeleccionarNivel.class);
                                PendingIntent intentEnNot = PendingIntent.getActivity(v.getContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                                elManager.createNotificationChannel(elCanal);
                                elBuilder.setSmallIcon(android.R.drawable.star_big_on)
                                        .setContentTitle(getString(R.string.hasGanado))
                                        .setContentText(getString(R.string.puntuacionEs)+puntuacion+" "+getString(R.string.pulsaMejora))
                                        .setSubText(getString(R.string.hasGanado))
                                        .setVibrate(new long[]{0, 1000, 500, 1000})
                                        .setAutoCancel(true).setContentIntent(intentEnNot);
                                elManager.notify(1, elBuilder.build());
                            }
                        }
                        else{
                            NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(v.getContext(), "IdCanal");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                                        NotificationManager.IMPORTANCE_DEFAULT);
                                Intent i = new Intent(v.getContext(),Registro.class);
                                PendingIntent intentEnNot = PendingIntent.getActivity(v.getContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                                elManager.createNotificationChannel(elCanal);
                                elBuilder.setSmallIcon(android.R.drawable.star_big_on)
                                        .setContentTitle(getString(R.string.hasGanado))
                                        .setContentText(getString(R.string.puntuacionEs)+" "+nivel.getPuntuacion()+". "+getString(R.string.registrateParaRanking))
                                        .setSubText(getString(R.string.hasGanado))
                                        .setVibrate(new long[]{0, 1000, 500, 1000})
                                        .setAutoCancel(true).setContentIntent(intentEnNot);
                                elManager.notify(1, elBuilder.build());
                            }
                        }

                    }

                    else if (nivel.getIntentos()<=0){
                        //Si no quedan más intentos se llama al diálogo de fin de nivel
                        DialogoFinNivel dialogoFinNivel2=new DialogoFinNivel();
                        dialogoFinNivel2.show(getSupportFragmentManager(), "etiqueta");
                    }

                }
                else {


                    //En caso de no acertar se informa al usuario de ello
                    Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.palabraErronea)+" "+getString(R.string.palabrasRestantes)+": "+nivel.getPalabrasRestantes(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                    toast.show();
                    if (nivel.getIntentos()<=0){
                        //Si se han agotado los intentos se informa de que ha perdido y se genera
                        //el diálogo correspondiente
                        Toast toastPerdido=Toast.makeText(getApplicationContext(),getString(R.string.hasPerdido)+"...", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
                        toast.show();
                        registrarPuntuacion();

                        DialogoFinNivel dialogoFinNivel=new DialogoFinNivel();
                        dialogoFinNivel.show(getSupportFragmentManager(), "etiqueta");
                    }


                }
                //Se actualiza la puntuación y los intentos
                tagPuntuacion.setText(getString(R.string.puntuacion)+": "+nivel.getPuntuacion());
                tagIntentos.setText(getString(R.string.intentos)+": "+nivel.getIntentos());
            }

            //Método que actualiza la puntuación del usuario en la base de datos
            public int registrarPuntuacion() {
                String nombre=preferencias.getString("nombreUsuario",null);
                if (nombre !=null){
                    Log.i("MYAPP","registrando puntuación");
                    //Si hay un usuario registrado se almacena su puntuación
                    String[] campos = new String[]{"Puntuacion"};
                    String[] argumentos = new String[] {nombre};
                    SQLiteDatabase db = GestorDB.getWritableDatabase();
                    Cursor cu = db.query("Usuarios", campos, "NombreUsuario=?", argumentos, null, null, null);
                    Log.i("MYAPP",nombre);
                    int puntuacion=0;
                    if(cu.moveToNext()){
                        puntuacion=cu.getInt(0);
                    }
                    puntuacion=puntuacion+nivel.getPuntuacion();
                    ContentValues cv=new ContentValues();
                    Log.i("MYAPP", String.valueOf(puntuacion));

                    cv.put("Puntuacion",puntuacion);
                    db.update("Usuarios",cv,"NombreUsuario=?",argumentos);
                    return puntuacion;
                                    }
                return 0;
            }
        });






    }

    //En caso de que la aplicación se cierre inseperadamente se almacenan los datos del momento
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putString("nombreNivel",nivel.getNombre());
            savedInstanceState.putInt("idNivel",nivel.getId());

            savedInstanceState.putInt("nivelPuntuacion",nivel.getPuntuacion());
            savedInstanceState.putInt("nivelAciertos",nivel.getAciertos());
            savedInstanceState.putInt("nivelIntentos",nivel.getIntentos());

            savedInstanceState.putStringArrayList("nivelAcertadas",nivel.getPalabrasAcertadas());

    }

    //Implementación de los métodos para gestionar la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantalla_juego_menu_layout,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        DialogoSalir dialogoSalir=new DialogoSalir();
        dialogoSalir.show(getSupportFragmentManager(), "etiqueta");
        return super.onOptionsItemSelected(item);
    }

    //Se implementan las interfaces del diálogo fin de nivel
    @Override
    public void alpulsarMenuNiveles() {
ListaNiveles.getListaNiveles().resetNivel(idNivel);        //Se abre la actividad de menú niveles
        Intent i =new Intent(this, ActividadSeleccionarNivel.class);
        finish();
        startActivity(i);

    }

    @Override
    public void alpulsarSiguienteNivel() {
        ListaNiveles.getListaNiveles().resetNivel(idNivel);

        //Se carga el siguiente nivel en caso de no haber más se vuelve a la pantalla de inicio
        Intent i=new Intent(this,PantallaJuego.class);
        int idNivel=ListaNiveles.getListaNiveles().getSiguienteNivel();
        if (idNivel!=0){
            i.putExtra("id",idNivel);
            startActivity(i);
            finish();
        }
        else {
            Toast toast=Toast.makeText(getApplicationContext(),"No hay más niveles", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
            toast.show();
            Intent i2=new Intent(this,MainActivity.class);
            startActivity(i2);
        }

    }

    @Override
    public void alpulsarOK() {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
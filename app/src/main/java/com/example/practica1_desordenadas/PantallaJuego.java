package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.AlarmManager;
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
import android.os.SystemClock;
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
    int numPistas;
    boolean hayPistas;
    Nivel nivel;
    ArrayAdapter adapter;
    ArrayList<String> lista=new ArrayList<String>();
    ListView intentosAnteriores;
    ListaNiveles listaNiveles = ListaNiveles.getListaNiveles();
    BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
    Button botonPista;
    TextView pistas;
    AlarmManager manager;
    Intent i;
    //Variables para configurar la alarma
    PendingIntent service;
    long inicio = System.currentTimeMillis();
    int tiempo=300000;
    int tiempoResta=0;

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
        pistas=findViewById(R.id.tagPistas);
        botonPista=findViewById(R.id.buttonPista);
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
            int tiempoRestaNuevo=savedInstanceState.getInt("tiempo");
            int tiempoRestaAc=savedInstanceState.getInt("tiempoAcumulado");
            tiempo=tiempo-tiempoRestaNuevo-tiempoRestaAc;
            tiempoResta=tiempoRestaAc+tiempoResta;

            hayPistas=savedInstanceState.getBoolean("hayPistas");

            if(hayPistas){
                numPistas=savedInstanceState.getInt("numPistas");
            }
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
            if(preferencias.getString("nombreUsuario",null)!=null){
                numPistas=preferencias.getInt("pistas",0);
                hayPistas=true;
                pistas.setText(getString(R.string.pistas)+" :"+numPistas);
                botonPista.setText(R.string.pista);


            }
            else{
                hayPistas=false;
                pistas.setText("");
                botonPista.setVisibility(View.INVISIBLE);
                botonPista.setEnabled(false);
            }

        }

        // Se establece una alarma para que termine la partida al de 5 minutos de empezar la partida
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        i = new Intent(this, ServicioTerminarJuego.class);
        service = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        /**
         * Código obtenido de: https://stackoverflow.com/questions/41525944/alarm-manager-not-working-in-my-code
         * Autor: https://stackoverflow.com/users/4393601/pedro-sim%c3%b5es
         * **/
        Log.i("MYAPP","TIEMPO PROGRAMADO"+tiempo);
        Log.i("MYAPP","TIEMPO ACUMULADO"+tiempoResta);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ///manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + tiempo, service);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + tiempo, service);
        } else {
            //manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, service);
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + tiempo, service);
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
        pistas=findViewById(R.id.tagPistas);

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
                        registrarPuntuacion();
                        if(hayPistas){
                            actualizarPistas();
                        }
                        //Al terminar la partida se para la alarma
                        /**Código extraído de StackOverflow
                         Pregunta :https://stackoverflow.com/questions/17615986/how-to-stop-an-alarm-in-android
                         Autor:https://stackoverflow.com/users/1140237/user1140237
                         **/
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(service);

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
                                        .setContentText(getString(R.string.puntuacionEs)+nivel.getPuntuacion()+" "+getString(R.string.pulsaMejora))
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
                        //Al terminar la partida se para la alarma
                        /**Código extraído de StackOverflow
                         Pregunta :https://stackoverflow.com/questions/17615986/how-to-stop-an-alarm-in-android
                         Autor:https://stackoverflow.com/users/1140237/user1140237
                         **/
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(service);

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
                        if(hayPistas){
                            actualizarPistas();
                        }
                        //Al terminar la partida se para la alarma
                        /**Código extraído de StackOverflow
                         Pregunta :https://stackoverflow.com/questions/17615986/how-to-stop-an-alarm-in-android
                         Autor:https://stackoverflow.com/users/1140237/user1140237
                         **/
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(service);

                        DialogoFinNivel dialogoFinNivel=new DialogoFinNivel();
                        dialogoFinNivel.show(getSupportFragmentManager(), "etiqueta");
                    }


                }
                //Se actualiza la puntuación y los intentos
                tagPuntuacion.setText(getString(R.string.puntuacion)+": "+nivel.getPuntuacion());
                tagIntentos.setText(getString(R.string.intentos)+": "+nivel.getIntentos());
            }


            //Método que actualiza la puntuación del usuario en la base de datos

        });

        //Listener del botón de las pistas


        botonPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayPistas){
                    numPistas=numPistas-1;
                    if(numPistas>=0){
                        String pista=nivel.getPista();
                        texto.setText(pista);
                        pistas.setText(getString(R.string.pistas)+": "+numPistas);
                    }
                    else {
                        Toast toast=Toast.makeText(getApplicationContext(),R.string.noPistas, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
                else {
                    Toast toast=Toast.makeText(getApplicationContext(),R.string.necesarioIniciarSesion, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });



    }
    public void registrarPuntuacion() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        String usuario=preferencias.getString("nombreUsuario",null);
        if (usuario !=null){
            int puntuacion=nivel.getPuntuacion();
            Data datos = new Data.Builder()
                    .putString("fichero","usuarios.php")
                    .putString("parametros","funcion=puntuacion&nombreUsuario="+usuario+"&puntuacion="+puntuacion)
                    .build();
            OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("registrarPuntuacion").build();
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                String resultado = workInfo.getOutputData().getString("resultado");
                                Log.i("MYAPP","inicio realizado");

                                Log.i("MYAPP",resultado);

                            }
                        }
                    });
            //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
            WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("registrarPuntuacion", ExistingWorkPolicy.REPLACE,requesContrasena);

        }

    }
    private void actualizarPistas() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        //Se actualiza la cantidad de pistas en las preferencias

        SharedPreferences.Editor editor=preferencias.edit();
        editor.putInt("pistas",numPistas);
        editor.apply();
        String usuario=preferencias.getString("nombreUsuario","");
        //Método que se comunica con la BD para actualizar las pistas del usuario
        //Se obtienen las pistas actuales de las preferencias

        int pistas=preferencias.getInt("pistas",0);
        //Se incrementan en 10 y se añaden a la BD
        pistas=pistas+10;
        Data datos = new Data.Builder()
                .putString("fichero","usuarios.php")
                .putString("parametros","funcion=pistas&nombreUsuario="+usuario+"&pistas="+pistas)
                .build();
        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("modificarPistas").build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            String resultado = workInfo.getOutputData().getString("resultado");
                            Log.i("MYAPP","inicio realizado");

                            Log.i("MYAPP",resultado);

                        }
                    }
                });
        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("modificarPistas", ExistingWorkPolicy.REPLACE,requesContrasena);


    }

    //En caso de que la aplicación se cierre inseperadamente se almacenan los datos del momento
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        //Se para la alarma
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(service);

        long fin = System.currentTimeMillis();
        int tiempo = (int) ((fin - inicio));
        Log.i("MYAPP","TIEMPO"+tiempo);
        //Se almacena el tiempo que ha pasado durante la partida para poder configurar la siguiente alarma quitandole el tiempo
        savedInstanceState.putInt("tiempo",tiempo);
        //Se almacena la resta de tiempo acumulada para poder configurar la siguiente alarma quitandole el tiempo
        savedInstanceState.putInt("tiempoAcumulado",tiempoResta+tiempo);


        savedInstanceState.putString("nombreNivel",nivel.getNombre());
            savedInstanceState.putInt("idNivel",nivel.getId());

            savedInstanceState.putInt("nivelPuntuacion",nivel.getPuntuacion());
            savedInstanceState.putInt("nivelAciertos",nivel.getAciertos());
            savedInstanceState.putInt("nivelIntentos",nivel.getIntentos());
            savedInstanceState.putBoolean("hayPistas",hayPistas);
            if(hayPistas){
                savedInstanceState.putInt("numPistas",numPistas);

            }

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
            ListaNiveles.getListaNiveles().resetNivel(idNivel);
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
        //Al pulsar salir se para la alarma
        /**Código extraído de StackOverflow
         Pregunta :https://stackoverflow.com/questions/17615986/how-to-stop-an-alarm-in-android
         Autor:https://stackoverflow.com/users/1140237/user1140237
         **/
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(service);

        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        /**Código extraído de StackOverflow
         Pregunta: https://github.com/javierarce/palabras/blob/master/listado-general.txt
         Autor: https://es.stackoverflow.com/users/9982/rafael-guti%c3%a9rrez
         **/
        //Al pulsar el botón atras se resetea el nivel
        ListaNiveles.getListaNiveles().resetNivel(idNivel);
        finish();



        //Al pulsar el botón atras se para la alarma
        /**Código extraído de StackOverflow
         Pregunta :https://stackoverflow.com/questions/17615986/how-to-stop-an-alarm-in-android
         Autor:https://stackoverflow.com/users/1140237/user1140237
         **/

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(service);

    }
}
package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class PantallaJuego extends AppCompatActivity implements DialogoFinNivel.ListenerdelDialogoFinNivel{
    String letras;
    int idNivel;
    int idImagen;
    Nivel nivel;
    ArrayAdapter adapter;
    ArrayList<String> lista=new ArrayList<String>();
    ListView intentosAnteriores;
    ListaNiveles listaNiveles = ListaNiveles.getListaNiveles();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        Bundle extras=getIntent().getExtras();
        //Coger el nivel que hay que cargar
        if (extras!=null){

            if (extras.getInt("id")!=0){
                idNivel=extras.getInt("id");


            }

        }
        //Se obtiene el nivel de la base de datos
//        String[] campos = new String[]{"Codigo", "Letras","IdImagen"};
//        String[] argumentos = new String[] {String.valueOf(idImagen)};
//        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
//        SQLiteDatabase db = GestorDB.getWritableDatabase();
//        Cursor cu = db.query("Niveles", campos, "Codigo=?", argumentos, null, null, null);
//        int i=0;
//        cu.moveToNext();
//
//        Log.i("MYAPP","Obteniendo elemento");
//
//        Integer Cod = cu.getInt(0);
//
//        String letrasNivel = cu.getString(1);
//        String idImagen = cu.getString(2);
//        Log.i("MYAPP",idImagen);
//        cu.close();
//        db.close();


        if (savedInstanceState!=null){
            ListaNiveles.getListaNiveles().cargarNiveles(this);

            String nNivel=savedInstanceState.getString("nombreNivel");
            int idNivel=savedInstanceState.getInt("idNivel");
            int pPuntuacion=savedInstanceState.getInt("nivelPuntuacion");
            int nAciertos=savedInstanceState.getInt("nivelAciertos");
            int nIntentos=savedInstanceState.getInt("nivelIntentos");
            Log.i("MYAPP",
                    nNivel+pPuntuacion+nAciertos+nIntentos);
            ArrayList<String> arrayAcertadas=savedInstanceState.getStringArrayList("nivelAcertadas");
            lista=arrayAcertadas;
            HashSet<String> acertadas= new HashSet<>(arrayAcertadas);
            if (acertadas==null){
                acertadas=new HashSet<String>();
            }
            Log.i("MYAPP",
                    String.valueOf(acertadas));
            //nivel=new Nivel(nNivel,pPuntuacion,nAciertos,acertadas,nIntentos);
            ListaNiveles.getListaNiveles().setNivel(idNivel,pPuntuacion,nAciertos,acertadas,nIntentos);
            nivel=ListaNiveles.getListaNiveles().getNivel(idNivel);
            ListaNiveles.getListaNiveles().setNivelAct(nivel);
            idImagen=nivel.getIdImagen();

        }
        else{
            ListaNiveles.getListaNiveles().cargarNiveles(this);
            nivel=ListaNiveles.getListaNiveles().getNivel(idNivel);
            ListaNiveles.getListaNiveles().setNivelAct(nivel);
            idImagen=nivel.getIdImagen();
        }




        //Se dibuja la imagen correspondiente
        ImageView imagen= findViewById(R.id.imageView);
        imagen.setImageResource(idImagen);
        //Se crea el nivel partiendo de los datos obtenidos


        //Se cargan los elementos de la pantalla
        intentosAnteriores=findViewById(R.id.lista);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        intentosAnteriores.setAdapter(adapter);
        Button boton=findViewById(R.id.botonAñadir);
        EditText texto=(EditText) findViewById(R.id.textoPalabra);
        TextView tagPuntuacion=findViewById(R.id.tagPuntuacion);
        TextView tagIntentos=findViewById(R.id.tagIntentos);
        tagPuntuacion.setText("Puntuación: "+nivel.getPuntuacion());
        tagIntentos.setText("Intentos: "+nivel.getIntentos());

        //listener del boton
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String palabra=texto.getText().toString();
                if (nivel.jugar(palabra)!=null){
                    lista.add(palabra);
                    ArrayAdapter adapter= (ArrayAdapter) intentosAnteriores.getAdapter();
                    adapter.notifyDataSetChanged();
                    Log.i("MYAPP", String.valueOf(nivel.getNumeroPalabras()));
                    int palabrasRestantes=nivel.getPalabrasRestantes();
                    if (palabrasRestantes>0){
                        Toast toast=Toast.makeText(getApplicationContext(),"Te quedan "+palabrasRestantes+" palabras", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                    else{
                        Log.i("MYAPP", "HAS GANADOOO");

                        DialogoFinNivel dialogoFinNivel=new DialogoFinNivel();
                        dialogoFinNivel.show(getSupportFragmentManager(), "etiqueta");
                    }

                }
                else {

                    Toast toast=Toast.makeText(getApplicationContext(),"No has acertado! "+nivel.getPalabrasRestantes()+" palabras", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                    toast.show();

                }

                tagPuntuacion.setText("Puntuación: "+nivel.getPuntuacion());
                tagIntentos.setText("Intentos: "+nivel.getIntentos());
            }
        });
        //listener del edit text
        texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MYAPP","clicado edittext");
                texto.getText().clear();
            }
        });





    }
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

    @Override
    public void alpulsarMenuNiveles() {
        Intent i =new Intent(this, ActividadSeleccionarNivel.class);
        startActivity(i);
        finish();
    }

    @Override
    public void alpulsarSiguienteNivel() {
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
}
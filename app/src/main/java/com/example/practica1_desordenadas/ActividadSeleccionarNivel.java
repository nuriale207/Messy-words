package com.example.practica1_desordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ActividadSeleccionarNivel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccionar_nivel);
        // CONFIGURACIÓN DE LA VISTA DEL RECYCLER VIEW
        RecyclerView elRecyclerView=findViewById(R.id.recyclerView);

        //Lista con las imágenes y las letras
        ArrayList<Integer> imagenes=new ArrayList<Integer>();
        ArrayList<String> letras=new ArrayList<String>();

        //Se obtienen los niveles de la base de datos
        String[] campos = new String[]{"Codigo", "Letras","IdImagen"};
        BaseDeDatos GestorDB = new BaseDeDatos (this, "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        Cursor cu = db.query("Niveles", campos, null, null, null, null, null);
        int i=0;
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");

            int Cod = cu.getInt(0);
            String letrasNivel = cu.getString(1);
            String idImagen = cu.getString(2);
            Log.i("MYAPP",idImagen);
            imagenes.add(getResources().getIdentifier(idImagen,"drawable",getPackageName()));
            letras.add(letrasNivel);

        }
        cu.close();
        db.close();
        //Se añaden los niveles al recycler view
        AdaptadorRecyclerNivel elAdaptaador=new AdaptadorRecyclerNivel(letras,imagenes);
        elRecyclerView.setAdapter(elAdaptaador);
        StaggeredGridLayoutManager elLayoutRejillaDesigual =
                new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        elRecyclerView.setLayoutManager(elLayoutRejillaDesigual);


    }
}
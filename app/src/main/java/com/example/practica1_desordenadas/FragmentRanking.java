package com.example.practica1_desordenadas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentRanking extends Fragment {
    TextView etiquetaRanking;
    ListView listaRanking;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MYAPP","actividad creadaaaaa");
        etiquetaRanking= getView().findViewById(R.id.Ranking);
        listaRanking= getView().findViewById(R.id.listaRanking);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    public void cargarRanking() {
        ArrayList<String> contenidoLista=new ArrayList<String>();
        //Se obtienen los usuarios de la base de datos
        String[] campos = new String[]{"NombreUsuario", "Puntuacion"};
        BaseDeDatos GestorDB = new BaseDeDatos (this.getActivity(), "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        Cursor cu = db.query("Usuarios", campos, null, null, null, null, null);
        int i=0;
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");
            String nombreUsuario = cu.getString(0);
            int puntuacion = cu.getInt(1);
            contenidoLista.add("Usuario: "+nombreUsuario+"\t Puntuación: "+puntuacion);

        }

        cu.close();
        db.close();
        listaRanking.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, contenidoLista));


    }
}
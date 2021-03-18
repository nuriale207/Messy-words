package com.example.practica1_desordenadas;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentRanking extends Fragment {
    ListView listaRanking;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MYAPP","actividad creadaaaaa");
        listaRanking= getView().findViewById(R.id.listaRanking);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    public void cargarRanking() {
        listaRanking= getView().findViewById(R.id.listaRanking);

        ArrayList<String> listaNombreUsuario=new ArrayList<String>();
        ArrayList<String> listaPuntuacion=new ArrayList<String>();

        //Se obtienen los usuarios de la base de datos
        String[] campos = new String[]{"NombreUsuario", "Puntuacion"};
        BaseDeDatos GestorDB = new BaseDeDatos (this.getActivity(), "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        String orderBy="Puntuacion DESC";
        Cursor cu = db.query("Usuarios", campos, null, null, null, null, orderBy);
        int i=0;
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");
            String nombreUsuario = cu.getString(0);
            int puntuacion = cu.getInt(1);

                //listaNombreUsuario.add(getString(R.string.usuario)+": "+nombreUsuario+" \t "+ getString(R.string.puntuacion)+": "+puntuacion);
            listaNombreUsuario.add(getString(R.string.usuario)+": "+nombreUsuario);
            listaPuntuacion.add(getString(R.string.puntuacion)+": "+puntuacion);
        }

        cu.close();
        db.close();

        //listaRanking.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_2,listaNombreUsuario));

        ArrayAdapter eladaptador =
                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_2,android.R.id.text1,listaNombreUsuario){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View vista= super.getView(position, convertView, parent);
                        TextView lineaprincipal=(TextView) vista.findViewById(android.R.id.text1);
                        TextView lineasecundaria=(TextView) vista.findViewById(android.R.id.text2);
                        lineaprincipal.setText(listaNombreUsuario.get(position));
                        lineasecundaria.setText(listaPuntuacion.get(position));
                        return vista;
                    }
                };
        listaRanking.setAdapter(eladaptador);

        listaRanking.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String texto=((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                texto=texto.split(" ")[1];
                Log.d("etiqueta", texto);

                Intent i=new Intent(view.getContext(),MostrarPerfil.class );
                i.putExtra("usuario",texto);

                startActivity(i);




            }
        });

    }
}
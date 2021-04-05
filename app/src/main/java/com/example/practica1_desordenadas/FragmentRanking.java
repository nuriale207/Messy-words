package com.example.practica1_desordenadas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.service.notification.NotificationListenerService;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentRanking extends Fragment {
    ListView listaRanking;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //al crear la actividad se carga la lista de ranking
        super.onActivityCreated(savedInstanceState);
        listaRanking= getView().findViewById(R.id.listaRanking);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    public void cargarRanking() {
        //Método que carga el ranking en la list view
        listaRanking= getView().findViewById(R.id.listaRanking);

        ArrayList<String> listaNombreUsuario=new ArrayList<String>();
        ArrayList<String> listaPuntuacion=new ArrayList<String>();

        //Se obtienen los usuarios de la base de datos
        /*String[] campos = new String[]{"NombreUsuario", "Puntuacion"};
        BaseDeDatos GestorDB = new BaseDeDatos (this.getActivity(), "NombreBD", null, 1);
        SQLiteDatabase db = GestorDB.getWritableDatabase();
        String orderBy="Puntuacion DESC";
        Cursor cu = db.query("Usuarios", campos, null, null, null, null, orderBy);
        int i=0;
        while (cu.moveToNext()) {
            Log.i("MYAPP","Obteniendo elemento");
            String nombreUsuario = cu.getString(0);
            int puntuacion = cu.getInt(1);
            //Se añaden los datos a las listas
            listaNombreUsuario.add(getString(R.string.usuario)+": "+nombreUsuario);
            listaPuntuacion.add(getString(R.string.puntuacion)+": "+puntuacion);
        }

        cu.close();
        db.close();*/
        //Se establece a que parte del servidor se hace la consulta y con que parámetros
        Data datos = new Data.Builder()
                .putString("fichero","usuarios.php")
                .putString("parametros","funcion=listar")
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBD.class).setInputData(datos).addTag("ranking").build();
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            String resultado=workInfo.getOutputData().getString("resultado");

                            try {
                                /**
                                 * Código obtenido de stackoverflow
                                 * Link a la pregunta:https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
                                 * Perfil del usuario:https://stackoverflow.com/users/432209/user432209
                                 */
                                Log.i("MYAPP",resultado);
                                JSONArray jsonArray = new JSONArray(resultado);

                                for(int x = 0; x < jsonArray.length(); x++) {
                                   JSONObject elemento = jsonArray.getJSONObject(x);
                                    Log.i("MYAPP", String.valueOf(elemento));
                                    Log.i("MYAPP",elemento.getString("NombreUsuario"));
                                    Log.i("MYAPP", String.valueOf(elemento.getInt("Puntuacion")));

                                    listaNombreUsuario.add(getString(R.string.usuario)+": "+elemento.getString("NombreUsuario"));
                                    listaPuntuacion.add(getString(R.string.puntuacion)+": "+elemento.getInt("Puntuacion"));
                                }
                                //Configuración del adaptador para incluir los dos elementos

                                ArrayAdapter eladaptador =
                                        new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_2,android.R.id.text1,listaNombreUsuario){
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
                                //Al pulsar sobre uno de los usuarios se muestra su perfil
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

                                   /* String img64=jsonObj.getString("Imagen");
                                Log.i("MYAPP",img64);

                                byte[] fotoEnBytes= Base64.decode(img64,Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(fotoEnBytes, 0,fotoEnBytes.length);
                                imagen.setImageBitmap(decodedByte);*/


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("MYAPP","Error con el JSON");
                            }
                            Log.i("MYAPP",resultado);
                        }
                    }
                });
        WorkManager.getInstance(this.getContext()).enqueueUniqueWork("ranking", ExistingWorkPolicy.REPLACE,otwr);

        //Configuración del adaptador para incluir los dos elementos

       /* ArrayAdapter eladaptador =
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
        //Al pulsar sobre uno de los usuarios se muestra su perfil
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
        });*/

    }
}
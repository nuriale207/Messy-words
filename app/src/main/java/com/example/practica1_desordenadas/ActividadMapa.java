package com.example.practica1_desordenadas;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ActividadMapa extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mapa;
    Button empezarRuta;
    Button otraRuta;
    Marker posicionActual;
    Marker posicionInicial;
    Marker posicionDestino;
    TextView textoPistas;
    TextView textoInstrucciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Paso 0: Se mira el tema que tiene que tener la actividad
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
        setContentView(R.layout.activity_main);

        //Paso 2: Gestión del idioma
        //Paso 1: miro el idioma de las preferencias
        String idiomaConfigurado=preferencias.getString("idioma","castellano");
        String sufijoIdioma="es";
        if (idiomaConfigurado.equals("Euskera")){
            sufijoIdioma="eu";
        }

        //Paso 2: miro la localización del dispositivo
        String localizacionActual= getResources().getString(R.string.localizacion);

        //Paso 3: si la localización no coincide con el idioma de las preferencias se
        //cambia al idioma correspondiente
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
        //Paso 3: se añade el texto de los textview y botones
        setContentView(R.layout.activity_actividad_mapa);
        SupportMapFragment elfragmento =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);
        empezarRuta = findViewById(R.id.botonEmpezarRuta);
        otraRuta=findViewById(R.id.otraRuta);
        empezarRuta.setText(R.string.comenzarRuta);
        otraRuta.setText(R.string.generarOtraRuta);

        textoPistas=findViewById(R.id.textNumPistas);
        textoInstrucciones=findViewById(R.id.textInstrucciones);
        textoInstrucciones.setText(R.string.planificaRutaParaPistas);
        int pistas=preferencias.getInt("pistas",0);
        textoPistas.setText(getString(R.string.tuCantidadDePistasEs)+" "+pistas);

    }

    @Override
    public void onMapReady(GoogleMap elmapa) {
        //Cuando el mapa está listo se elige el tipo de mapa a mostrar y se solicita el permiso de localización
        mapa = elmapa;

        elmapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        FusedLocationProviderClient proveedordelocalizacion =
                LocationServices.getFusedLocationProviderClient(this);
        //En caso de que el permiso esté denegado se solicita
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);


        } else {
            //Se obtiene la posición del usuario cada 5 segundos
            LocationRequest peticion = LocationRequest.create();
            peticion.setInterval(1000);
            peticion.setFastestInterval(5000);
            peticion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationCallback actualizador = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null) {
                        Location location=locationResult.getLastLocation();
                        //Si no se ha planificado una ruta anteriormente se muestra una posición de destino aleatoria a 500 metros de la posición actual
                        if(!hayRuta()){

                            posicionActual = elmapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("Tu posición"));

                            CameraUpdate actualizar = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
                            elmapa.animateCamera(actualizar);
                            LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng puntoDestino = getRandomLocation(punto, 500);
                            if(posicionDestino==null){
                                posicionDestino = elmapa.addMarker(new MarkerOptions()
                                        .position(puntoDestino)
                                        .title("Posición destino"));

                            }
                        }
                        else{
                            //Si se ha planificado una ruta anteriormente se vacía el mapa y se muestra tanto la ruta como la posición actual modificada

                            mapa.clear();
                            if(posicionActual!=null){
                                posicionActual.remove();
                            }
                            posicionActual = elmapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("Tu posición"));
                            mostrarRuta();

                            comprobarRuta();
                        }


                    } else {
                        //Se muestra un mensaje indicando que ha habido un error al obtener la posición
                        Toast toast=Toast.makeText(getApplicationContext(),"No se ha podido obtener la ubicación en tiempo real", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            };
            proveedordelocalizacion.requestLocationUpdates(peticion, actualizador, null);
        }

        //Al pulsar el boton empezar ruta
        empezarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se almacenan en la preferencia las coordenadas de la ruta para poder retomarla en caso de cerrar la app
                SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putFloat("longitudInicio", (float) posicionActual.getPosition().longitude);
                editor.putFloat("latitudInicio",(float) posicionActual.getPosition().latitude);
                editor.putFloat("longitudDestino", (float) posicionDestino.getPosition().longitude);
                editor.putFloat("latitudDestino", (float) posicionDestino.getPosition().latitude);
                editor.apply();
                //Se establece que la posición inicial sea la actual
                posicionInicial=posicionActual;
                //Se dibuja la ruta
                generarRuta();

            }
        });
        //Al pulsar el boton generar otra ruta
        otraRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se elimina la ruta establecida
                eliminarRuta();
                //Se muestra la posición actual y una nueva posición de destino establecida aleatoriamente
                posicionActual=mapa.addMarker(new MarkerOptions()
                        .position(posicionActual.getPosition())
                        .title("Tu posición"));
                LatLng puntoDestino = getRandomLocation(posicionActual.getPosition(), 500);
                posicionDestino.remove();
                posicionDestino=mapa.addMarker(new MarkerOptions()
                        .position(puntoDestino)
                        .title("Posición destino"));


            }
        });
    }

    private void mostrarRuta() {
        //Método que se encarga de mostrar la ruta almacenada
        //Obtiene las coordenadas de las preferencias y añade los marcadores
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        double longInicio=preferencias.getFloat("longitudInicio",0);
        double latitudInicio=preferencias.getFloat("latitudInicio",0);
        double longitudDestino=preferencias.getFloat("longitudDestino",0);
        double latitudDestino=preferencias.getFloat("latitudDestino",0);
        posicionInicial=mapa.addMarker(new MarkerOptions()
                .position(new LatLng(latitudInicio,longInicio))
                .title("Posición inicial"));
        posicionDestino=mapa.addMarker(new MarkerOptions()
                .position(new LatLng(latitudDestino,longitudDestino))
                .title("Posición destino"));
        //Llama al método encargado de dibujar las líneas en el mapa
        generarRuta();
    }

    private void eliminarRuta(){
        //Elimina el contenido del mapa y la ruta de las preferencias
        mapa.clear();
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferencias.edit();
        editor.remove("longitudInicio");
        editor.remove("latitudInicio");
        editor.remove("longitudDestino");
        editor.remove("latitudDestino");
        editor.apply();
    }

    private void comprobarRuta(){
        //Método que comprueba si se ha llegado al destino y añade las pistas y resetea el mapa en caso de haber llegado
        boolean llegado=comprobarSiLlegado();

        if(llegado){
            //Mostrar diálogo con la cantidad de pistas obtenidas
            DialogoMapa dialogoMapa=new DialogoMapa();
            dialogoMapa.show(getSupportFragmentManager(), "etiqueta");

            //Añade las pistas al usuario
            añadirPistas();

            //Resetear mapa
            eliminarRuta();
            posicionActual=mapa.addMarker(new MarkerOptions()
                    .position(posicionActual.getPosition())
                    .title("Tu posición"));
            LatLng puntoDestino = getRandomLocation(posicionActual.getPosition(), 500);
            posicionDestino.remove();
            posicionDestino=mapa.addMarker(new MarkerOptions()
                    .position(puntoDestino)
                    .title("Posición destino"));
        }
    }

    private void añadirPistas() {
        //Método que se comunica con la BD para actualizar las pistas del usuario
        //Se obtienen las pistas actuales de las preferencias
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario=preferencias.getString("nombreUsuario","");
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
        //Se actualiza la cantidad de pistas en las preferencias
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt("pistas",pistas);
        editor.apply();
    }

    private boolean comprobarSiLlegado() {
        //Comprueba si se ha llegado al destino con 10 metros de margen
        //Genera la localizacion actual y la de destino
        LatLng posInicio=posicionActual.getPosition();
        Location localizacionA=new Location("punto A");
        localizacionA.setLatitude(posInicio.latitude);
        localizacionA.setLongitude(posInicio.longitude);
        LatLng posDestin=posicionDestino.getPosition();
        Location localizacionB=new Location("punto B");
        localizacionB.setLatitude(posDestin.latitude);
        localizacionB.setLongitude(posDestin.longitude);

        //Calcula la distancia entre las dos localizaciones
        float distancia=localizacionA.distanceTo(localizacionB);
        boolean llegado=false;
        if (distancia<=10){
            llegado=true;
        }
        return llegado;
    }


    private boolean hayRuta() {
        //Método que comprueba si existe una ruta almacenada
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(preferencias.contains("longitudInicio")){
            return true;
        }
        else{
            return false;
        }
    }



    public LatLng getRandomLocation(LatLng point, int radius) {
        /**
         Codigo obtenido de StackOverflow:
         Pregunta:https://stackoverflow.com/questions/33976732/generate-random-latlng-given-device-location-and-radius
         Usuario:https://stackoverflow.com/users/5242161/abhishek
         **/
        //Obtiene una localización aleatoria a radius metros del punto point pasado como parámetro
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);


        double x0 = point.latitude;
        double y0 = point.longitude;

        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLatitude = new_x + x0;
        double foundLongitude = y + y0;
        LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
        randomPoints.add(randomLatLng);
        Location l1 = new Location("");
        l1.setLatitude(randomLatLng.latitude);
        l1.setLongitude(randomLatLng.longitude);
        randomDistances.add(l1.distanceTo(myLocation));

        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Tu posición"));
    }



    private void generarRuta() {
        //Genera una ruta entre dos puntos para ello crea una lista de LatLng y llama a un método que dibuja una polyline
          List<LatLng> list=new ArrayList<LatLng>();
          list.add(posicionInicial.getPosition());
          if(posicionActual!=null){
              list.add(posicionActual.getPosition());

          }
          list.add(posicionDestino.getPosition());
          drawPolyLineOnMap(list);

    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        /**
         Codigo obtenido de StackOverflow:
         Pregunta:https://stackoverflow.com/questions/17425499/how-to-draw-interactive-polyline-on-route-google-maps-v2-android
         Usuario:https://stackoverflow.com/users/5242161/abhishek
         **/
        //Método que dibuja líneas entre los puntos pasados como parámetros
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);

        mapa.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        CameraUpdate actualizar = CameraUpdateFactory.newLatLngZoom(this.posicionDestino.getPosition(), 15);
        mapa.animateCamera(actualizar);
        //mapa.animateCamera(cu);
    }

    //Métodos añadidos para poder realizar una petición a la api directions, finalmente no se hizo porque la api resultó ser de pago
    /**
     * Method to create URL to directions api
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=AIzaSyAtorhrGKh2yh4mXKX1iwm5PE-oi0cbjpY";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    /**
     *  Method that Receives a JSONObject and returns a list of lists containing latitude and longitude10/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */

    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }

    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}
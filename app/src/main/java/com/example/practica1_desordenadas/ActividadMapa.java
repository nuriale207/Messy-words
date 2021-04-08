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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferencias.edit();
        editor.remove("longitudInicio");
        editor.remove("latitudInicio");
        editor.remove("longitudDestino");
        editor.remove("latitudDestino");
        editor.apply();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mapa);
        SupportMapFragment elfragmento =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);
        empezarRuta = findViewById(R.id.botonEmpezarRuta);
        otraRuta=findViewById(R.id.otraRuta);
        LocationCallback actualizador = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {

                } else {

                }
            }
        };


    }

    @Override
    public void onMapReady(GoogleMap elmapa) {
        mapa = elmapa;

        elmapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        FusedLocationProviderClient proveedordelocalizacion =
                LocationServices.getFusedLocationProviderClient(this);
        CameraUpdate actualizar = CameraUpdateFactory.newLatLngZoom(new LatLng(43.26, -2.95), 9);
        elmapa.animateCamera(actualizar);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);


        } else {

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
                            if(posicionActual!=null){
                                posicionActual.remove();
                            }
                            posicionActual.remove();
                            posicionActual = elmapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("Tu posición"));
                        }


                    } else {

                    }
                }
            };
            proveedordelocalizacion.requestLocationUpdates(peticion, actualizador, null);
        }

        empezarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putFloat("longitudInicio", (float) posicionActual.getPosition().longitude);
                editor.putFloat("latitudInicio",(float) posicionActual.getPosition().latitude);
                editor.putFloat("longitudDestino", (float) posicionDestino.getPosition().longitude);
                editor.putFloat("latitudDestino", (float) posicionDestino.getPosition().latitude);
                editor.apply();
                posicionInicial=posicionActual;
                generarRuta();

            }
        });

        otraRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng puntoDestino = getRandomLocation(posicionActual.getPosition(), 500);
                posicionDestino.remove();
                posicionDestino=elmapa.addMarker(new MarkerOptions()
                        .position(puntoDestino)
                        .title("Posición destino"));
                SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferencias.edit();
                editor.remove("longitudInicio");
                editor.remove("latitudInicio");
                editor.remove("longitudDestino");
                editor.remove("latitudDestino");
                editor.apply();


            }
        });
    }



    private boolean hayRuta() {
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
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        //for(int i = 0; i<10; i++) {
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
        // }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }

    private void getRandomPosition(double pLatitude, double pLongitude) throws IOException {
        String ciudad = getCurrentCity(pLatitude, pLongitude);

        if (!ciudad.equals("")) {

        } else {
            // do your stuff
        }
    }

    private String getCurrentCity(double pLatitude, double pLongitude) throws IOException {
        /**
         Codigo obtenido de StackOverflow:
         Pregunta:https://stackoverflow.com/questions/2296377/how-to-get-city-name-from-latitude-and-longitude-coordinates-in-google-maps
         Usuario:https://stackoverflow.com/users/143505/ccheneson
         **/
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(pLatitude, pLongitude, 1);
        String city = "";
        if (addresses.size() > 0) {
            city = addresses.get(0).getLocality();
            Log.i("MYAPP", city);
        }
        return city;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Tu posición"));
    }



    private void generarRuta() {
        // Getting URL to the Google Directions API

        String url = getDirectionsUrl(this.posicionInicial.getPosition(),this.posicionDestino.getPosition());

        Data datos = new Data.Builder()
                .putString("url",url)
                .build();
        OneTimeWorkRequest requesContrasena = new OneTimeWorkRequest.Builder(ConexionMaps.class).setInputData(datos).addTag("comprobarInicio").build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requesContrasena.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            String resultado = workInfo.getOutputData().getString("resultado");

                            Log.i("MYAPP",resultado);


                        }
                    }
                });
        //WorkManager.getInstance(getApplication().getBaseContext()).enqueue(requesContrasena);
        WorkManager.getInstance(getApplication().getBaseContext()).enqueueUniqueWork("comprobarInicio", ExistingWorkPolicy.REPLACE,requesContrasena);


    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=AIzaSyAtorhrGKh2yh4mXKX1iwm5PE-oi0cbjpY";

        // Building the parameters to the web service
        String parameters = str_origin+"&amp;"+str_dest+"&amp;"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
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
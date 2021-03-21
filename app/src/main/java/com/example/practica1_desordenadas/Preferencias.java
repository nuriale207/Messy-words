package com.example.practica1_desordenadas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class Preferencias extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //Paso 0: Mirar el tema que tiene que tener la app
            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String tema=preferencias.getString("tema","Greenish blue");
            if(tema.equals("Greenish blue")){
                getActivity().setTheme(R.style.TemaDesordenadasGreen);
            }
            else{
                getActivity().setTheme(R.style.TemaDesordenadasPurple);
            }
            addPreferencesFromResource(R.xml.root_preferences);
        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            //Se comprueba el idioma de la app
            if (s.equals("idioma")){

                String idioma=sharedPreferences.getString("idioma","castellano");
                Log.i("MYAPP",idioma);

                if (idioma.equals("Euskera")){
                    Locale nuevaloc = new Locale("eu");

                    Locale.setDefault(nuevaloc);
                    Configuration configuration =
                            this.getActivity().getBaseContext().getResources().getConfiguration();
                    configuration.setLocale(nuevaloc);
                    configuration.setLayoutDirection(nuevaloc);
                    Context context =
                            this.getActivity().getBaseContext().createConfigurationContext(configuration);
                    this.getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
                    this.getActivity().finish();
                    Intent i=new Intent(this.getActivity(), MainActivity.class);
                    startActivity(i);

                }
                else{
                    Locale nuevaloc = new Locale("es");

                    Locale.setDefault(nuevaloc);
                    Configuration configuration =
                            this.getActivity().getBaseContext().getResources().getConfiguration();
                    configuration.setLocale(nuevaloc);
                    configuration.setLayoutDirection(nuevaloc);
                    Context context =
                            this.getActivity().getBaseContext().createConfigurationContext(configuration);
                    this.getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
                    this.getActivity().finish();
                    Intent i=new Intent(this.getActivity(), MainActivity.class);
                    startActivity(i);
                }


            }
            else if(s.equals("tema")){
                Log.i("MYAPP","CAMBIANDO EL TEMA");
                String tema=sharedPreferences.getString("tema","Greenish blue");

                Intent i=new Intent(this.getActivity(), MainActivity.class);
                startActivity(i);

                Log.i("MYAPP",
                        tema);
                Log.i("MYAPP",
                        s);
            }
        }

        @Override
        public void onResume() {
                super.onResume();
                getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
        @Override
            public void onPause() {
                super.onPause();
                getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }


}

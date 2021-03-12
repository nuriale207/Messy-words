package com.example.practica1_desordenadas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

public class Preferencias extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.root_preferences);
        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

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

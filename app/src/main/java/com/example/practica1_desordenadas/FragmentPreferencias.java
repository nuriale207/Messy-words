package com.example.practica1_desordenadas;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class FragmentPreferencias extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
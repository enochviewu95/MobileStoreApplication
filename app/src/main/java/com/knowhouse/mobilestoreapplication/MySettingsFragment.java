package com.knowhouse.mobilestoreapplication;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_layout,rootKey);
    }
}

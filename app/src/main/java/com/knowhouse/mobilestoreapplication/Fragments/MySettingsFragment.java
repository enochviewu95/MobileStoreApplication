package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.knowhouse.mobilestoreapplication.R;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_layout,rootKey);
    }
}

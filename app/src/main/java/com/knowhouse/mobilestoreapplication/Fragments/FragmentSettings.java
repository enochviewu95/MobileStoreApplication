package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.mobilestoreapplication.MySettingsFragment;
import com.knowhouse.mobilestoreapplication.R;

public class FragmentSettings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        if(getActivity()!=null){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_fragment,new MySettingsFragment())
                    .commit();
        }
        return view;
    }

}
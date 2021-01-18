package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.knowhouse.mobilestoreapplication.R;

public class FragmentContentSwitch extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_switch,container,false);

        if(getActivity() != null){
            FragmentCategories fragmentCategories = new FragmentCategories();
            fragmentCategories.setArguments(getActivity().getIntent().getExtras());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_switch,fragmentCategories).commit();

        }
        return view;
    }

}
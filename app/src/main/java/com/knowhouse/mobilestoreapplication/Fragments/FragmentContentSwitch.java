package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.mobilestoreapplication.R;

public class FragmentContentSwitch extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_switch,container,false);

        if(getActivity() != null){
            FragmentMain fragmentMain = new FragmentMain();
            fragmentMain.setArguments(getActivity().getIntent().getExtras());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_switch,fragmentMain).commit();

        }

        return view;
    }

}
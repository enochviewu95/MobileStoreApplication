package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.RecyclerViewClickInterface;

import java.util.Objects;

public class FragmentContentSwitch extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_switch,container,false);

        FragmentMain fragmentMain = new FragmentMain();
        fragmentMain.setArguments(requireActivity().getIntent().getExtras());

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.content_switch,fragmentMain).commit();

        return view;
    }

}
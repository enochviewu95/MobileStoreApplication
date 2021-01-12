package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.knowhouse.mobilestoreapplication.R;

public class FragmentOrder extends Fragment {

    public static final String ARG_POSITION = "position";
    public static final String ARG_URL = "url";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        assert args != null;
        int position = args.getInt(FragmentOrder.ARG_POSITION);
        String url = args.getString(FragmentOrder.ARG_URL);
        Log.i("FragmentOrder",String.valueOf(position));
        Log.i("FragmentOrder",url);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }


}
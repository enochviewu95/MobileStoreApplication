package com.knowhouse.mobilestoreapplication.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.knowhouse.mobilestoreapplication.Fragments.FragmentDescription;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentMap;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentOrder;


public class StateAdapter extends FragmentStateAdapter {



    public StateAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new FragmentOrder();
                return fragment;
            case 1:
                fragment = new FragmentMap();
                return fragment;
            default:
                fragment = new FragmentDescription();
                return fragment;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

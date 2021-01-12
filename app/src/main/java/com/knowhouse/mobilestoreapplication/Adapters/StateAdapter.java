package com.knowhouse.mobilestoreapplication.Adapters;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.knowhouse.mobilestoreapplication.Fragments.FragmentCollection;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentDescription;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentMap;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentOrder;


public class StateAdapter extends FragmentStateAdapter {


    private Bundle itemValues;

    public StateAdapter(Fragment fragment,Bundle itemValues) {
        super(fragment);
        this.itemValues = itemValues;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        Bundle orderDetails = new Bundle();
        int itemPositionValue = itemValues.getInt(FragmentCollection.ARGS_POSITION);
        String itemUrl = itemValues.getString(FragmentCollection.ARGS_URL);

        switch(position){
            case 0:
                orderDetails.putInt(FragmentOrder.ARG_POSITION,itemPositionValue);
                orderDetails.putString(FragmentOrder.ARG_URL,itemUrl);
                fragment = new FragmentOrder();
                fragment.setArguments(orderDetails);
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

package com.knowhouse.mobilestoreapplication.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.knowhouse.mobilestoreapplication.Adapters.StateAdapter;
import com.knowhouse.mobilestoreapplication.BlurBuilder;
import com.knowhouse.mobilestoreapplication.R;

import java.util.Objects;


public class FragmentCollection extends Fragment {


    public static final String ARGS_POSITION = "position";
    StateAdapter stateAdapter;
    ViewPager2 viewPager;
    Toolbar blurredImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");

        //TODO: Get position using constant and use it to populate the view
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lucy_image);
        Bitmap blurredBitmap = BlurBuilder.blur(getActivity(),originalBitmap);


        blurredImage = toolbar;
        blurredImage.setBackground(new BitmapDrawable(getResources(),blurredBitmap));

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        stateAdapter = new StateAdapter(this);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(stateAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tablayout_viewpager);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText("Order");
                            break;
                        case 1:
                            tab.setText("Map");
                            break;
                        default:
                            tab.setText("About");
                    }
                }
        );

        tabLayoutMediator.attach();
    }

}
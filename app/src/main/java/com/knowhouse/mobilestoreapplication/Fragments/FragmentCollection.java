package com.knowhouse.mobilestoreapplication.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.knowhouse.mobilestoreapplication.Adapters.StateAdapter;
import com.knowhouse.mobilestoreapplication.BlurBuilder;
import com.knowhouse.mobilestoreapplication.R;



public class FragmentCollection extends Fragment {


    StateAdapter stateAdapter;
    ViewPager2 viewPager;
    AppBarLayout blurredImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Bitmap orignalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lucy_image);
        Bitmap blurredBitmap = BlurBuilder.blur(getActivity(),orignalBitmap);

        setAppBarHeight(view);

        blurredImage = view.findViewById(R.id.app_bar_layout);
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

    private void setAppBarHeight(View view){
        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight()+dpToPx(48+56)));
    }

    private int getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    private int dpToPx(int dp){{
        float density = getResources()
                .getDisplayMetrics()
                .density;

        return Math.round((float) dp * density);
    }
    }
}
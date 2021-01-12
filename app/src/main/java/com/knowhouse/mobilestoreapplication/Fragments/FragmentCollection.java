package com.knowhouse.mobilestoreapplication.Fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.knowhouse.mobilestoreapplication.Adapters.StateAdapter;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.Utilities.BlurBuilder;

import java.util.Objects;


public class FragmentCollection extends Fragment {


    public static final String ARGS_POSITION = "position";
    public static final String ARGS_URL = "url";
    private Toolbar blurredImage;
    private Toolbar toolbar;
    private Bundle itemValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        itemValue = getArguments();
        toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        String url = itemValue.getString(FragmentCollection.ARGS_URL);
        convertBitmapToDrawable(url);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StateAdapter stateAdapter = new StateAdapter(this, itemValue);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(stateAdapter);
        viewPager.setUserInputEnabled(false);
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

    private void convertBitmapToDrawable(String url){
        Glide.with(this)
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap originalBitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(),resource.getIntrinsicHeight()
                                ,Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(originalBitmap);
                        resource.setBounds(0,0,resource.getIntrinsicWidth(),resource.getIntrinsicHeight());
                        resource.draw(canvas);
                        Bitmap blurredBitmap = BlurBuilder.blur(getActivity(),originalBitmap);
                        blurredImage = toolbar;
                        blurredImage.setBackground(new BitmapDrawable(getResources(),blurredBitmap));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

}
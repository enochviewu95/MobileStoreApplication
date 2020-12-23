package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.knowhouse.mobilestoreapplication.R;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if(getActivity() != null){

            GoogleMapOptions options = new GoogleMapOptions();
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map,mapFragment)
                    .commit();
                mapFragment.getMapAsync(this);

            options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                    .compassEnabled(true)
                    .tiltGesturesEnabled(true)
                    .zoomControlsEnabled(true);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add a marker in current location
        LatLng location = new LatLng(-34,151);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
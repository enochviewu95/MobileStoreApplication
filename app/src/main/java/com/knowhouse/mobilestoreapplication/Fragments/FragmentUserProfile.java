package com.knowhouse.mobilestoreapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.knowhouse.mobilestoreapplication.LoginActivity;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUserProfile extends Fragment implements View.OnClickListener{

    private Button logoutButton;
    private CircleImageView profileImage;
    private TextView userFullName;
    private Spinner gender;
    private TextInputEditText age;
    private TextView locations;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private Button saveButton;

    private FusedLocationProviderClient fusedLocationClient;

    public final static String CountryCode = "+233";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        checkPermission();

        logoutButton = view.findViewById(R.id.logout);
        profileImage = view.findViewById(R.id.profile_image);
        userFullName  = view.findViewById(R.id.user_full_name);
        //gender = view.findViewById(R.id.gender_spinner);
        //age = view.findViewById(R.id.userAge);
        locations = view.findViewById(R.id.default_location);
        email = view.findViewById(R.id.user_profile_email);
        phoneNumber = view.findViewById(R.id.user_profile_phone);
        saveButton = view.findViewById(R.id.save_button);





        logoutButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(SharedPrefManager.getInstance(getContext())
                .logOut()){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(),LoginActivity.class));
            requireActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        populateView();
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) requireContext(), location -> {
                        //Get last know location.
                        if(location!=null){
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try{
                                List<Address> address = geocoder.
                                        getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                Address obj = address.get(0);
                                String locality =  obj.getFeatureName();
                                String adminArea = obj.getAdminArea();
                                String country = obj.getCountryCode();
                                SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
                                String knownLocation = locality +
                                        "\n" + adminArea + "\n" + country;
                                prefManager.setLocation(knownLocation);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }else{
            //you can directly ask for the permission.
            //the registered activityResultCallback gets the results of this request
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    //Permission is granted. Continue the action or workflow
                    checkPermission();
                }else{
                    //Explain to the user that the feature is unavailable because the
                    //feature requires the permission.

                }
            });

    private void populateView(){
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        userFullName.setText(prefManager.getUserFullName());
        email.setText(prefManager.getUserEmail());
        String phoneNumberGhana = CountryCode + prefManager.getPhoneNumber();
        phoneNumber.setText(phoneNumberGhana);
        locations.setText(prefManager.getLocation());
    }

}
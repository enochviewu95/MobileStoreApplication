package com.knowhouse.mobilestoreapplication.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.knowhouse.mobilestoreapplication.Interfaces.DialogResponseInterface;
import com.knowhouse.mobilestoreapplication.LoginActivity;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUserProfile extends Fragment {

    private Button logoutButton;
    private CircleImageView profileImage;
    private TextView userFullName;
    private Spinner gender;
    private TextInputEditText age;
    private TextView location;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private Button saveButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);

        logoutButton = view.findViewById(R.id.logout);
        profileImage = view.findViewById(R.id.profile_image);
        userFullName  = view.findViewById(R.id.user_full_name);
        gender = view.findViewById(R.id.gender_spinner);
        age = view.findViewById(R.id.userAge);
        location = view.findViewById(R.id.default_location);
        email = view.findViewById(R.id.user_profile_email);
        phoneNumber = view.findViewById(R.id.user_profile_phone);
        saveButton = view.findViewById(R.id.save_button);

        checkPermission();
        populateView();

        logoutButton.setOnClickListener(v -> {

            if(SharedPrefManager.getInstance(getContext())
            .logOut()){

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),LoginActivity.class));
                requireActivity().finish();
            }
        });
        return view;
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){

            //performAction();
        }else if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            showInContextUI();
        }else{
            //you can directly ask for the permission.
            //the registered activityResultCallback gets the results of this request
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void showInContextUI(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        PromptUserFragment promptUserFragment = new PromptUserFragment();
        promptUserFragment.show(fragmentManager,"dialog");

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    //Permission is granted. Continue the action or workflow
                }else{
                    //Explain to the user that the feature is unavailable because the
                    //feature requires the permission.
                }
            });

    private void populateView(){
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        userFullName.setText(prefManager.getUserFullName());
        email.setText(prefManager.getUserEmail());
        phoneNumber.setText(prefManager.getPhoneNumber());
    }
}
package com.knowhouse.mobilestoreapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class FragmentUserProfile extends Fragment{

    private static final int IMG_REQUEST = 1 ;
    private CircleImageView profileImage;
    private TextView userFullName;
    private TextView locations;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText birthDate;

    private Button logoutButton;
    private Button saveButton;

    private Bitmap bitmap;

    private ImageButton profileEditButton;


    private Calendar myCalendar;


    private FusedLocationProviderClient fusedLocationClient;

    public final static String CountryCode = "+233";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        checkPermission();

        //TextViews
        profileImage = view.findViewById(R.id.profile_image);
        userFullName  = view.findViewById(R.id.user_full_name);
        locations = view.findViewById(R.id.default_location);

        email = view.findViewById(R.id.user_profile_email);
        phoneNumber = view.findViewById(R.id.user_profile_phone);
        birthDate = view.findViewById(R.id.userAge);

        //Buttons
        logoutButton = view.findViewById(R.id.logout);
        saveButton = view.findViewById(R.id.save_button);

        profileEditButton = view.findViewById(R.id.profile_image_edit_button);


        myCalendar = Calendar.getInstance();


        //Button OnClickListener
        saveButton.setOnClickListener(saveButtonClicked);
        logoutButton.setOnClickListener(logoutButtonClicked);

        //Image Button OnClickListener
        email.setOnClickListener(emailEditButtonClicked);
        phoneNumber.setOnClickListener(phoneNoEditButtonClicked);
        birthDate.setOnClickListener(birthdayEditButtonClicked);
        profileEditButton.setOnClickListener(profileEditButtonClicked);
        return view;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };


    private View.OnClickListener logoutButtonClicked = v -> {
        if(SharedPrefManager.getInstance(getContext())
                .logOut()){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(),LoginActivity.class));
            requireActivity().finish();
        }
    };

    private View.OnClickListener profileEditButtonClicked = v ->{

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    };//Select image for the user profile image

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(requireActivity().getContentResolver(),path);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private View.OnClickListener emailEditButtonClicked = v -> {
        if(!email.isFocusable()){
            if(email.requestFocus()){
                InputMethodManager imm = (InputMethodManager)
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };

    private View.OnClickListener phoneNoEditButtonClicked = v -> {
        if(phoneNumber.isFocusable()){
            if(phoneNumber.requestFocus()){
                InputMethodManager imm = (InputMethodManager)
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(phoneNumber, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };

    private View.OnClickListener birthdayEditButtonClicked = v -> {
        new DatePickerDialog(requireContext(),date,myCalendar
                    .get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    };

    private View.OnClickListener saveButtonClicked = v -> {

    };



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

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.getDefault());
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void populateView(){
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        userFullName.setText(prefManager.getUserFullName());
        email.setText(prefManager.getUserEmail());
        String phoneNumberGhana = CountryCode + prefManager.getPhoneNumber();
        phoneNumber.setText(phoneNumberGhana);
        locations.setText(prefManager.getLocation());
    }

}
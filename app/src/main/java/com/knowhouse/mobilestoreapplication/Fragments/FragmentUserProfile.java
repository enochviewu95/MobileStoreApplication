package com.knowhouse.mobilestoreapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.knowhouse.mobilestoreapplication.LoginActivity;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;
import com.knowhouse.mobilestoreapplication.VolleyRequests.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
    private ProgressDialog progressDialog;
    private Button logoutButton;
    private Button saveButton;
    private ImageButton profileEditButton;

    private Bitmap bitmap;
    private Calendar myCalendar;
    private FusedLocationProviderClient fusedLocationClient;

    public static final String CountryCode = "+233";
    private static final int REQUEST_PERMISSIONS = 100;

    private String userId;
    private String filePath;
    private String locality;
    private String adminArea;
    private String country;


    /**
     * onCreate method
     * @param inflater responsible for inflating the view
     * @param container     Container for the view group
     * @param savedInstanceState  bundle for saving state
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        checkLocationPermission();

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

        //Set OnClickListeners
        email.setOnClickListener(emailEditButtonClicked);
        phoneNumber.setOnClickListener(phoneNoEditButtonClicked);
        birthDate.setOnClickListener(birthdayEditButtonClicked);
        profileEditButton.setOnClickListener(profileEditButtonClicked);

        populateView();
        return view;
    }

    /**
     * Date picker dialog anonymous class
     */
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };


    /**
     * logout button onclicked anonymous class
     */
    private View.OnClickListener logoutButtonClicked = v -> {
        if(SharedPrefManager.getInstance(getContext())
                .logOut()){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(),LoginActivity.class));
            requireActivity().finish();
        }
    };

    /**
     * profileEditButtonOnCliked anonymous class
     */
    private View.OnClickListener profileEditButtonClicked = v ->{

        if((ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)&&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if((ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))&&
                    (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE))){

            }else{
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
            }

        }else{
            showFileChooser();
        }

    };


    /**
     * emailEditButtonClicked anonymous class
     */
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


    /**
     * phoneNoEditButtonClicked anonymous class
     */
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


    /**
     * birthdayEditButton anonymous class
     */
    private View.OnClickListener birthdayEditButtonClicked = v ->
            new DatePickerDialog(requireContext(),date,myCalendar
            .get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    /**
     * savedButtonClicked anonymous class
     */
    private View.OnClickListener saveButtonClicked = v -> {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving ...");
        progressDialog.show();
        String locationValue = null;
        if(bitmap != null){
            uploadBitmap(bitmap);
        }

        if(!locations.getText().toString().equals("")){
            locationValue = locations.getText().toString();
        }else{
            locationValue = "null";
        }

        uploadUserTextDetails(locationValue);
    };

    /**
     * Method to upload the user details in the text box excluding image
     * @param locationValue is the value of the gps location
     */
    private void uploadUserTextDetails(String locationValue) {

        String url = ConstantURL.SITE_URL +"UpdateProfile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        if(!obj.getBoolean("error")){
                            Toast.makeText(getContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            if(SharedPrefManager.getInstance(getContext()).logOut()){
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                requireActivity().finish();
                                progressDialog.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams(){

                String uneditedPhoneNo = Objects.requireNonNull(phoneNumber.getText()).toString();
                String filteredNo = uneditedPhoneNo.replace("+233","");
                Map<String, String> params = new HashMap<>();
                params.put("uid",userId);
                params.put("email", Objects.requireNonNull(email.getText()).toString());
                params.put("phoneNo", filteredNo);
                params.put("birthday", Objects.requireNonNull(birthDate.getText()).toString());
                params.put("location", locationValue);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    /**
     * Method to show the file chooser dialog for selecting picture
     */
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),IMG_REQUEST);
    }

    /**
     * Callback method for retrieving the result of the file chooser activity
     * @param requestCode for successful creation of chooser
     * @param resultCode   for successful results
     * @param data  nullable
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            filePath = getPath(path);
            if(filePath != null){
                try {
                    bitmap = MediaStore.Images.Media.
                            getBitmap(requireActivity().getContentResolver(),path);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getContext(),"No Image Was Selected",Toast.LENGTH_LONG).show();
            }

        }
    }


    /**
     * Save the user details into the shared preferences
     * before destroying view
     */
    @Override
    public void onDestroyView() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        String knownLocation = locality + "\n" + adminArea + "\n" + country;
        prefManager.setLocation(knownLocation);
        super.onDestroyView();
    }

    /**
     * Method to retrieve path of image
     * @param uri the uri path of the image
     * @return path of image in string
     */
    private String getPath(Uri uri){
        String path = "";
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = requireContext().getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if(cursor !=null){
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }

        }
        return path;
    }


    /**
     * method to check permission allowed for location
     */
    private void checkLocationPermission(){
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
                                locality =  obj.getFeatureName();
                                adminArea = obj.getAdminArea();
                                country = obj.getCountryCode();
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

    /**
     * Request permission launcher anonymous class
     */
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    //Permission is granted. Continue the action or workflow
                    checkLocationPermission();
                }
            });

    /**
     * Format for the date in the birthday edit box
     */
    private void updateLabel(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.getDefault());
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * method to populate the widget in the fragment view
     */
    private void populateView(){

        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        String myProfileImage = prefManager.getCustomerImageUrl();
        String myLocation = prefManager.getLocation();
        String prefUserIdNo = prefManager.getUserID();
        String prefUserFullName = prefManager.getUserFullName();
        String prefUserPHoneNumber = prefManager.getPhoneNumber();
        String prefUserBirthDate = prefManager.getDataOfBirth();
        String prefUserEmail = prefManager.getUserEmail();
        String prefUserImage = prefManager.getCustomerImageUrl();

        userId = prefUserIdNo;
        userFullName.setText(prefUserFullName);
        email.setText(prefUserEmail);
        String phoneNumberGhana = CountryCode + prefUserPHoneNumber;
        phoneNumber.setText(phoneNumberGhana);
        birthDate.setText(prefUserBirthDate);

        if(!myProfileImage.equals("null")) {
            //profileImage.setImageBitmap(stringToImage(myProfileImage));
            Glide.with(requireContext()).load(prefUserImage).into(profileImage);
        }
        if(myLocation != null){
            locations.setText(prefManager.getLocation());
        }

    }

    /**
     * Method to convert image to string
     * @param bitmap is the chosen image
     * @return byte array
     */
    private byte[] getFileDataFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
        //byte[] imgBytes = byteArrayOutputStream.toByteArray();
        //return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }


    /**
     * Method to upload bitmap to database on server
     * @param bitmap holds the image
     */
    private void uploadBitmap(final Bitmap bitmap){
        String url = ConstantURL.SITE_URL +"UploadUserImage.php";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                response -> {
                    JSONObject obj = null;
                    try {
                        //String responses = new String(response.data);
                        obj = new JSONObject(new String(response.data));
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("GotError",""+error.getMessage());
                }){
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                int imageName = Integer.parseInt(SharedPrefManager.getInstance(requireContext()).getUserID());
                long time = System.currentTimeMillis();
                params.put("image", new DataPart(imageName+"-"+time + ".jpg", getFileDataFromBitmap(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        MySingleton.getInstance(getContext()).addToRequestQueue(volleyMultipartRequest);
    }

    /**
     * Method to convert the string into image
     * @param imageString is the image in string format
     * @return Bitmap.
     */
    private Bitmap stringToImage(String imageString){
        byte[] imageBytes = Base64.decode(imageString,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    }

}
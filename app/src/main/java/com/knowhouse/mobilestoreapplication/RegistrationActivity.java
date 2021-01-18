package com.knowhouse.mobilestoreapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaration of variable to hold references to widgets
    private Button signUpButton;
    private TextInputEditText fullName;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputEditText birthDate;

    private ProgressDialog progressDialog;

    private final String TAG = "RegistrationActivity";

    //Declaration of firebase instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get references to the widget used
        fullName = findViewById(R.id.full_name_edit_text);
        email = findViewById(R.id.email_edit_text);
        phoneNumber = findViewById(R.id.number_edit_text);
        birthDate = findViewById(R.id.date_edit_text);
        password = findViewById(R.id.password_edit_text);
        confirmPassword = findViewById(R.id.confirm_password_edit_text);
        signUpButton = findViewById(R.id.signup_button);

        progressDialog = new ProgressDialog(this);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(this);  //Set onClickListener
    }


    /**
     * OnClick method to be called when the signUpButton is called
     * @param v holds the reference to the widget that issued the onClick listener
     */
    @Override
    public void onClick(View v) {


        if(fullName.getText() != null && email.getText() != null
                && phoneNumber.getText() != null && password.getText() != null
                && confirmPassword.getText()!=null && birthDate.getText() != null){        //test for an empty field
                String mFullName = fullName.getText().toString().trim();    //Get text for full name
                String mEmail = email.getText().toString().trim();  //get text for email and convert to string
                String mPassword = password.getText().toString().trim();    //get text for password and convert to string
                String mConfirmPassword = confirmPassword.getText().toString().trim();
                String mPhoneNumber = phoneNumber.getText().toString().trim();  //get text for phone number
                String mBirthDate = birthDate.getText().toString().trim();

                if(!mEmail.isEmpty() && !mPassword.isEmpty()){
                    if(mPassword.equals(mConfirmPassword)){

                        progressDialog.setMessage("Signing Up");
                        progressDialog.show();

                        mAuth.createUserWithEmailAndPassword(mEmail,mPassword)      //create a user using the email and password
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //listener for the results of user creation
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            //Sign in success,
                                            Log.d(TAG,"createUserWithEmail: success");
                                            registerUser(mFullName,mEmail,mPhoneNumber,mPassword,mBirthDate);
                                            updateUI();
                                        }else{
                                            //if sign in fails, display a message to the user
                                            progressDialog.dismiss();
                                            Log.w(TAG,"createUserWithEmail:failure",task.getException());
                                            if(task.getException() != null)
                                                Toast.makeText(RegistrationActivity.this,task.getException().getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(this,"Passwords don't match",Toast.LENGTH_LONG)
                                .show();
                    }
                }else{
                    Toast.makeText(this,"Fields are empty",Toast.LENGTH_LONG)
                            .show();
                }
        }
    }

    private void registerUser(String name, String email,String phoneNumber,
                                 String password,String birthDay){

        String url = "http://192.168.42.61/MobileStoreApp/PhpScripts/Registration.php";

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        if(!obj.getBoolean("error")){
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG)
                                    .show();
                        }else{
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("full_name",name);
                params.put("email",email);
                params.put("phone_number",phoneNumber);
                params.put("password",password);
                params.put("birthday",birthDay);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void updateUI() {

            progressDialog.dismiss();
            startActivity(new Intent(RegistrationActivity.
                    this,LoginActivity.class));
            finish();
    }
}
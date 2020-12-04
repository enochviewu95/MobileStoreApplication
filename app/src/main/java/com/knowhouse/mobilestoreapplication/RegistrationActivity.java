package com.knowhouse.mobilestoreapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaration of variable to hold references to widgets
    private Button signUpButton;
    private TextInputEditText fullName;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;

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
        password = findViewById(R.id.password_edit_text);
        confirmPassword = findViewById(R.id.confirm_password_edit_text);
        signUpButton = findViewById(R.id.signup_button);

        progressDialog = new ProgressDialog(this);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(this);  //Set onClickListener
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and updata UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * OnClick method to be called when the signUpButton is called
     * @param v holds the reference to the widget that issued the onClick listener
     */
    @Override
    public void onClick(View v) {


        if(fullName.getText() != null && email.getText() != null            //test for an empty field
        && password.getText() != null && confirmPassword.getText()!=null){
                String mEmail = email.getText().toString().trim();  //get text for email and convert to string
                String mPassword = password.getText().toString().trim();    //get text for password and convert to string
                String mConfirmPassword = confirmPassword.getText().toString().trim();

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
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        }else{
                                            //if sign in fails, display a message to the user
                                            progressDialog.dismiss();
                                            Log.w(TAG,"createUserWithEmail:failure",task.getException());
                                            Toast.makeText(RegistrationActivity.this,"Authentication failed.",
                                                    Toast.LENGTH_LONG).show();
                                            updateUI(null);
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

    /**
     * Update user interface accordingly.
     * This is going to launch the MainActivity activity
     * @param currentUser parameter holding the current user
     */
    private void updateUI(FirebaseUser currentUser) {
        //if current user is not null, launch the MainActivity activity
        if(currentUser != null){
            progressDialog.dismiss();
            startActivity(new Intent(RegistrationActivity.
                    this,MainActivity.class));
            finish();
        }
    }
}
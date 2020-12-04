package com.knowhouse.mobilestoreapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText email;
    private TextInputEditText password;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private TextView redirect;

    private FirebaseAuth mAuth; //declaration of firebase authenticator class

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.signin_button);
        redirect = findViewById(R.id.registration_redirect);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        loginButton.setOnClickListener(this);
        redirect.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser  = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(v.findViewById(R.id.signin_button))) {
            userLogin();
        }else if(v.equals(v.findViewById(R.id.registration_redirect))){
            onSignUp(v);
        }
    }

    private void userLogin(){

        if(email.getText() != null && password.getText()!=null){
            final String userEmail = email.getText().toString().trim();
            final String userPassword = password.getText().toString().trim();

            if(!userEmail.isEmpty() && !userPassword.isEmpty()){
                progressDialog.setMessage("Logging In User ...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //signin successful
                                    Log.d(TAG,"signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                }else{
                                    //if sign in fails
                                    progressDialog.dismiss();
                                    Log.w(TAG,"signInWithEmail:failure",task.getException());
                                    Toast.makeText(LoginActivity.this,"Authentication failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }else{
                Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void onSignUp(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            progressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

}
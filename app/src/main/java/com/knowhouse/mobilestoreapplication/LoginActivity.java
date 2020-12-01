package com.knowhouse.mobilestoreapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText email;
    private TextInputEditText password;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private TextView redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.signin_button);
        redirect = findViewById(R.id.registration_redirect);

        progressDialog = new ProgressDialog(this);
        loginButton.setOnClickListener(this);
        redirect.setOnClickListener(this);
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
        progressDialog.setMessage("Logging In User ...");
        progressDialog.show();

        if(email.getText() != null && password.getText()!=null){
            final String userEmail = email.getText().toString().trim();
            final String userPassword = password.getText().toString().trim();
            progressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private void onSignUp(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

}
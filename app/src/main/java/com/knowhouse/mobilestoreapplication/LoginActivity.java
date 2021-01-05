package com.knowhouse.mobilestoreapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            onSignUp();
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
                        .addOnCompleteListener(this, task -> {
                            if(task.isSuccessful()){
                                //signin successful
                                Log.d(TAG,"signInWithEmail:success");
                                login(userEmail,userPassword);
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else{
                                //if sign in fails
                                progressDialog.dismiss();
                                Log.w(TAG,"signInWithEmail:failure",task.getException());
                                Toast.makeText(LoginActivity.this,"Authentication failed",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

            }else{
                Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void onSignUp() {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            progressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private void login(String email,String password){
        String url = ConstantURL.SITE_URL +"Login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject obj = null;
                    JSONObject content = null;
                    try {
                        obj = new JSONObject(response);
                        content = new JSONObject(obj.getString("content"));

                        if(!obj.getBoolean("error")){
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .onUserLogin(content.getInt("id"),
                                                content.getInt("contact"),
                                                content.getString("full_name"),
                                                content.getString("email"));
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
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
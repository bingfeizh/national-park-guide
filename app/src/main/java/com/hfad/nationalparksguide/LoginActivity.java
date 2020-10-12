package com.hfad.nationalparksguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class LoginActivity extends NavigationActivity implements View.OnTouchListener{

    private EditText emailId, password;
    private TextView signUp;
    private Button logIn;
    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser user;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_login, null, false);

        drawerLayout.addView(contentView, 0);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        signUp = findViewById(R.id.link_signup);

        db = FirebaseFirestore.getInstance();



        signUp.setOnTouchListener(this);

        logIn = findViewById(R.id.btn_login);

        logIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                verifyAccount();
            }
        });
    }

    /**
     * On touch sign_up textview
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent event){
        if(view.getId() == R.id.link_signup){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
        return true;
    }

    /**
     * Verifies the account info in editview.
     */
    public void verifyAccount(){
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if(email.isEmpty()){
            emailId.setError("Email ID is required");
            emailId.requestFocus();
        }
        else if(pwd.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
        }
        else if(email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }
        //Fields are valid, check for firebase auth for login
        else{
            mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user = mFirebaseAuth.getCurrentUser();

                        //TODO: do something after logged in
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                    //incorrect email or password
                    else{
                        Toast.makeText(LoginActivity.this, "Login failed. Incorrect email or password.",
                                Toast.LENGTH_SHORT).show();
                        emailId.getText().clear();
                        password.getText().clear();
                    }
                }
            });
        }


    }

}

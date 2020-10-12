package com.hfad.nationalparksguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Profile page. Shown on "My Account" button touched if user logged in.
 */
public class ProfileActivity extends NavigationActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    FirebaseUser mUser;

    TextView userName;
    TextView myComments;
    Button signout_btn;

    List<ParkInfo> favorates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_profile, null, false);

        drawerLayout.addView(contentView, 0);

        userName = findViewById(R.id.profileName);
        myComments = findViewById(R.id.myComments);
        signout_btn = findViewById(R.id.signout_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        getCurrentUser();
        addOnClickListeners();
    }

    public void addOnClickListeners(){
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    /**
     * Populate profile fields with current user information
     */
    public void getCurrentUser(){
        mUser = firebaseAuth.getCurrentUser();
        // Should never be null here
        if(mUser != null){
            String userEmail = mUser.getEmail();
            userName.setText(userEmail);

        }
        else{
            Toast.makeText(this, "Sign in status error, please log in again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {

    }
}

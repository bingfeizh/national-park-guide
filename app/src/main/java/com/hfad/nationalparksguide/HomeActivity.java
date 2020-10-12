package com.hfad.nationalparksguide;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;

import java.util.ArrayList;

public class HomeActivity extends NavigationActivity {

    private ImageButton exploreBtn;
    private ImageButton favoritesBtn;
    private ImageButton mapBtn;
    private ImageButton accountBtn;
    AppDatabase db;


    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        exploreBtn = (ImageButton) findViewById(R.id.explore_btn);
        View v1 = findViewById(R.id.explore_btn);
        v1.getBackground().setAlpha(150);
        favoritesBtn = (ImageButton) findViewById(R.id.favorites_btn);
        View v2 = findViewById(R.id.favorites_btn);
        v2.getBackground().setAlpha(150);
        mapBtn = (ImageButton) findViewById(R.id.map_btn);
        View v3 = findViewById(R.id.map_btn);
        v3.getBackground().setAlpha(150);
        accountBtn = (ImageButton) findViewById(R.id.my_account_btn);
        View v4 = findViewById(R.id.my_account_btn);
        v4.getBackground().setAlpha(150);
        addButtonListener();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
    }


    public void addButtonListener() {
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.ParkInfoDao().findAll().size() != 0) {
                    startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Downloading data...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = getUser();
                if(user==null){
                    Toast.makeText(HomeActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                }
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = getUser();
                if(user==null){
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                else{
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                }

            }
        });
    }

    public FirebaseUser getUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }


}

package com.hfad.nationalparksguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.nationalparksguide.BroadcastReceiver.NetworkStateReceiver;
import com.hfad.nationalparksguide.Controller.ParkApiService;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.Service.NationalParksApiService;
import com.hfad.nationalparksguide.data.model.Park;
import com.hfad.nationalparksguide.data.model.ParkList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 2000;

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "https://developer.nps.gov/api/v1/";
    static Retrofit retrofit = null;
    final static String API_KEY = "qr1sHl7kgHxN64GPvOjY0a3x9xm0ysphzA9khDRg";
    //ArrayList<String> fields = new ArrayList<>();

    AppDatabase db;
    NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, com.hfad.nationalparksguide.Service.NationalParksApiService.class);
//        startService(intent);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        if (networkStateReceiver == null) {
            networkStateReceiver = new NetworkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);

        if (networkStateReceiver.isConnected()) {

        }
        Intent intent = new Intent(getApplicationContext(), NationalParksApiService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }

}

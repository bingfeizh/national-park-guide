package com.hfad.nationalparksguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.hfad.nationalparksguide.Controller.Adapter.EventApiAdapter;
import com.hfad.nationalparksguide.Controller.EventApiService;
import com.hfad.nationalparksguide.data.model.Event;
import com.hfad.nationalparksguide.data.model.EventList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventActivity extends NavigationActivity {

    private RecyclerView recyclerView;

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "https://developer.nps.gov/api/v1/";
    static Retrofit retrofit = null;
    final static String API_KEY = "qr1sHl7kgHxN64GPvOjY0a3x9xm0ysphzA9khDRg";

    String parkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        parkCode = intent.getStringExtra("parkCode");
        connect(parkCode);
        setContentView(R.layout.activity_event);
    }

    private void connect(String parkCode) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        EventApiService EventApiService = retrofit.create(EventApiService.class);
        Call<EventList> call = EventApiService.getList(API_KEY, parkCode);
        call.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, Response<EventList> response) {
                ArrayList<Event> events = response.body().getEventList();

                if (events.size() != 0) {
                    recyclerView = findViewById(R.id.rvEventList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));
                    recyclerView.setAdapter(new EventApiAdapter(events));
                }

                else {
                    Toast.makeText(getApplicationContext(), "No Events Available",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            finish();
                        }
                    }, 2000);
                }

            }
            @Override
            public void onFailure(Call<EventList> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}


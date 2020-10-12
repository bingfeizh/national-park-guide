package com.hfad.nationalparksguide.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.hfad.nationalparksguide.Controller.ParkApiService;
import com.hfad.nationalparksguide.MainActivity;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.data.model.Park;
import com.hfad.nationalparksguide.data.model.ParkList;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NationalParksApiService extends IntentService {

    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "https://developer.nps.gov/api/v1/";
    static Retrofit retrofit = null;
    final static String API_KEY = "qr1sHl7kgHxN64GPvOjY0a3x9xm0ysphzA9khDRg";

    AppDatabase db;

    public NationalParksApiService() {
        super("NationalParksApiService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext(), "Downloading data...", Toast.LENGTH_LONG).show();
            }
        }, 2000);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        com.hfad.nationalparksguide.Controller.ParkApiService ParkApiService = retrofit.create(ParkApiService.class);
        Call<ParkList> call = ParkApiService.getList(API_KEY, 90, "National Park", "image");

        call.enqueue(new Callback<ParkList>() {
            @Override
            public void onResponse(Call<ParkList> call, Response<ParkList> response) {
                if (response.body() != null) {
                    ArrayList<Park> parks = response.body().getParkList();
                    for (Park p : parks) {
                        if (p.getDesignation().equals("National Park")) {
                            db.ParkInfoDao().insert(new ParkInfo(p.getId(), p.getName(), p.getDescription(), p.getImageURL(), p.getURL(), p.getParkCode()));
                            Log.println(Log.INFO, "name", p.getName());
                        }

                    }
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Data updated!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ParkList> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Failed to update data.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        stopSelf();
    }

}

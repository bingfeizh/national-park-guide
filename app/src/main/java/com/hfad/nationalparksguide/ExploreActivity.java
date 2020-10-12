package com.hfad.nationalparksguide;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.hfad.nationalparksguide.Controller.Adapter.ParkListAdapter;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Populates the activity with recyclerView containing park information.
 */
public class ExploreActivity extends NavigationActivity implements NavigationView.OnNavigationItemSelectedListener, ParkListAdapter.OnParkClickListenr {

    private RecyclerView recyclerView;
    View contentView;

    AppDatabase db;

    List<ParkInfo> parks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();


        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentView = inflater.inflate(R.layout.activity_explore, null, false);

        setView(contentView);

        drawerLayout.addView(contentView, 0);
    }

    private void setView(View contentView) {
        parks = db.ParkInfoDao().findAll();

        recyclerView = contentView.findViewById(R.id.rvParkList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExploreActivity.this));
        recyclerView.setAdapter(new com.hfad.nationalparksguide.Controller.Adapter.ParkListAdapter(parks, this));
    }

    @Override
    public void onParkClick(int position) {
        Intent intent = new Intent(this, ParkInfoActivity.class);

        intent.putExtra("selected park", parks.get(position).parkName);

        startActivity(intent);
    }
}


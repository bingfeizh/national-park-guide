package com.hfad.nationalparksguide;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hfad.nationalparksguide.Controller.Adapter.ParkListAdapter;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.data.model.FirebaseHandler;
import com.hfad.nationalparksguide.data.model.Park;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FavoritesActivity extends NavigationActivity implements ParkListAdapter.OnParkClickListenr {

    private RecyclerView recyclerView;
    private static final String TAG = "FavoritesActivity";

    View contentView;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    List<ParkInfo> parks;


    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        user = getUser();

        contentView = inflater.inflate(R.layout.activity_favorites, null, false);

        getFavoriteParks();

        drawerLayout.addView(contentView, 0);

    }

    /**
     * Listen to updates on Firebase for newly added favorite parks, populates the Map<String, String> favoriteParks
     *
     */
    public void getFavoriteParks(){
        String userEmail = user.getEmail();
        int charLoc = userEmail.indexOf("@");
        String userName = user.getEmail().substring(0,charLoc);

        databaseReference = FirebaseHandler.getDbReference("Users/"+userName+"/Favorites");



        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> favoriteParks = dataSnapshot.getValue(genericTypeIndicator);
                getFavParkNameFromDb(favoriteParks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(FavoritesActivity.this, "Error loading favorite list", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * With map of parknames, populate a list.
     * @return
     */
    public void getFavParkNameFromDb(Map<String, String> favoriteParks){

        List<String> parkNames = new ArrayList<>();

        if(favoriteParks != null && favoriteParks.size()!=0){
            for(String val : favoriteParks.values()){
                parkNames.add(val);
                System.out.println(val);
            }

            retrieveAndDisplayParkInfo(parkNames);
        }
    }

    /**
     * For the list of park names, get their info from local database.
     * @param parkNames
     * @return
     */
    public void retrieveAndDisplayParkInfo(List<String> parkNames){
        parks = new ArrayList<>();
        for(String park : parkNames){
            parks.add(db.ParkInfoDao().findByName(park));

        }
        System.out.println(parks.size() + "alkfjdlakjsdflakjsdlfkajf");
        populateView(contentView, parks);
    }

    private void populateView(View contentView, List<ParkInfo> parks) {

        recyclerView = contentView.findViewById(R.id.rvParkList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoritesActivity.this));
        recyclerView.setAdapter(new com.hfad.nationalparksguide.Controller.Adapter.ParkListAdapter(parks, this));
    }


    @Override
    public void onParkClick(int position) {
        Intent intent = new Intent(this, ParkInfoActivity.class);

        intent.putExtra("selected park", parks.get(position).parkName);

        startActivity(intent);
    }

    public FirebaseUser getUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }


}

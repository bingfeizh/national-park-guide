package com.hfad.nationalparksguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.nationalparksguide.Room.AppDatabase;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.data.model.FavoritePark;
import com.hfad.nationalparksguide.data.model.FirebaseHandler;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkInfoActivity extends NavigationActivity {

    ParkInfo info;
    String parkName;
    String parkDescription;
    String parkImage;
    String selectedPark;
    String parkURL;
    String parkCode;
    FloatingActionButton addFavBtn;
    List<ParkInfo> parkInfoList;

    FirebaseUser user;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_park_info, null, false);

        drawerLayout.addView(contentView, 0);

        selectedPark = getIntent().getStringExtra("selected park");

        addFavBtn = findViewById(R.id.add_favorite_btn);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        info = db.ParkInfoDao().findByName(selectedPark);

        parkName = info.parkName;
        parkDescription = info.parkDescription;
        parkImage = info.parkImage;
        parkURL = info.url;
        parkCode = info.parkCode;


        setTitle(parkName);
        ImageView imageView = findViewById(R.id.parkImage);
        Picasso.get().load(parkImage).into(imageView);
        TextView nameView = findViewById(R.id.ParkName);
        nameView.setText(parkName);
        TextView descriptionView = findViewById(R.id.ParkDescription);
        descriptionView.setText(parkDescription);

        TextView URLView = findViewById(R.id.ParkURL);
        URLView.setText(parkURL);

        final Button btnProgramList = findViewById(R.id.event);
        btnProgramList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("parkCode", parkCode);
                startActivity(intent);
            }
        });

        setFavBtnOnClickAcition();
    }

    /**
     * Add the current park to favorite list. Store under User field in fb-db.
     */
    public void setFavBtnOnClickAcition(){
        addFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();

                if(user==null){
                    Toast.makeText(ParkInfoActivity.this, "To do so, please login", Toast.LENGTH_SHORT).show();
                }
                else{
                    //get user name
                    String userEmail = user.getEmail();
                    int charLoc = user.getEmail().indexOf("@");
                    String userName = userEmail.substring(0,charLoc);

                    // Set up a child for fav (if not exist)
                    databaseReference = FirebaseHandler.getDbReference("Users/"+userName);
                    DatabaseReference favChildRef = databaseReference.child("Favorites");

                    Map<String, Object> map = new HashMap<>();
                    map.put(parkName,parkName);

                    favChildRef.updateChildren(map);

                    Toast.makeText(ParkInfoActivity.this, "Added to favorite!", Toast.LENGTH_SHORT).show();
                }

;            }
        });
    }

}

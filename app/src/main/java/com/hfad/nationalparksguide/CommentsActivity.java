package com.hfad.nationalparksguide;

import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hfad.nationalparksguide.Controller.Adapter.CommentListAdapter;
import com.hfad.nationalparksguide.data.model.Comment;
import com.hfad.nationalparksguide.data.model.FirebaseHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    static final String TAG = MainActivity.class.getSimpleName();
    List<Comment> commentList;
    private double userLongitude;
    private double userLatitude;
    private DatabaseReference commentDbReference;
    private GeoFire geoFire;
    private Button showCommentsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        userLatitude = getIntent().getDoubleExtra("userLatitude", 0);

        commentDbReference = FirebaseHandler.getDbReference("Comments");

        commentList = new ArrayList<>();

        queryComments();

        showCommentsBtn = findViewById(R.id.show_comments_btn);
    }

    private void queryComments(){

        commentList.clear();

        geoFire = new GeoFire(commentDbReference);

        GeoLocation userLocation = new GeoLocation(userLatitude, userLongitude);
        GeoQuery geoQuery = geoFire.queryAtLocation(userLocation, 5000);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                String[] commentString = key.split("::");
                Comment comment = new Comment(commentString[1], commentString[0]);
                commentList.add(comment);
                connect();
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void connect() {

        recyclerView = findViewById(R.id.rvCommentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
        recyclerView.setAdapter(new CommentListAdapter(commentList));
    }
}


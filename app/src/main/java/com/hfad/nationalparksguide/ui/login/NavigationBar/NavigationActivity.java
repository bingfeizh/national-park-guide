package com.hfad.nationalparksguide.ui.login.NavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.nationalparksguide.FavoritesActivity;
import com.hfad.nationalparksguide.LoginActivity;
import com.hfad.nationalparksguide.ExploreActivity;
import com.hfad.nationalparksguide.HomeActivity;
import com.hfad.nationalparksguide.MapsActivity;
import com.hfad.nationalparksguide.ProfileActivity;
import com.hfad.nationalparksguide.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Class dedicated for implementing the navigation drawer. May switch to using fragments in the future.
 */

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationdrawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // drawer menu settings
        drawerLayout = findViewById(R.id.navigationDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        FirebaseUser user;

        switch(menuItem.getItemId()){
            case R.id.home:
                intent = new Intent(this, HomeActivity.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                finish();
                break;

            /**
             * Only switch to favorite page if logged in
             */
            case R.id.favorites:
                user = getUser();
                if(user==null){
                    Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
                }
                else{
                    intent = new Intent(this, FavoritesActivity.class);
                    drawerLayout.closeDrawers();
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.explore:
                intent = new Intent(this, ExploreActivity.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                finish();
                break;

            /**
             * go to login page if not logged in, else go to profile page.
             */
            case R.id.account:
                user = getUser();
                if(user==null){
                    startActivity(new Intent(this, LoginActivity.class));
                }
                else{
                    startActivity(new Intent(this, ProfileActivity.class));
                }
                drawerLayout.closeDrawers();
                finish();
                break;

            case R.id.map:
                intent = new Intent(this, MapsActivity.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                finish();
                break;
        }

        return false;
    }

    public FirebaseUser getUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }
}

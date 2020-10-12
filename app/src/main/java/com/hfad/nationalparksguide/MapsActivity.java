package com.hfad.nationalparksguide;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.nationalparksguide.data.model.Comment;
import com.hfad.nationalparksguide.data.model.FirebaseHandler;
import com.hfad.nationalparksguide.ui.login.NavigationBar.NavigationActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MapsActivity extends NavigationActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLng;

    private Button myLocationBtn;
    private EditText userComment;
    private Button sendCommentBtn;
    private Button showCommentsBtn;

    private MarkerOptions userMarkerOption;

    private List<Comment> comments;

    FirebaseAuth firebaseAuth;

    // current user's database
    DatabaseReference commentDbReference;

    // all users' database
    DatabaseReference databaseReference;

    GeoFire geoFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_maps, null, false);

        drawerLayout.addView(contentView, 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        myLocationBtn = contentView.findViewById(R.id.my_location_btn);
        sendCommentBtn = contentView.findViewById(R.id.send_comment);
        showCommentsBtn = contentView.findViewById(R.id.show_comments_btn);
        userComment = contentView.findViewById(R.id.write_comment);
        addButtonListener();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseHandler.getDbReference();
        commentDbReference = FirebaseHandler.getDbReference("Comments");

    }

    private void addButtonListener() {
        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLastLocation();

            }
        });

        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLastLocation();
                String comment = userComment.getText().toString();
                userComment.setText("");

                uploadCommentToFirebase(comment);
            }
        });

        showCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtra("userLatitude", userLatLng.latitude);
                intent.putExtra("userLongitude", userLatLng.longitude);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchLastLocation();
    }

    private void askLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                0, 0, locationListener);

                        //get user last location to set default location marker in the map
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        mMap.clear();
                        userMarkerOption = new MarkerOptions().position(userLatLng).title("I am here!")
                                .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.user_location));
                        mMap.addMarker(userMarkerOption);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

                        locationManager.removeUpdates(locationListener);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void fetchLastLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //store user latlng
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                userMarkerOption = new MarkerOptions().position(userLatLng).title("I am here!")
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.user_location));
                mMap.addMarker(userMarkerOption);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        // asking for map permission with user / location permission
        askLocationPermission();

        locationManager.removeUpdates(locationListener);
        queryCommentsToMap();
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,
                vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    /**
     * Uploads comment to firebase given that user is currently logged in.
     * If not logged in, a Toast will be shown.
     * Else comment will be uploaded
     *
     * Comments are uploaded along with location information
     * @param comment
     */
    public void uploadCommentToFirebase(String comment){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //User not signed in
        if(currentUser==null){
            Toast.makeText(this, "Please log in to make comments", Toast.LENGTH_SHORT).show();
        }
        //User signed in
        else if (comment == null || comment.length() < 1){
            Toast.makeText(this, "Empty Comment!", Toast.LENGTH_SHORT).show();
        }

        else{

            // put under Users
            String userEmail = currentUser.getEmail();
            int charLoc = userEmail.indexOf("@");
            String userName = userEmail.substring(0,charLoc);
            databaseReference = FirebaseHandler.getDbReference("Users/"+userName).child("Comments");
            geoFire = new GeoFire(databaseReference);

            geoFire.setLocation(comment, new GeoLocation(userLatLng.latitude,userLatLng.longitude));


            // put under Comments

            DatabaseReference commentDbReference = FirebaseHandler.getDbReference("Comments/");
            geoFire = new GeoFire(commentDbReference);
            geoFire.setLocation(userName+ "::" + comment, new GeoLocation(userLatLng.latitude,userLatLng.longitude));


            mMap.clear();
            userMarkerOption = userMarkerOption.title(comment);
            mMap.addMarker(userMarkerOption);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

        }
    }

    private void queryCommentsToMap(){

        geoFire = new GeoFire(commentDbReference);

        GeoLocation userLocation = new GeoLocation(userLatLng.latitude, userLatLng.longitude);
        GeoQuery geoQuery = geoFire.queryAtLocation(userLocation, 5000);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                String markerTitle = key.replace("::", ": ");
                MarkerOptions commentMarker = new MarkerOptions().position(userLatLng).title(markerTitle);
                mMap.addMarker(commentMarker);
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

}

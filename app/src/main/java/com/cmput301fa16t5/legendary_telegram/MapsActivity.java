package com.cmput301fa16t5.legendary_telegram;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapsActivity class is the view of the map.
 * @author zhimao
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    // This is the actual map object
    private GoogleMap mMap;
    private LatLng start;
    private LatLng end;
    private Marker startMarker;
    private Marker endMarker;



    // View has the controller
    private MapController mapcontroller = new MapController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Code from: http://stackoverflow.com/questions/17412882/positioning-google-maps-v2-zoom-in-controls-in-android
        // Show the zoom button on the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        start = new LatLng(53.522945, -113.525594);
        end = new LatLng(53.525037, -113.521324);
        // Learn from: https://developers.google.com/maps/documentation/android-api/marker
        // https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker
        startMarker = mMap.addMarker(new MarkerOptions().position(start).draggable(true));
        endMarker = mMap.addMarker(new MarkerOptions().position(end).draggable(true));


        /* Sample code
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        // Learn from: http://stackoverflow.com/questions/30067228/onmarkerdraglistener-for-marker-position-along-associated-circles-google-maps-an
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (marker.getTitle().equals("Start")) {
                    Context context = getApplicationContext();
                    start = marker.getPosition();
                    Toast.makeText(context,"S: "+start.toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"E: "+end.toString(),Toast.LENGTH_SHORT).show();
                } else if (marker.getTitle().equals("End")) {
                    Context context = getApplicationContext();
                    end = marker.getPosition();
                    Toast.makeText(context,"S: "+start.toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"E: "+end.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }









}

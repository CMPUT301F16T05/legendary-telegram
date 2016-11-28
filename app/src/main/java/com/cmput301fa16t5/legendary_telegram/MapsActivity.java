package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * MapsActivity class is the view of the map.
 * It is used to show and get the geometric information of rider and driver.
 * @author zhimao
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Actual map onject
    private GoogleMap mMap;

    //Data class for map
    private MapData mapData;

    //Markers of start and end points
    private Marker startMarker;
    private Marker endMarker;

    // Buttons and Text boxes
    private Button okButton;
    private Button searchButton;
    private Button filterButton;
    private EditText startEditText;
    private EditText endEditText;
    private TextView endTextView;

    private ArrayList<LatLng> positionPair;
    private String riderOrDriver;

    // View has the controller
    private MapController myController;

    private Boolean backFromFilter;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        okButton = (Button) findViewById(R.id.OkButton);
        searchButton = (Button) findViewById(R.id.SearchButton);
        filterButton = (Button) findViewById(R.id.FilterButton);
        startEditText = (EditText) findViewById(R.id.StartEditText);
        endEditText = (EditText) findViewById(R.id.EndEditText);
        endTextView = (TextView) findViewById(R.id.EndTextView);

        mapData = new MapData();

        Intent intent = getIntent();
        myController = new MapController();
        riderOrDriver = intent.getStringExtra("Map");
        backFromFilter = false;
        description = "";
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
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

        mapData.setStart(new LatLng(53.522945, -113.525594));
        mapData.setEnd(new LatLng(53.525037, -113.521324));

        /**
         * Null in case of tests since there is nothing.
         * Not fromRider means Driver.
         */
        if ((riderOrDriver == null) || (!riderOrDriver.equals("fromRider"))) {
            startMarker = mMap.addMarker(new MarkerOptions().position(mapData.getStart()).draggable(true));
            startMarker.setTitle("Start");

            endEditText.setEnabled(false);
            endEditText.setVisibility(View.INVISIBLE);
            endTextView.setEnabled(false);
            endTextView.setVisibility(View.INVISIBLE);


        }

        /**
         * "fromRider" aka two points.
         */
        else {
            startMarker = mMap.addMarker(new MarkerOptions().position(mapData.getStart()).draggable(true));
            endMarker = mMap.addMarker(new MarkerOptions().position(mapData.getEnd()).draggable(true));

            // Add a indicator for the marker
            // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
            startMarker.setTitle("Start");
            endMarker.setTitle("End");

            filterButton.setText("Description");
        }

        //zoom to start position:
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mapData.getStart()).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Long click marker and drag to get accurate location
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
                // If it is Driver
                if ((riderOrDriver == null) || (!riderOrDriver.equals("fromRider"))) {
                    if (marker.getTitle().equals("Start") || marker.getTitle().equals(mapData.getStartAddress())) {
                        Context context = getApplicationContext();
                        mapData.setStart(marker.getPosition());
                        Toast.makeText(context,"S: " + mapData.getStart().toString(),Toast.LENGTH_SHORT).show();
                    }

                    final String url = myController.createLatLngURL(mapData.getStart(), getString(R.string.google_maps_key));
                    Log.d("URL Driver is ", url);
                    JSONObject jsonObject = myController.readUrl(url);

                    mapData = myController.getPlace(jsonObject);
                    drawMarker();

                }
                // As a rider
                else {
                    if (marker.getTitle().equals("Start")) {
                        Context context = getApplicationContext();
                        mapData.setStart(marker.getPosition());
                        Toast.makeText(context,"S: " + mapData.getStart().toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"E: " + mapData.getEnd().toString(),Toast.LENGTH_SHORT).show();
                    } else if (marker.getTitle().equals("End")) {
                        Context context = getApplicationContext();
                        mapData.setEnd(marker.getPosition());
                        Toast.makeText(context,"S: " + mapData.getStart().toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"E: " + mapData.getEnd().toString(),Toast.LENGTH_SHORT).show();
                    }

                    // After drag - search again
                    final String url = myController.createURl(mapData.getStart(),
                            mapData.getEnd(), getString(R.string.google_maps_key));
                    Log.d("URL is ", url);

                    JSONObject jsonObject = myController.readUrl(url);
                    mapData = myController.getInfoFromJson(jsonObject);
                    Log.d("Start LatLng is ", mapData.getStart().toString());
                    Log.d("End LatLng is ", mapData.getEnd().toString());
                    Log.d("Start Address is ", mapData.getStartAddress());
                    Log.d("End Address is ", mapData.getEndAddress());
                    Log.d("Distance", String.valueOf(mapData.getDistance()));

                    drawMarker();
                    drawRoute();
                }

            }
        });

        // Click search Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If it is Driver
                if ((riderOrDriver == null) || (!riderOrDriver.equals("fromRider"))) {
                    mapData.setStartAddress(startEditText.getText().toString().replaceAll(" ", "+"));
                    final String url = myController.createPlaceURL(mapData.getStartAddress(), getString(R.string.google_maps_key));
                    Log.d("URL Driver is ", url);
                    JSONObject jsonObject = myController.readUrl(url);

                    mapData = myController.getPlace(jsonObject);
                    drawMarker();
                }

                // As a rider
                else {
                    // Do something after it is clicked
                    mapData.setStartAddress(startEditText.getText().toString().replaceAll(" ", "+"));
                    mapData.setEndAddress(endEditText.getText().toString().replaceAll(" ", "+"));
                    // For test purpose:
//                    startAddress = "10310 102 Ave NW, Edmonton".replaceAll(" ", "+");
//                    endAddress = "11020 53 Ave. NW, Edmonton".replaceAll(" ", "+");

                    final String url = myController.createURl(mapData.getStartAddress(), mapData.getEndAddress(), getString(R.string.google_maps_key));
                    Log.d("URL is ", url);

                    JSONObject jsonObject = myController.readUrl(url);
                    mapData = myController.getInfoFromJson(jsonObject);
                    Log.d("Start LatLng is ", mapData.getStart().toString());
                    Log.d("End LatLng is ", mapData.getEnd().toString());
                    Log.d("Start Address is ", mapData.getStartAddress());
                    Log.d("End Address is ", mapData.getEndAddress());
                    Log.d("Distance", String.valueOf(mapData.getDistance()));
                    drawMarker();
                    drawRoute();
                }





            }
        });

        // Click ok button to return start and end points
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionPair = new ArrayList<>();
                if (riderOrDriver.equals("fromRider")) {
                    positionPair.add(mapData.getStart());
                    positionPair.add(mapData.getEnd());
                    Log.d("Start: ", mapData.getStart().toString());
                    Log.d("End: ", mapData.getEnd().toString());

                }
                else {
                    positionPair = new ArrayList<>();
                    positionPair.add(mapData.getStart());
                    Log.d("Start: ", mapData.getStart().toString());
                    
                }

                myController.sendCoordinates(positionPair, description);
                finish();
            }
        });

        // Click filter button - set the filter
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reworking to also provide the description input for riders
                if ((riderOrDriver == null) || (riderOrDriver.equals("fromRider"))) {
                    AlertDialog.Builder descBox = new AlertDialog.Builder(MapsActivity.this);
                    descBox.setTitle("Please enter your description");

                    final EditText editDesc = new EditText(MapsActivity.this);
                    editDesc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    descBox.setView(editDesc);

                    descBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            description = editDesc.getText().toString();
                            Log.d("TEST", "Got the string " + description);
                        }
                    });
                    descBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            description = "";
                            dialog.cancel();
                        }
                    });

                    descBox.show();
                }else {
                    // Do something about the filter
                    //go to filter activity
                    String location_cordinate = mapData.getStartAddress();
                    backFromFilter = true;
                    Intent filter_intent = new Intent(MapsActivity.this, FilterActivity.class);
                    filter_intent.putExtra("Location", location_cordinate);
                    startActivity(filter_intent);
                }
            }
        });

    }

    /**
     * Clear the map and draw the start marker and end marker.
     */
    private void drawMarker() {
        // If it is Driver
        if ((riderOrDriver == null) || (!riderOrDriver.equals("fromRider"))) {
            // Clear the map
            mMap.clear();

            startMarker = mMap.addMarker(new MarkerOptions().position(mapData.getStart()).draggable(true));

            // Add a indicator for the marker
            // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
            startMarker.setTitle(mapData.getStartAddress());

            //zoom to start position:
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mapData.getStart()).zoom(14).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        // If it is rider
        else {
            // Clear the map
            mMap.clear();

            startMarker = mMap.addMarker(new MarkerOptions().position(mapData.getStart()).draggable(true));
            endMarker = mMap.addMarker(new MarkerOptions().position(mapData.getEnd()).draggable(true));

            // Add a indicator for the marker
            // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
            startMarker.setTitle("Start");
            endMarker.setTitle("End");


            //zoom to start position:
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mapData.getStart()).zoom(14).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    /**
     * After draw the marker, it will draw the route.
     */
    public void drawRoute() {
        PolylineOptions options = new PolylineOptions().width(10).color(Color.argb(255, 66, 133, 244)).geodesic(true);
        List<LatLng> routeList = mapData.getRouteList();
        for (int z = 0; z < routeList.size(); z++) {
            LatLng point = routeList.get(z);
            options.add(point);
        }
        mMap.addPolyline(options);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mapData.getStart());
        builder.include(mapData.getEnd());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));

    }

    /**
     * Finish the activity when come back from filter.
     */
    @Override
    protected void onResume(){
        super.onResume();
        if (backFromFilter){
            finish();
        }
    }
}

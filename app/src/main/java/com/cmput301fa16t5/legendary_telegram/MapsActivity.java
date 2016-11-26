package com.cmput301fa16t5.legendary_telegram;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.protocol.RequestUserAgentHC4;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * MapsActivity class is the view of the map.
 * @author zhimao
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    // This is the actual map object
    private GoogleMap mMap;
    private LatLng start;
    private LatLng end;
    private String startAddress;
    private String endAddress;
    private float distance;
    private List<LatLng> routeList;
    private Marker startMarker;
    private Marker endMarker;

    private Button okButton;
    private Button searchButton;
    private Button filterButton;
    private EditText startEditText;
    private EditText endEditText;

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

        /**
         * Null in case of tests since there is nothing.
         * Not fromRider means Driver.
         */
        if ((riderOrDriver == null) || (!riderOrDriver.equals("fromRider"))) {
            startMarker = mMap.addMarker(new MarkerOptions().position(start).draggable(true));
            startMarker.setTitle("Start");
        }

        /**
         * "fromRider" aka two points.
         */
        else {
            startMarker = mMap.addMarker(new MarkerOptions().position(start).draggable(true));
            endMarker = mMap.addMarker(new MarkerOptions().position(end).draggable(true));

            // Add a indicator for the marker
            // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
            startMarker.setTitle("Start");
            endMarker.setTitle("End");

            filterButton.setText("Description");
        }
        //zoom to start position:
        CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

        // Click search Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something after it is clicked
                startAddress = startEditText.getText().toString().replaceAll(" ", "+");
                endAddress = endEditText.getText().toString().replaceAll(" ", "+");
                // For test purpose:
                startAddress = "10310 102 Ave NW, Edmonton".replaceAll(" ", "+");
                endAddress = "11020 53 Ave. NW, Edmonton".replaceAll(" ", "+");

                final String url = myController.createURl(startAddress, endAddress);
                Log.d("URL is ", url);

                JSONObject jsonObject = myController.readUrl(url);
                getInfoFromJson(jsonObject);
                drawMarker();
                drawRoute();
                Log.d("Start LatLng is ", start.toString());
                Log.d("End LatLng is ", end.toString());
                Log.d("Start Address is ", startAddress);
                Log.d("End Address is ", endAddress);
                Log.d("Distance", String.valueOf(distance));

            }
        });

        // Click ok button to return start and end points
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionPair = new ArrayList<LatLng>();
                if (riderOrDriver.equals("fromRider")) {
                    positionPair.add(start);
                    positionPair.add(end);
                    Log.d("Start: ", start.toString());
                    Log.d("End: ", end.toString());

                }
                else {
                    positionPair = new ArrayList<LatLng>();
                    positionPair.add(start);
                    Log.d("Start: ", start.toString());
                    
                }

                myController.sendCoordinates(positionPair, description);
                finish();
            }
        });

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
                    String location_cordinate = new String();
                    backFromFilter = true;
                    Intent filter_intent = new Intent(MapsActivity.this, FilterActivity.class);
                    filter_intent.putExtra("Location", location_cordinate);
                    startActivity(filter_intent);
                }
            }
        });

    }

    // Parse the Json file read from google map
    // Code from: http://stackoverflow.com/questions/7237290/json-parsing-of-google-maps-api-in-android-app
    public void getInfoFromJson(JSONObject jsonObject) {
        try {
            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Grab the first route
            JSONObject route = routesArray.getJSONObject(0);

            // Get the overview_polyline
            JSONObject polyLines = route.getJSONObject("overview_polyline");

            // Take all legs from the route
            JSONArray legs = route.getJSONArray("legs");

            // Grab first leg
            JSONObject leg = legs.getJSONObject(0);

            // Get the distance in float
            JSONObject distanceObject = leg.getJSONObject("distance");
            String distanceString = distanceObject.getString("text");
            String[] separated = distanceString.split(" ");
            distance = Float.valueOf(separated[0]);
            //Log.d("Distance", String.valueOf(distance));

            // Get start and end lat and lng
            JSONObject latlngStartObject = leg.getJSONObject("start_location");
            String latStartString = latlngStartObject.getString("lat");
            String lngStartString = latlngStartObject.getString("lng");
            start = new LatLng(Double.valueOf(latStartString), Double.valueOf(lngStartString));
            //Log.d("Start LatLng is ", start.toString());

            JSONObject latlngEndObject = leg.getJSONObject("end_location");
            String latEndString = latlngEndObject.getString("lat");
            String lngEndString = latlngEndObject.getString("lng");
            end = new LatLng(Double.valueOf(latEndString), Double.valueOf(lngEndString));
            //Log.d("End LatLng is ", end.toString());

            // Get the start and end address
            startAddress = leg.getString("start_address");
            endAddress = leg.getString("end_address");
            //Log.d("Start Address is ", startAddress);
            //Log.d("End Address is ", endAddress);




            /*
            // Code from: http://stackoverflow.com/questions/17425499/how-to-draw-interactive-polyline-on-route-google-maps-v2-android
            // Draw the polyLine
            String encodedString = polyLines.getString("points");
            List<LatLng> list = PolyUtil.decode(encodedString);
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            mMap.addPolyline(options);
            LatLng locationA = new LatLng(53.527400, -113.529435);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationA));
            */


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawMarker() {
        // Clear the map
        mMap.clear();

        startMarker = mMap.addMarker(new MarkerOptions().position(start).draggable(true));
        endMarker = mMap.addMarker(new MarkerOptions().position(end).draggable(true));

        // Add a indicator for the marker
        // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
        startMarker.setTitle("Start");
        endMarker.setTitle("End");


        //zoom to start position:
        CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }



    @Override
    protected void onResume(){
        super.onResume();
        if (backFromFilter){
            finish();
        }
    }
}

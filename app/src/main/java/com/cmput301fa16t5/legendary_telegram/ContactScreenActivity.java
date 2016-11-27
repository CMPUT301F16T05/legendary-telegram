package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Contact Screen -- Accept/Commit to Requests/Drivers, Phone/Email Contact info, see Route on Map.
 *
 *  What this class is:
 *  1) Allows a Rider to accept a Driver.
 *  2) Allows a Driver to accept/commit to a request.
 *
 *  3) Describes the views for the Contact Screen, where,
 *  for example, a Rider can see the info of a potential Driver
 *  or committed Driver and click those fields to phone/email them.
 *
 *  This activity is reached by
 *  a) A Rider Clicking on a specific driver in the RequestStatusActivity
 *  b) A Driver clicking on a Request in their MainRequestActivity.
 *
 */
public class ContactScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Used for google map display
    private GoogleMap mMap;
    private LatLng start;
    private LatLng end;
    private String startAddress;
    private String endAddress;
    private List<LatLng> routeList;
    private Marker startMarker;
    private Marker endMarker;
    // Can be used for future part 6
    private Double distance;

    private TextView infoTitle;
    private TextView infoDetail;
    private TextView phoneTxt;
    private TextView emailTxt;
    private TextView startFillText;
    private TextView endFillText;
    private Button commitButton;

    private ContactScreenController myController;
    private MapController mapController;

    /**
     * OnCreate Initializes views and Controller setup, and fills in button texts and fields.
     * @param savedInstanceState Because Android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

        // Obtain the MapFragment and get notified when the map is ready to be used.
        // Code from: http://stackoverflow.com/questions/36832035/nullpointerexception-at-mapfragment-getmapasync
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ContactMap);
        mapFragment.getMapAsync(this);

        myController = new ContactScreenController();
        // Construct map controller
        mapController = new MapController();

        infoTitle = (TextView) findViewById(R.id.infoTitle);
        infoDetail = (TextView) findViewById(R.id.infoTextView);
        phoneTxt = (TextView) findViewById(R.id.phoneTextView);
        emailTxt = (TextView) findViewById(R.id.emailTextView);
        startFillText = (TextView) findViewById(R.id.StartFill);
        endFillText = (TextView) findViewById(R.id.EndFill);
        commitButton = (Button) findViewById(R.id.commitButton);

        // In the case of a Rider we need the index of the Driver card they're viewing.
        if (!myController.isDriverOrRider()) {
            myController.getIndex(getIntent().getStringExtra("CardNum"));
        }

        fillOutFields(myController.getEntries());
        String buttonText = myController.setButtonText();

        if (buttonText == null) {
            commitButton.setText("Already Has Committed Driver");
            commitButton.setClickable(false);
        }

        else {
            commitButton.setText(buttonText);
        }


        //@Yu Tang Lin
        phoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://stackoverflow.com/questions/5403308/make-a-phone-call-click-on-a-button//
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phoneTxt.getText().toString().trim()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });


        // Clicking the email field is handled by
        // http://stackoverflow.com/questions/10464954/how-to-make-an-email-address-clickable
        // http://stackoverflow.com/questions/9689732/email-intent-email-chooser
        emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                        emailTxt.getText().toString(), null));
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Legendary Telegram " +
                        "Request ID: " + infoTitle.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    /**
     * When google map is ready, get the URL from start and end point.
     * Send the URL to google map server, which returns a JSON object.
     * Read the JSON object in order to draw the route.
     * Coded by Zhimao
     * @param googleMap Map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Request request = myController.getRequestOfFocus();
        start = request.getStartLocation();
        end = request.getEndLocation();

        final String url = mapController.createURl(start, end, getString(R.string.google_maps_key));
        Log.d("URL is ", url);

        JSONObject jsonObject = mapController.readUrl(url);
        getInfoFromJson(jsonObject);



    }

    /**
     * Parse the Json file read from google map
     * @param jsonObject Object to read from
     */
    // Code from: http://stackoverflow.com/questions/7237290/json-parsing-of-google-maps-api-in-android-app
    private void getInfoFromJson(JSONObject jsonObject) {
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
            // Can be used for future part 6
            JSONObject distanceObject = leg.getJSONObject("distance");
            String distanceString = distanceObject.getString("text");
            String[] separated = distanceString.split(" ");
            distance = Double.valueOf(separated[0].replaceAll(",", ""));
            //Log.d("Distance", String.valueOf(distance));

            // Get the start and end address
            startAddress = leg.getString("start_address");
            endAddress = leg.getString("end_address");
            Log.d("Start Address is ", startAddress);
            Log.d("End Address is ", endAddress);

            // Display the Address - Regarding to User Story: 04.04.01
            startFillText.setText(startAddress);
            endFillText.setText(endAddress);


            // Parse the route
            String encodedString = polyLines.getString("points");
            // learn from: http://googlemaps.github.io/android-maps-utils/javadoc/com/google/maps/android/PolyUtil.html
            // PolyUtil.decode() returns List<LatLng>
            routeList = PolyUtil.decode(encodedString);

            // Clear the map and draw the marker and route
            mMap.clear();
            // Add Start and End markers
            startMarker = mMap.addMarker(new MarkerOptions().position(start).draggable(false));
            endMarker = mMap.addMarker(new MarkerOptions().position(end).draggable(false));
            // Add a title for the marker - showing by click the marker
            // Learn from: https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#setSnippet(java.lang.String)
            startMarker.setTitle("Start: "+ startAddress);
            endMarker.setTitle("End: " + endAddress);

            // Draw the route
            PolylineOptions options = new PolylineOptions().width(10).color(Color.argb(255, 66, 133, 244)).geodesic(true);
            for (int z = 0; z < routeList.size(); z++) {
                LatLng point = routeList.get(z);
                options.add(point);
            }
            mMap.addPolyline(options);

            // Move the camera to the route
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(start);
            builder.include(end);
            Log.d("Start LatLng is ", start.toString());
            Log.d("End LatLng is ", end.toString());

            // Code from: http://stackoverflow.com/questions/25231949/add-bounds-to-map-to-avoid-swiping-outside-a-certain-region/30497039#30497039
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30); // offset from edges of the map 12% of screen
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * Fills out the views with appropriate information.
     * @param entries String[] containing username, phone number, email and the request ID.
     */
    private void fillOutFields(String[] entries){
        infoTitle.setText(entries[0]);
        infoDetail.setText(entries[1]);
        phoneTxt.setText(entries[2]);
        emailTxt.setText(entries[3]);
    }

    /**
     * Calls controller to handle. Prints a message based on what it determines.
     * @param v Android
     */
    public void commitButtonPress(View v) {
        Toast.makeText(getApplicationContext(), myController.commitPress(), Toast.LENGTH_LONG).show();
        finish();
    }
}

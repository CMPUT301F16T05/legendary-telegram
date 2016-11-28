package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * MapController class is the controller of the map.
 * It is used to create the URL according to different parameters.
 * It is also used to sent the URL to the server and get JSON object return.
 * @author zhimao
 */

public class MapController {

    private final CentralController centralCommand;
    private JSONObject jsonObject;

    /**
     * Constructor
     */
    public MapController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Sends the Central Controller coordinates depending on whether a new Request will be made
     * (Rider) or whether requests will be looked for (Driver).
     * @param positionPair An ArrayList of Coordinates.
     */
    public void sendCoordinates(ArrayList<LatLng> positionPair, String description) {
        if (positionPair.size() == 1) {
            centralCommand.searchRequests(positionPair);
        } else {
            centralCommand.createRequest(positionPair, description);
        }
    }

    /**
     * It returns a URL for searching directions between two addresses.
     * @param startAddress
     * @param endAddress
     * @param key
     * @return URL for searching directions between two addresses
     */
    public String createURl(String startAddress, String endAddress, String key) {
        // example: https://maps.googleapis.com/maps/api/directions/json?origin=Edmonton&destination=vancouver&key=KKKEEEYYY
        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + startAddress
                + "&destination=" + endAddress // + "&mode=driving" (Default mode is driving)
                + "&key=" + key;
    }

    /**
     * It returns a URL searching for directions between two LatLng points.
     * @param start
     * @param end
     * @param key
     * @return URL for searching directions between two LatLng points
     */
    public String createURl(LatLng start, LatLng end, String key) {
        // example: https://maps.googleapis.com/maps/api/directions/json?origin=Edmonton&destination=vancouver&key=KKKEEEYYY
        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + String.valueOf(start.latitude) + "," + String.valueOf(start.longitude)
                + "&destination=" + String.valueOf(end.latitude) + "," + String.valueOf(end.longitude) // + "&mode=driving" (Default mode is driving)
                + "&key=" + key; //+ R.string.google_maps_key;
    }

    /**
     * It returns a URL for searching a place by its address.
     * @param address
     * @param key
     * @return URL for searching a place by its address
     */
    public String createPlaceURL(String address, String key) {
        // Learn from: https://developers.google.com/maps/documentation/geocoding/start
        // https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + address
                + "&key=" + key; //+ R.string.google_maps_key;
    }

    /**
     * It returns a URL for searching a place by its LatLng points.
     * @param point
     * @param key
     * @return URL for searching a place by its LatLng points
     */
    public String createLatLngURL(LatLng point, String key) {
        // Learn from: https://developers.google.com/maps/documentation/geocoding/start
        // https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(point.latitude) + "," + String.valueOf(point.longitude)
                + "&key=" + key; //+ R.string.google_maps_key;
    }

    /**
     * Send the URL to google map server in the background.
     * It returns a JSON object.
     * @param url
     * @return JSON object of the searching result
     */
    public JSONObject readUrl(final String url) {
        // Running URL has to be in another thread. Not in main thread.
        // Code from: http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception/6343299#6343299

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    jsonObject = readJsonFromUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // Start the thread
        thread.start();
        // Code from: http://stackoverflow.com/questions/16624788/wait-for-a-thread-before-continue-on-android
        // Wait for the thread to be finished
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Read URL
     * @param urlString
     * @return JSON object from URL.
     * @throws IOException
     * @throws JSONException
     */
    // Code from: http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    private JSONObject readJsonFromUrl(String urlString) throws IOException, JSONException {
        InputStream is = new URL(urlString).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            Log.d("json file ", jsonText);

            jsonObject = new JSONObject(jsonText);

            return jsonObject;

        } finally {
            is.close();
        }
    }

    /**
     * Input address and return the Latitude and Longtitude of the address
     * @param jsonObject
     * @return a MapData object containing all the parsed information
     */
    public MapData getPlace(JSONObject jsonObject) {
        MapData mapData = new MapData();
        try {
            // resultsArray contains the place
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            // Grab the first route
            JSONObject result = resultsArray.getJSONObject(0);

            // Get the format string
            mapData.setStartAddress(result.getString("formatted_address"));

            // Get the geometry object
            JSONObject geometry = result.getJSONObject("geometry");

            // Get location object
            JSONObject location = geometry.getJSONObject("location");

            // Get the LatLng
            String latStartString = location.getString("lat");
            String lngStartString = location.getString("lng");
            mapData.setStart(new LatLng(Double.valueOf(latStartString), Double.valueOf(lngStartString)));
            Log.d("Start LatLng is ", mapData.getStart().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mapData;
    }

    /**
     * Parse the Json file read from google map.
     * We can get folloing information:
     * Start location
     * End location
     * Start address
     * End address
     * distance (For future part 6)
     * Route
     * @param jsonObject
     * @return a MapData object containing all the parsed information
     */
    // Code from: http://stackoverflow.com/questions/7237290/json-parsing-of-google-maps-api-in-android-app
    public MapData getInfoFromJson(JSONObject jsonObject) {
        MapData mapData = new MapData();
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

            // Get the distance in Double
            JSONObject distanceObject = leg.getJSONObject("distance");
            String distanceString = distanceObject.getString("text");
            String[] separated = distanceString.split(" ");
            mapData.setDistance(Double.valueOf(separated[0].replaceAll(",", "")));
            //Log.d("Distance", String.valueOf(distance));

            // Get start and end lat and lng
            JSONObject latlngStartObject = leg.getJSONObject("start_location");
            String latStartString = latlngStartObject.getString("lat");
            String lngStartString = latlngStartObject.getString("lng");
            mapData.setStart(new LatLng(Double.valueOf(latStartString), Double.valueOf(lngStartString)));
            //Log.d("Start LatLng is ", start.toString());

            JSONObject latlngEndObject = leg.getJSONObject("end_location");
            String latEndString = latlngEndObject.getString("lat");
            String lngEndString = latlngEndObject.getString("lng");
            mapData.setEnd(new LatLng(Double.valueOf(latEndString), Double.valueOf(lngEndString)));
            //Log.d("End LatLng is ", end.toString());

            // Get the start and end address
            mapData.setStartAddress(leg.getString("start_address"));
            mapData.setEndAddress(leg.getString("end_address"));
            //Log.d("Start Address is ", startAddress);
            //Log.d("End Address is ", endAddress);


            // Parse the route
            String encodedString = polyLines.getString("points");
            // learn from: http://googlemaps.github.io/android-maps-utils/javadoc/com/google/maps/android/PolyUtil.html
            // PolyUtil.decode() returns List<LatLng>
            mapData.setRouteList(PolyUtil.decode(encodedString));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mapData;
    }

}

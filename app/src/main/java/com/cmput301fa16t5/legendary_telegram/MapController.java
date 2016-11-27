package com.cmput301fa16t5.legendary_telegram;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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

    // Central controller - construct request
    private CentralController centralCommand;
    // Return JSON object
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
     * @param positionPair: An ArrayList of Coordinates.
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
     * @return
     * @throws IOException
     * @throws JSONException
     */
    // Code from: http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    public JSONObject readJsonFromUrl(String urlString) throws IOException, JSONException {
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
}

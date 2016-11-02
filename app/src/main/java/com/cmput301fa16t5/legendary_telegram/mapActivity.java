package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Riders pick where they are and where they want to go.
 * Drivers pick where they are looking for Requests in.
 */
public class MapActivity extends AppCompatActivity {

    private MapController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        myController = new MapController();
    }
}

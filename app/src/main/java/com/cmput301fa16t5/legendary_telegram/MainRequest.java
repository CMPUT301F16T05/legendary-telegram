package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainRequest extends AppCompatActivity {

    private TextView title;
    private Button goToSettings;
    private Button findRequests;
    private Button makeRequests;
    private ListView relevantRequests;
    private ArrayAdapter<Request> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_request);

        title = (TextView) findViewById(R.id.mainRequestTitle);
        goToSettings = (Button) findViewById(R.id.settingsButton);
        findRequests = (Button) findViewById(R.id.driverButton);
        makeRequests = (Button) findViewById(R.id.riderButton);
        relevantRequests = (ListView) findViewById(R.id.mainRequestLV);
    }
}

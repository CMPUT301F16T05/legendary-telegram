package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * After a user logs in, they see this screen.
 * It's where they choose to find requests or make them
 * Or see a listview of requests they can choose from/have made.
 */
public class MainRequestActivity extends AppCompatActivity {

    private Button disabledButton;
    private TextView title;
    private Button goToSettings;
    private Button findRequests;
    private Button makeRequests;
    private ListView relevantRequests;
    private ArrayAdapter<Request> adapter;

    private MainRequestController myController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_request);

        myController = new MainRequestController();

        title = (TextView) findViewById(R.id.mainRequestTitle);
        goToSettings = (Button) findViewById(R.id.settingsButton);
        findRequests = (Button) findViewById(R.id.driverButton);
        makeRequests = (Button) findViewById(R.id.riderButton);
        relevantRequests = (ListView) findViewById(R.id.mainRequestLV);


        //@Yu Tang Lin - goes to userProfile Activity to change User setting
        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disabledButton = (Button)findViewById(R.id.addUser);
                disabledButton.setVisibility(View.GONE);
                Intent intent = new Intent(MainRequestActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

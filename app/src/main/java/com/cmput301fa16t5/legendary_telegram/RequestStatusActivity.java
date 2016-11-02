package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Clicking on a Request in MainRequestActivity should lead to this.
 * For a Rider this would be more detailed information like the number of
 * Drivers have offered to fulfill that request. From there they could click on a driver shown
 * in a ListView and go to ContactScreenActivity to see the phone/email and contact them.
 */
public class RequestStatusActivity extends AppCompatActivity {

    private TextView title;
    private Button cancelButton;
    private ListView requestsDriversLV;
    private ArrayAdapter<String> adapter;

    private RequestStatusController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        myController = new RequestStatusController();

        title = (TextView) findViewById(R.id.specificRequestStatus);
        cancelButton = (Button) findViewById(R.id.cancelReqButton);
        requestsDriversLV = (ListView) findViewById(R.id.RequestInfoLV);

        // Ideally when the activity is called it unpacks the info about the request
        // Then it will get the name of the request or more specifically its
        // to and from location. And then change the title text;
        setTitleText("Placeholder");
    }

    private void setTitleText(String titleText) {
        title.setText(titleText);
    }
}

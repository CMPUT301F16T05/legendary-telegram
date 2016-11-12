package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private ArrayAdapter<IdentificationCard> adapter;

    private RequestStatusController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        myController = new RequestStatusController();

        title = (TextView) findViewById(R.id.specificRequestStatus);
        cancelButton = (Button) findViewById(R.id.cancelReqButton);
        requestsDriversLV = (ListView) findViewById(R.id.RequestInfoLV);

        title.setText(myController.getRequestName());


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.adapter = myController.setRequestAdapter(this);
    }

    public void cancelButtonPressed(View v) {
        // Cancel the request.
        // Remove it from view.
        // Remove from ESearch.
        // go back.
    }
}

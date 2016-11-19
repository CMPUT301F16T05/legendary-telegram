package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private ListView requestsDriversLV;
    private ArrayAdapter<IdentificationCard> adapter;

    private RequestStatusController myController;

    /**
     * Initialize views, set ArrayAdapter.
     * This view shows the IdentificationCards.
     * We initialize the ArrayAdapter in onCreate
     * because it is linked to a specific ArrayList and does not change (unlike main request activity)
     * @param savedInstanceState: Because Android.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        myController = new RequestStatusController();

        title = (TextView) findViewById(R.id.specificRequestStatus);
        requestsDriversLV = (ListView) findViewById(R.id.RequestInfoLV);

        title.setText(myController.getRequestName());

        this.adapter = myController.setRequestAdapter(this);
        requestsDriversLV.setAdapter(adapter);

        requestsDriversLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapt, View v, int position, long l) {
                Intent intent = new Intent(RequestStatusActivity.this, ContactScreenActivity.class);
                intent.putExtra("CardNum", Integer.toString(position));
                startActivity(intent);
            }
        });
    }

    /**
     * Cancel a Request. Go back, since it's Cancelled.
     * @param v
     */
    public void cancelButtonPressed(View v) {
        myController.cancel();
        finish();
    }

    /**
     * We want to remove the old adapter on destroy since next time we'll be
     * calling a new onCreate with a new adapter and we don't want to fill
     * the arraylist of ArrayObserver with a bunch of nulls/old adapters.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myController.removeAdapter(this.adapter);
    }
}

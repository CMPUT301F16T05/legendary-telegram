package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 *  What this class is:
 *  1) Allows a Rider to accept a Driver.
 *  2) Allows a Driver to commit to a Rider who has accepted them.
 *
 *  3) Describes the views for the Contact Screen, where,
 *  for example, a Rider can see the info of a potential Driver
 *  or committed Driver and click those fields to phone/email them.
 *
 *  This activity is reached by
 *  a) A Rider Clicking on a specific driver in the RequestStatusActivity
 *  b) A Driver clicking on a Request in their MainRequestActivity.
 *
 *  This class is responsible for telling the GUI what it should display, the numbers
 *  to call or the addresses to email. It doesn't actually invoke the function call
 *  to call that number or make an email, that is left to ContactScreenController.
 */
public class ContactScreenActivity extends AppCompatActivity {

    private TextView infoTitle;
    private TextView infoDetail;
    private TextView phoneTxt;
    private TextView emailTxt;
    private Button commitButton;

    private ContactScreenController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

        myController = new ContactScreenController();

        infoTitle = (TextView) findViewById(R.id.infoTitle);
        infoDetail = (TextView) findViewById(R.id.infoTextView);
        phoneTxt = (TextView) findViewById(R.id.phoneTextView);
        emailTxt = (TextView) findViewById(R.id.emailTextView);
        commitButton = (Button) findViewById(R.id.commitButton);

        infoTitle.setText("For Rider it displays Driver Info for Driver, Rider Info");
        infoDetail.setText("The actual details");
        phoneTxt.setText("The phone number. Clicking it should cause the phone to dial the number.");
        emailTxt.setText("Email. Clicking it should... you get the idea.");

        commitButton.setText("Accepting a Driver as a Rider? Accepting a Rider as a Driver? Depends on context");


    }
}

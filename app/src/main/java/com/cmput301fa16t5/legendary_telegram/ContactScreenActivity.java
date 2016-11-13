package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
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
                startActivity(callIntent);
            }
        });

        // Clicking the email field is handled by
        // http://stackoverflow.com/questions/10464954/how-to-make-an-email-address-clickable
    }

    /**
     * Fills out the views with appropriate information.
     * @param entries: String[] containing username, phone number, email and the request ID.
     */
    private void fillOutFields(String[] entries){
        infoTitle.setText(entries[0]);
        infoDetail.setText(entries[1]);
        phoneTxt.setText(entries[2]);
        emailTxt.setText(entries[3]);
    }

    /**
     * Calls controller to handle. Prints a message based on what it determines.
     * @param v
     */
    public void commitButtonPress(View v) {
        Toast.makeText(getApplicationContext(), myController.commitPress(getApplicationContext()), Toast.LENGTH_LONG).show();
        finish();
    }
}

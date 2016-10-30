package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class contactScreen extends AppCompatActivity {

    private TextView infoTitle;
    private TextView infoDetail;
    private TextView phoneTxt;
    private TextView emailTxt;
    private Button commitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

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

package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class userProfile extends AppCompatActivity {

    private Button checkUser;
    private Button changeSettings;
    private EditText nameEntered;
    private EditText pwEntered;
    private EditText confirmPW;
    private EditText phoneEntered;
    private EditText emailEntered;
    private EditText vehicleEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        checkUser = (Button) findViewById(R.id.checkUsrName);
        changeSettings = (Button) findViewById(R.id.changeSettings);
        nameEntered = (EditText) findViewById(R.id.userNameSettings);
        pwEntered = (EditText) findViewById(R.id.userPassword);
        confirmPW = (EditText) findViewById(R.id.userConfirmPassword);
        phoneEntered = (EditText) findViewById(R.id.userPhone);
        emailEntered = (EditText) findViewById(R.id.userEmail);
        vehicleEntered = (EditText) findViewById(R.id.userVehicle);
    }
}

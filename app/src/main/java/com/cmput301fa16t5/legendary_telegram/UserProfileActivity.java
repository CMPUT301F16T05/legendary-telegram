package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
This is the activity called upon when:
1) A new user account is to be created (reached by login activity).
2) An existing user account to be modified (reached by main request activity)

In the first case, the fields are to be blank, filled with nothing, and the button at the bottom (changeSettings)
says "create new account"

In the second case, the fields are to be filled with the existing information about the user, which they can modify.
The button at the bottom will say "change settings"

The check button at the top checks the name entered into the field. If a user with that name already exists
a new user account cannot be made/the existing account cannot be modified. 

When the user creates or modifies an account the function that checks the name should be called anyway. THe 
separate button exists for convience.
*/

public class UserProfileActivity extends AppCompatActivity {

    private Button updateButton;
    private Button checkUser;
    private Button addButton;
    private EditText nameEntered;
    private EditText phoneEntered;
    private EditText emailEntered;
    private EditText vehicleEntered;
    private String key_string;

    private boolean validation;
    private User newUser;

    private UserProfileController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        validation = false;
        myController = new UserProfileController();

        checkUser = (Button) findViewById(R.id.checkUsrName);
        addButton = (Button) findViewById(R.id.addUser);
        updateButton = (Button)findViewById(R.id.updateUser);

        nameEntered = (EditText) findViewById(R.id.userNameSettings);
        phoneEntered = (EditText) findViewById(R.id.userPhone);
        emailEntered = (EditText) findViewById(R.id.userEmail);
        vehicleEntered = (EditText) findViewById(R.id.userVehicle);

        key_string = getIntent().getStringExtra("Register");

        if(key_string.equals("fromRegister")){
            updateButton.setVisibility(View.GONE);
        }else{
            addButton.setVisibility(View.GONE);
        }

        /**Click on the Check button to see if the User name is valid
         * if yes, return a toast saying "OK TO USE
         * else, return a toast saying "NOT VALID"
         * if the name is not valid set the validation to false
         * so when the user click submit with unvalid name, the app
         * will return a toast saying "Unvalid User Name"
         */
        checkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GsonController.checkIfExists(nameEntered.getText().toString(), getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "User Name Already Exist.", Toast.LENGTH_SHORT).show();
                }
                else {
                    validation = true;
                    Toast.makeText(getApplicationContext(), "Unvalid User Name.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * the  addButton only shows up when the previous activity is from login activity
         * THis is for new user when they register(username does not exist in gson file)
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation==true){
                    newUser = new User(nameEntered.getText().toString(),emailEntered.getText().toString(),phoneEntered.getText().toString());
                    //if the user has vehicle, add the vehicle number in
                    if(vehicleEntered.getText().toString()!= ""){
                        newUser.setVehicle(vehicleEntered.getText().toString());
                    }
                    GsonController.saveUserToDisk(newUser, getApplicationContext());
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unvalid User Name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

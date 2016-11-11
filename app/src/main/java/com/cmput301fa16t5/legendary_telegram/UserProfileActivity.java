package com.cmput301fa16t5.legendary_telegram;

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

    //the currentUser will be null if not login
    private User currentUser;


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

        /**
         * @ Yu Tang Lin                                      NOV-9-2016
         * the key_string will get the string message pass from MainRequestActivity
         * if the key_string is from MainRequestActivity, hide the addButton
         *    get the info of current login user, and set them to EditText
         * else this activity is called from LoginActivity and hide the updateButton
         */
        key_string = getIntent().getStringExtra("fromMainRequest");

        if(key_string.equals("fromMainRequest")){
            addButton.setVisibility(View.GONE);
            currentUser = myController.GetCurrentUser();
            nameEntered.setText(currentUser.getUserName());
            phoneEntered.setText(currentUser.getTelephone());
            emailEntered.setText(currentUser.getEmail());
            vehicleEntered.setText(currentUser.getVehicle());
        }else{
            updateButton.setVisibility(View.GONE);
        }

        /**
         * @ Yu Tang Lin                                   NOV-9-2016
         * Click on the Check button to see if the User name is valid
         * if userName already exist in Gson fileList, pass a toast saying
         * the name already exists.
         * else, pass a toast saying Valid User Name" and set the validation to True
         * Issue:
         * When the user get to this activity from MainRequestActivity, the EditText will be filled
         * up with the user info. However, if the user click the check button without changing the name,
         * the user will not be able to update its changes
         */

        checkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GsonController.checkIfExists(nameEntered.getText().toString(), getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "User Name Already Exist.", Toast.LENGTH_SHORT).show();
                }
                else {
                    validation = true;
                    Toast.makeText(getApplicationContext(), "Valid User Name.", Toast.LENGTH_SHORT).show();
                }
                /**
                 * My solution for issue is that we checked whether or not the user name of the currentUser is same
                 * as the name in Name Edit Text
                 * if it is same we set the validation to true;
                 */

                if (currentUser.getUserName().equals(nameEntered.getText().toString())){
                    validation =true;
                }
            }
        });

        /**
         * @ Yu Tang Lin                                                       NOV-9-2016
         * the  addButton only shows up when the previous activity is from login activity
         * THis is for new user when they register(username does not exist in gson file)
         * if the userName is valid (does not exsist in gson fileList) add it in.
         * else pass a toast saying user name is unvalid
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation==true){
                    newUser = new User(nameEntered.getText().toString(),emailEntered.getText().toString(),phoneEntered.getText().toString());
                    if(vehicleEntered.getText().toString()!= ""){
                        newUser.setVehicle(vehicleEntered.getText().toString());
                    }
                    // save to disk
                    GsonController.saveUserToDisk(newUser, getApplicationContext());
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unvalid User Name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * updateButton only showed up when this Activity is called from MainRequestActivity
         * if the name check pass, and the user did not change the user name,
         * change the user properties to the new properties
         * else deletetheFile and create a new one
         */

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation == true) {
                    if (currentUser.getUserName().equals(nameEntered.getText().toString())) {
                        //change the attribute.... need safe?
                        currentUser.setTelephone(phoneEntered.getText().toString());
                        currentUser.setEmail(emailEntered.getText().toString());
                        currentUser.setVehicle(vehicleEntered.getText().toString());
                    }else{
                        //deleteFile and create a new one
                        GsonController.deleteOldUserName(nameEntered.getText().toString(), getApplicationContext());
                        GsonController.saveUserToDisk(currentUser, getApplicationContext());
                    }
                }
            }
        });
    }
}
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
 Originally created by Keith.
 Filled out by Yu Tang Lin
 Refactored by Keith
*/
public class UserProfileActivity extends AppCompatActivity {

    private Button updateButton;
    private Button checkUser;
    private EditText nameEntered;
    private EditText phoneEntered;
    private EditText emailEntered;
    private EditText vehicleEntered;
    private String key_string;

    private UserProfileController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        myController = new UserProfileController();

        checkUser = (Button) findViewById(R.id.checkUsrName);
        updateButton = (Button)findViewById(R.id.userSettingsMain);

        nameEntered = (EditText) findViewById(R.id.userNameSettings);
        phoneEntered = (EditText) findViewById(R.id.userPhone);
        emailEntered = (EditText) findViewById(R.id.userEmail);
        vehicleEntered = (EditText) findViewById(R.id.userVehicle);

        /**
        * the key_string will get the string message pass from MainRequestActivity
        * change the button text go Edit User Profile and get the info of current
        * user and set it info to the Edit Text Field
        * else we set the button text as Create New User since this activity will be
        * called from login activity
        */
        key_string = getIntent().getStringExtra("Setting");

        if (key_string.equals("fromMainRequest")) {
            updateButton.setText("Edit User Profile");

            String[] fieldData = myController.getProfileData();

            nameEntered.setText(fieldData[0]);
            emailEntered.setText(fieldData[1]);
            phoneEntered.setText(fieldData[2]);
            vehicleEntered.setText(fieldData[3]);
        }

        else {
            updateButton.setText("Create New User");
        }

        /**
         * Click on the Check button to see if the User name is valid
         * if userName already exist in Gson fileList, pass a toast saying
         * the name already exists.
         * else, pass a toast saying Valid User Name" and set the validation to True
         */
        checkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myController.invalidateName(nameEntered.getText().toString())){
                    toastFromEnum(UserProfileEnum.badNameMemory);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Valid User Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * when click on update Button, it will check which activity is calling this activity
         * and change or create user profile
         */
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserProfileEnum buttonEnum;
                if (key_string.equals("fromMainRequest")) {
                    buttonEnum = myController.attemptEditUser(nameEntered.getText().toString(),
                            emailEntered.getText().toString(),
                            phoneEntered.getText().toString(),
                            vehicleEntered.getText().toString());
                }

                else {
                    buttonEnum = myController.attemptNewUser(nameEntered.getText().toString(),
                            emailEntered.getText().toString(),
                            phoneEntered.getText().toString(),
                            vehicleEntered.getText().toString());

                    if (buttonEnum.equals(UserProfileEnum.allValid)) {
                        toastFromEnum(buttonEnum);
                        finish();
                    }
                }

                toastFromEnum(buttonEnum);
            }
        });
    }

    /**
     * Toasts a message based on Enum passed in.
     * @param returnEnum Emum determining the process status of fields entered.
     */
    private void toastFromEnum(UserProfileEnum returnEnum) {
        String message;

        if (returnEnum.equals(UserProfileEnum.badNameMemory)) {
            message = "User Name Already Exists or is Invalid";
        }

        else if (returnEnum.equals(UserProfileEnum.badNameStyle)) {
            message = "User Name contains bad characters (E.g. *, .)";
        }

        else if (returnEnum.equals(UserProfileEnum.badEmail)) {
            message = "Email must contain one '@' and one '.'";
        }

        else if (returnEnum.equals(UserProfileEnum.badPhone)) {
            message = "Phone number must only contain numbers and dashes";
        }

        else {
                message = "Valid Entries. Operation Executed.";
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
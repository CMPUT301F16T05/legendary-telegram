package com.cmput301fa16t5.legendary_telegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity (View) class for Log In Screen.
 * If you press the new user button, you get taken to an activity where you can make a new user,
 * no questions asked.
 * If you press the login button, you must first provide a valid username for this device.
 * @author kgmills
 */
public class LogInActivity extends AppCompatActivity {

    private EditText userName;
    private LogInController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        myController = new LogInController();

        userName = (EditText) findViewById(R.id.userNameET);
    }

    /**
     * @ Yu Tang Lin
     * On click function for R.id.newUserLogin
     * Go to UserProfileActivity to create new User.
     * Since we got 2 Activity calling the UserProfileActivity, we
     * pass a message see which activity is calling the UserProfileActivity
     * (MainRequestActivity also called the UserProfileActivity to change setting)
     * @param v: Needed or else runtime exception.
     */
    public void newUserClick(View v) {
        Intent intent = new Intent(LogInActivity.this, UserProfileActivity.class);
        intent.putExtra("Register", "fromRegister");
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    /**
     * On click function for R.id.loginButton
     * Checks userName (EditText) input and if valid, logs you into the program.
     * Else it will tell you you need to enter one.
     * @param v Needed or else runtime exception
     */
    public void loginClick(View v) {

        if (myController.validateUserName(userName.getText().toString(), getApplicationContext())) {
            startActivity(new Intent(this, MainRequestActivity.class));
        }
        else {
            Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
        }
    }
}

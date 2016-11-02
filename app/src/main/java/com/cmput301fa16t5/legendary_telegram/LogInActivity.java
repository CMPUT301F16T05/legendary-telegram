package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * You see the xml corresponding to this activity as soon as the App starts
 * User enters a username. If that username exists (i.e. has a file in system memory)
 * they proceed. If not, they must enter an existing name or create a new user account.
 * This class displays all the information. The act of seeing if the Username exists
 * is delegated to LogInController
 */
public class LogInActivity extends AppCompatActivity {

    private EditText userName;
    private Button login;
    private Button newUser;

    private LogInController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        myController = new LogInController();

        userName = (EditText) findViewById(R.id.userNameET);
        login = (Button) findViewById(R.id.loginButton);
        newUser = (Button) findViewById(R.id.newUserLogin);
    }
}

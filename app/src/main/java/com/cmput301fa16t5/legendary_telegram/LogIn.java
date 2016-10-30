package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Right now, this only contains the basic information.
 * For simplicity, I've taken the liberty of instantiating all the view variables.
 * I will do the same for the remaining activities.
 */
public class LogIn extends AppCompatActivity {

    private EditText userName;
    private Button login;
    private Button newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = (EditText) findViewById(R.id.userNameET);
        login = (Button) findViewById(R.id.loginButton);
        newUser = (Button) findViewById(R.id.newUserLogin);
    }
}

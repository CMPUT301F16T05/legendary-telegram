package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.support.test.InstrumentationRegistry;

import com.robotium.solo.Solo;

/**
 * Created by idrea on 2016/11/12.
 */

public class UserProfileActivityTests_Main extends ActivityInstrumentationTestCase2<LogInActivity> {
    private Solo solo;
    public UserProfileActivityTests_Main(){
        super(com.cmput301fa16t5.legendary_telegram.LogInActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCheckUser(){
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestUsername_main");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "A@test.gmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "1234680");

        solo.clickOnButton("Create New User");
        solo.waitForText("New User Created");
        solo.assertCurrentActivity("Should be log in activity", LogInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameET), "TestUsername_main");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Should be main request activity", MainRequestActivity.class);
        solo.clickOnButton("Setting");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);

        solo.clickOnButton("Check");
        solo.waitForText("User Name Already Exists or is Invalid.");

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestUsername1");
        solo.clickOnButton("Check");
        solo.waitForText("Valid User Name.");

        Context testContext = InstrumentationRegistry.getTargetContext();
        assertTrue(testContext.deleteFile("TestUsername_main.sav"));
    }

    public void testUpdateButton(){
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestUsername_main");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "A@test.gmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "1234680");

        solo.clickOnButton("Create New User");
        solo.waitForText("New User Created");
        solo.assertCurrentActivity("Should be log in activity", LogInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameET), "TestUsername_main");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Should be main request activity", MainRequestActivity.class);
        solo.clickOnButton("Setting");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userEmail), "AA@test.gmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "11234680");

        solo.clickOnButton("Edit User Profile");
        solo.waitForText("User Settings Edited");
        solo.goBack();
        solo.assertCurrentActivity("Should be main request activity", MainRequestActivity.class);
        Context testContext = InstrumentationRegistry.getTargetContext();
        assertTrue(testContext.deleteFile("TestUsername_main.sav"));
    }
}

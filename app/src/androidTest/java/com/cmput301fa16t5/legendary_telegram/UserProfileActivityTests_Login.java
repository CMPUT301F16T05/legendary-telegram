package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by idrea on 2016/11/5.
 */

public class UserProfileActivityTests_Login extends ActivityInstrumentationTestCase2<LogInActivity> {
    private Solo solo;
    public UserProfileActivityTests_Login(){
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
        solo.clickOnButton("Check");
        solo.waitForText("User Name Already Exists or is Invalid");

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestUsername_login");
        solo.clickOnButton("Check");
        solo.waitForText("Valid Entries. Operation Executed.");
    }

    public void testUpdateButton(){
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestUsername_login");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "A@test.gmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "1234680");

        solo.clickOnButton("Create New User");
        solo.waitForText("Valid Entries. Operation Executed.");
        solo.assertCurrentActivity("Should be log in activity", LogInActivity.class);
    }

    public void tearDown() throws Exception {
        Context testContext = InstrumentationRegistry.getTargetContext();
        testContext.deleteFile("TestUsername_login.sav");
    }
}

package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

/**
 * MapsActivityTests class contains unit tests for MapsActivity.
 * @author zhimao
 */

// Use the code from Lab 6
public class MapsActivityTests extends ActivityInstrumentationTestCase2<MapsActivity> {
    private Solo solo;

    // Constructor
    public MapsActivityTests() {
        super(com.cmput301fa16t5.legendary_telegram.MapsActivity.class);
    }

    // Start the activity
    public void testStart() throws Exception {
        Activity activity = getActivity();

    }
    public void setUp() throws Exception {
        Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    // Test for Ok button
    public void okButtonTest() {
        solo.clickOnButton("Ok");

    }

    // Activity finish
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

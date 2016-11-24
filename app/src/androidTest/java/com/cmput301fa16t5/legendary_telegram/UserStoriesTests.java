package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * This is a special class of test cases.
 * It does not test a specific class of code in the app, per say.
 * Rather, this class is meant to test the User Story Requirements that our App should be able
 * to perform.
 *
 * Each test is commented with the User Stories it ensures can be fulfilled.
 *
 * @author kgmills
 */
public class UserStoriesTests extends ActivityInstrumentationTestCase2<LogInActivity>{
    private Solo solo;

    /**
     * Solo clutter to get the ball rolling.
     */
    public UserStoriesTests() {
        super(com.cmput301fa16t5.legendary_telegram.LogInActivity.class);
    }

    /**
     * Solo clutter to get the ball rolling
     */
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Solo setup
     */
    public void setUp() throws Exception {
        Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * This test is focused on the User class functionality.
     * It ensures the following User Story Requirements can be fulfilled:
     * US 01.09.01
     * US 03.01.01
     * US 03.02.01
     * US 03.04.01
     */
    public void testUserFunctionality() {
        solo.clickOnButton("Login");
        solo.waitForText("does not exist");

        solo.enterText((EditText) solo.getView(R.id.userNameET), "Cthulhu_Test");
        solo.clickOnButton("Login");
        solo.waitForText("does not exist");

        solo.clickOnButton("New User");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "Cthulhu_Test");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "wingedTentacleDude@rlyeh.net");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "666666666");
        solo.clickOnButton("Check");
        solo.waitForText("Valid");
        solo.clickOnButton("Create New User");

        solo.assertCurrentActivity("Should be Log In Activity", LogInActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.userNameET));
        solo.enterText((EditText) solo.getView(R.id.userNameET), "Cthulhu_Test");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Should be main request activity", MainRequestActivity.class);
        solo.clickOnButton("Find Requests");
        solo.waitForText("Vehicle Field");
        solo.clickOnButton("Settings");

        solo.assertCurrentActivity("Should be User Profile Activity", UserProfileActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.userNameSettings));
        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "Cthulhu_Driver");
        solo.clickOnButton("Check");
        solo.waitForText("Valid");
        solo.enterText((EditText) solo.getView(R.id.userVehicle), "Soaked Dragon Wings");
        solo.clickOnButton("Edit");
        solo.waitForText("Edited");

        solo.goBack();
        solo.assertCurrentActivity("Should be Main Request Activity", MainRequestActivity.class);
        solo.clickOnButton("Find Requests");

        solo.assertCurrentActivity("Should be Maps Activity", MapsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Main Req Activity", MainRequestActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Log in screen", LogInActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.userNameET));
        solo.enterText((EditText) solo.getView(R.id.userNameET), "Cthulhu_Test");
        solo.clickOnButton("Login");
        solo.waitForText("does not exist");

        solo.clearEditText((EditText) solo.getView(R.id.userNameET));
        solo.enterText((EditText) solo.getView(R.id.userNameET), "Cthulhu_Driver");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Main Request Activity", MainRequestActivity.class);
        solo.goBack();

        Context testContext = InstrumentationRegistry.getTargetContext();
        assertTrue(testContext.deleteFile("Cthulhu_Driver.sav"));

    }


    public void testRiderDriverMain() {
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("User Profile Activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestRider");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "rydah@ggmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "123456789");
        solo.clickOnButton("Create New User");
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);

        solo.clickOnButton("New User");
        solo.assertCurrentActivity("User Profile Activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestDriver");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "dryvah@l33t.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "987654311");
        solo.clickOnButton("Create New User");
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);


    }

}

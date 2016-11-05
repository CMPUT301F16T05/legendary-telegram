package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.support.test.InstrumentationRegistry;

import com.robotium.solo.Solo;

/**
 * Created by keith on 11/4/2016.
 * Unit Tests for the Log In Activity (activity class and controller class)
 * @author kgmills
 */
public class LoginActivityTests extends ActivityInstrumentationTestCase2<LogInActivity> {

    private Solo solo;
    public LoginActivityTests() {
        super(com.cmput301fa16t5.legendary_telegram.LogInActivity.class);
    }

    /**
     * Test Start
     *
     * @throws Exception the exception
     */
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testNewUserButton() {
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("Should be user profile activity", UserProfileActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Should be log in activity", LogInActivity.class);
    }

    public void testFailed() {
        solo.clickOnButton("Login");
        solo.waitForText("does not exist");

        solo.enterText((EditText) solo.getView(R.id.userNameET), "___test___not_a_user");
        solo.clickOnButton("Login");
        solo.waitForText("does not exist");
    }

    public void testSuccess() {

        // Look at me with the dependency injection.
        // It's off the Hindle.
        Context testContext = InstrumentationRegistry.getTargetContext();
        String[] params = {"huhuhuhuhuhuhuhuhuhuhuhuTestCthuhluNobodyPickthis", "2", "3"};
        User testUser = new User(params[0], params[1], params[2]);
        GsonController.saveUserToDisk(testUser, testContext);

        solo.enterText((EditText) solo.getView(R.id.userNameET), params[0]);
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Should be main request activity", MainRequestActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Should be log in activity", LogInActivity.class);
        assertTrue(testContext.deleteFile(params[0] + ".sav"));
    }
}

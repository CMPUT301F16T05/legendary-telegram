package com.cmput301fa16t5.legendary_telegram;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * This is a special class of test cases.
 * It does not test a specific class of code in the app, per say.
 * Rather, this class is meant to test the User Story Requirements that our App should be able
 * to perform.
 *
 * Each test is commented with the User Stories it ensures can be fulfilled.
 *
 * A word of note is that sometimes the tests may slow down, either due to the ESearch algorithm,
 * or because I think Robotium's Solo has a hard time identifying what is a button and what is not
 * with the designs we used. (Aka it thinks a big green square with "Ok" in the middle is a textview,
 * not a button and waits a little before clicking it.
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
        solo.clickOnButton("Edit Profile");

        solo.assertCurrentActivity("Should be User Profile Activity", UserProfileActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.userNameSettings));
        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "Cthulhu_Driver");
        solo.clickOnButton("Check");
        solo.waitForText("Valid");
        solo.enterText((EditText) solo.getView(R.id.userVehicle), "Soaked Dragon Wings");
        solo.clickOnButton("Edit");
        solo.waitForText("Valid Entries. Operation Executed.");

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
    }


    /**
     * This is a large and complex test that tries to run through the process of creating two
     * different users, and processing a request from creation to completion. It may not work completely
     * correctly and usually if it doesn't work completely the request made may still be on the server
     * bugging up attempts to create the request further.
     *
     * It ensures the following User Story Requirements can be fulfilled:
     * US 01.01.01
     * US 01.02.01
     * US 01.03.01
     * US 01.06.01
     * US 01.07.01
     * US 01.08.01
     * US 02.01.01
     * US 03.03.01
     * US 04.01.01
     * US 04.05.01
     * US 05.01.01
     * US 05.02.01
     * US 05.03.01
     * US 05.04.01
     * US 10.01.01
     * US 10.02.01
     *
     *                         *****US 01.05.01*****
     * Implicitly, when you see the contact screen activity, you will see the highlighted text
     * that makes US 01.05.01 Possible. I DID NOT include clicking those links in this test. Why?
     * If you have an email account setup on the Emulator/Phone you're running the tests on, it will
     * take you to an email client, or prompt you as to which email client to use if you have many
     * installed on the device. If you haven't set up email, it will say "Unsupported action". The code
     * to make an email IS THERE if you check ContactScreenController.java, but that message pops up
     * by the Android OS, not the App.
     *
     * In short, I didn't make a test that passed/failed depending on the setup of someone else's
     * Emulator/Phone.
     *
     *                  *******US 08.01/02/03/04.01*******
     * These User Stories are handled by several pieces of App code. Check the source classes,
     * ArrayObserver,
     * Request,
     * MapsActivity,
     * MapController for details.
     *
     * Also see the description of testCancel for further information.
     *
     */
    public void testRiderDriverMain() {

        CentralController myController = CentralController.getInstance();
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("User Profile Activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestRiderDriver");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "rydah@ggmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "123456789");
        solo.enterText((EditText) solo.getView(R.id.userVehicle), "Prototype 2020 Ram 3500");
        solo.clickOnButton("Create New User");
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameET), "TestRiderDriver");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Should be Main Request", MainRequestActivity.class);
        solo.clickOnButton("Make A Request");

        solo.assertCurrentActivity("Should be Maps", MapsActivity.class);

        // Capilano Mall, Edmonton
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "5004 98 Ave Edmonton Alberta");

        // West Edmonton Mall, Edmonton
        solo.enterText((EditText) solo.getView(R.id.EndEditText), "8882 170 NW Edmonton Alberta");

        solo.clickOnText("Search");
        solo.clickOnText("Ok");
        solo.assertCurrentActivity("Main Request Activity", MainRequestActivity.class);

        // We need to get the ID of the request so we actually know what to click on later.
        String id = myController.getCurrentUser().getMyCurrentMode().getIDArray()[0];

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Maps", MapsActivity.class);

        // Searching for Requests beginning in Capilano
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "5004 98 Ave Edmonton Alberta");

        solo.clickOnText("Ok");
        solo.assertCurrentActivity("MainRequest", MainRequestActivity.class);
        solo.clickOnText(id);
        solo.assertCurrentActivity("Contact Screen", ContactScreenActivity.class);
        solo.clickOnButton("Accept Request");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnButton("Make A Request");
        solo.assertCurrentActivity("Should be maps", MapsActivity.class);
        solo.goBack();
        solo.clickOnText(id);

        solo.assertCurrentActivity("Request Status Screen", RequestStatusActivity.class);
        solo.clickOnText("Prototype");
        solo.assertCurrentActivity("Contact Screen", ContactScreenActivity.class);

        solo.clickOnButton("Accept Driver");
        solo.assertCurrentActivity("Request Status", RequestStatusActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Should be Main Request", MainRequestActivity.class);

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Should be maps", MapsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnText(id);
        solo.assertCurrentActivity("Contact Screen", ContactScreenActivity.class);
        solo.clickOnButton("Attempt to Commit");
        solo.waitForText("You've committed");

        solo.assertCurrentActivity("Should be Main Request", MainRequestActivity.class);
        solo.clickOnButton("Make A Request");
        solo.assertCurrentActivity("Should be maps", MapsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Should be main", MainRequestActivity.class);
        solo.clickOnText(id);

        solo.assertCurrentActivity("Request Status Screen", RequestStatusActivity.class);
        solo.clickOnText("Prototype");
        solo.assertCurrentActivity("Contact Screen", ContactScreenActivity.class);

        solo.clickOnButton("Complete Request");
        solo.waitForText("Pay the man.");

        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);
    }

    /**
     * Demonstration of a Request being made and cancelled.
     * US 01.04.01
     *
     * Turn off your internet connection and run this test, to see that it fulfills
     * US 08.02.01 and US 08.03.01
     *
     * Please keep in mind that unless the app has been given a chance to run with Wifi for a bit
     * and download the necessary Map files, you will not see the map.
     *
     */
    public void testCancel() {
        solo.clickOnButton("New User");
        solo.assertCurrentActivity("User Profile Activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestCancel");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "cancel@ggmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "5550123");

        solo.clickOnButton("Create New User");
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameET), "TestCancel");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Should be Main Request", MainRequestActivity.class);
        solo.clickOnButton("Make A Request");

        solo.assertCurrentActivity("Should be Maps", MapsActivity.class);

        // Bonnie Doon Mall, Edmonton
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "160 82 Ave NW Edmonton");

        // Biggest Canadian Tire in Canada :0
        solo.enterText((EditText) solo.getView(R.id.EndEditText), "2110 101 St NW Edmonton");

        solo.clickOnText("Search");
        solo.clickOnText("Ok");
        solo.assertCurrentActivity("Main Request Activity", MainRequestActivity.class);

        solo.clickOnText("No Accepting");
        solo.assertCurrentActivity("Should be contact screen activity.", RequestStatusActivity.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);

    }

    /**
     * US 04.03.01
     * US 04.04.01
     *
     * US 04.02.01 - Would do but can't find a way to get Solo to enter text into pop-up...
     * See https://archive.org/details/LTPromotional
     */
    public void testSearchFilters() {

        CentralController myController = CentralController.getInstance();

        solo.clickOnButton("New User");
        solo.assertCurrentActivity("User Profile Activity", UserProfileActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameSettings), "TestFilters");
        solo.enterText((EditText) solo.getView(R.id.userEmail), "filters@ggmail.com");
        solo.enterText((EditText) solo.getView(R.id.userPhone), "5550123");
        solo.enterText((EditText) solo.getView(R.id.userVehicle), "Blah Blah");

        solo.clickOnButton("Create New User");
        solo.assertCurrentActivity("Log In Screen", LogInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.userNameET), "TestFilters");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Should be Main Request", MainRequestActivity.class);
        solo.clickOnButton("Make A Request");
        solo.assertCurrentActivity("Maps", MapsActivity.class);

        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.enterText((EditText) solo.getView(R.id.EndEditText), "Athabasca Canada");
        solo.clickOnText("Search");
        solo.clickOnText("Ok");
        solo.assertCurrentActivity("Should be main request", MainRequestActivity.class);

        solo.clickOnButton("Make A Request");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.enterText((EditText) solo.getView(R.id.EndEditText), "Fort McMurray Canada");
        solo.clickOnText("Search");
        solo.clickOnText("Ok");

        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        String id1 = myController.getCurrentUser().getMyCurrentMode().getIDArray()[0];
        String id2 = myController.getCurrentUser().getMyCurrentMode().getIDArray()[1];

        ArrayList<Request> req = myController.getCurrentUser().getMyCurrentMode().getOpenRequests();
        Double cost0Upper = req.get(0).getFeePerKM() * 1.01;
        Double cost0Lower = req.get(0).getFeePerKM() * 0.99;
        Double cost1Upper = req.get(1).getFeePerKM() * 1.01;
        Double cost1Lower = req.get(1).getFeePerKM() * 0.99;

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.clickOnText("Filter");

        solo.assertCurrentActivity("Filters", FilterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.MaxET), "300");
        solo.enterText((EditText) solo.getView(R.id.MinET), "100");
        solo.clickOnButton("Filter And Search");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.clickOnText("Filter");

        solo.assertCurrentActivity("Filters", FilterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.MaxET), "500");
        solo.enterText((EditText) solo.getView(R.id.MinET), "200");
        solo.clickOnButton("Filter And Search");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.clickOnText("Filter");

        solo.assertCurrentActivity("Filters", FilterActivity.class);
        solo.clickOnText("KM");
        solo.enterText((EditText) solo.getView(R.id.MaxET), cost0Upper.toString());
        solo.enterText((EditText) solo.getView(R.id.MinET), cost0Lower.toString());
        solo.clickOnButton("Filter And Search");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnButton("Find Requests");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.StartEditText), "Edmonton Canada");
        solo.clickOnText("Filter");

        solo.assertCurrentActivity("Filters", FilterActivity.class);
        solo.clickOnText("KM");
        solo.enterText((EditText) solo.getView(R.id.MaxET), cost1Upper.toString());
        solo.enterText((EditText) solo.getView(R.id.MinET), cost1Lower.toString());
        solo.clickOnButton("Filter And Search");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnText("Make A Request");
        solo.assertCurrentActivity("Maps", MapsActivity.class);
        solo.goBack();

        solo.clickOnText(id1);
        solo.assertCurrentActivity("Request Status", RequestStatusActivity.class);
        solo.clickOnText("Cancel");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        solo.clickOnText(id2);
        solo.assertCurrentActivity("Request Status", RequestStatusActivity.class);
        solo.clickOnText("Cancel");
        solo.assertCurrentActivity("Main Request", MainRequestActivity.class);

        // Edmonton Athabasca is about $123
        // Edmonton Ft McMurray is about $457
    }

    public void tearDown() throws Exception {
        Context testContext = InstrumentationRegistry.getTargetContext();
        testContext.deleteFile("Cthulhu_Driver.sav");
        testContext.deleteFile("Cthulhu_Test.sav");
        testContext.deleteFile("TestRiderDriver.sav");
        testContext.deleteFile("TestCancel.sav");
        testContext.deleteFile("TestOffline.sav");
        testContext.deleteFile("TestFilters.sav");

    }

}

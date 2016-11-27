package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/7.
 * Test the methods in the Rider Class
 * @author Chuan
 */

public class RiderTests {

    @Test
    public void testCreateNewRequest(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Rider testRider = new Rider();
        testRider.createNewRequest(me , start, end);

        // Check the parameters in the OpenRequest are the same as what we saved
        assertEquals(testRider.getOpenRequests().get(0).getStartLocation(), start);
        assertEquals(testRider.getOpenRequests().get(0).getEndLocation(), end);
    }

    @Test
    public void testRemoveOrComplete(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Rider testRider = new Rider();
        testRider.createNewRequest(me , start, end);
        testRider.setCurrentRequest(0);
        Request testRequest = testRider.getCurrentRequest();

        //Check the return value
        assertEquals(testRequest, testRider.removeOrComplete());

        // Check if the request is saved into the OpenRequest
        assertFalse(testRider.getOpenRequests().contains(testRequest));
    }

    @Test
    public void testSelectDriver(){
        IdentificationCard driver1 = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard driver2 = new IdentificationCard("b", "b@test.com", "12345678901");
        IdentificationCard me = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);

        Rider testRider = new Rider();
        testRider.createNewRequest(me, start, end);
        testRider.setCurrentRequest(0);
        testRider.getCurrentRequest().addADriver(driver1);
        testRider.getCurrentRequest().addADriver(driver2);
        testRider.selectDriver(0);


        // Make sure after we call the selectDriver
        //CurrentRequest.myDriver = the picked driver
        assertEquals(testRider.getCurrentRequest().getMyDriver(), driver1);

        //CurrentRequest's state changes
        assertEquals(testRider.getCurrentRequest().getState(), RequestEnum.acceptedADriver);

        //The state of if it is on server changes
        assertEquals(testRider.getCurrentRequest().isOnServer(), false);
    }

}

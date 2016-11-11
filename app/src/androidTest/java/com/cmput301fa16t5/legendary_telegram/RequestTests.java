package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/5.
 */

public class RequestTests {
    //Need to be implemented
    @Test
    public void testAddADriver(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard driver = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Request request = new Request(me, start, end);

        request.addADriver(driver);

        assertTrue(request.getPotentialDrivers().contains(driver));
        assertEquals(RequestEnum.hasADriver, request.getState());
        assertFalse(request.isOnServer());
    }

    @Test
    public void testAcceptADriver(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard driver = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Request request = new Request(me, start, end);
        request.addADriver(driver);

        request.acceptADriver(0);

        assertEquals(driver, request.getPotentialDrivers().get(0));
        assertEquals(RequestEnum.acceptedADriver, request.getState());
        assertFalse(request.isOnServer());
    }

    @Test
    public void testCheckCommittedDriver(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard driver = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Request request = new Request(me, start, end);

        assertFalse(request.checkCommittedDriver(driver));

        // Test for the iIsThisMe is the same as what in the Driver Test
    }

    @Test
    public void testCommitToRequest(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Request request = new Request(me, start, end);

        request.commitToRequest();

        assertEquals(RequestEnum.driverHasCommitted, request.getState());
    }

}

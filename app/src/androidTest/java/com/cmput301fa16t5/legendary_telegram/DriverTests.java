package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by idrea on 2016/11/10.
 */

public class DriverTests {

    @Test
    public void testAcceptARequest(){
        ArrayList<Request> requests = new ArrayList<Request>();
        IdentificationCard rider1 = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard rider2 = new IdentificationCard("b", "b@test.com", "12345678901");
        IdentificationCard me = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        requests.add(new Request(rider1, start, end));
        requests.add(new Request(rider2, start, end));

        Driver driver = new Driver();
        driver.setOpenRequests(requests);
        driver.acceptARequest(0, me);

        assertEquals(driver.getCurrentRequest(), driver.getOpenRequests().get(0));
        assertTrue(driver.getCurrentRequest().getPotentialDrivers().contains(me));
        assertFalse(driver.getCurrentRequest().isOnServer());
    }

    @Test
    public void testCheckIfPicked(){
        IdentificationCard rider1 = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard me = new IdentificationCard("aa", "aa@test.com", "11234567890");
        IdentificationCard driver1 = new IdentificationCard("aaa", "aaa@test.com", "111234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        ArrayList<Request> requests = new ArrayList<Request>();
        requests.add(new Request(rider1, start, end));
        Driver driver = new Driver();
        Driver OtherDriver = new Driver();
        // Check if the driver is picker (return true)
        driver.setOpenRequests(requests);
        driver.acceptARequest(0, me);
        requests.get(0).acceptADriver(0);

        assertTrue(driver.checkIfPicked());

        // Check if the driver is not picker (return false)
        requests.add(new Request(rider1, start, end));
        driver.setOpenRequests(requests);
        driver.acceptARequest(1, me);
        OtherDriver.setOpenRequests(requests);
        OtherDriver.acceptARequest(1, driver1);
        requests.get(1).acceptADriver(1);

        assertFalse(driver.checkIfPicked());
    }


    @Test
    public void testCommit(){
        IdentificationCard rider1 = new IdentificationCard("a", "a@test.com", "1234567890");
        IdentificationCard me = new IdentificationCard("aa", "aa@test.com", "11234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        ArrayList<Request> requests = new ArrayList<Request>();
        requests.add(new Request(rider1, start, end));
        Driver driver = new Driver();
        driver.setOpenRequests(requests);
        driver.acceptARequest(0, me);
        requests.get(0).addADriver(me);

        driver.commit();

        assertEquals(driver.getCurrentRequest().getState(), RequestEnum.driverHasCommitted);
        assertFalse(driver.getCurrentRequest().isOnServer());
    }
}

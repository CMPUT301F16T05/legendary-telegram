package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/7.
 */

public class RiderTests {

    @Test
    public void testCreateNewRequest(){
        IdentificationCard me = new IdentificationCard("a", "a@test.com", "1234567890");
        LatLng start = new LatLng(0, 0);
        LatLng end = new LatLng(1,1);
        Rider testRider = new Rider();
        testRider.createNewRequest(me , start, end);

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

        assertEquals(testRequest.getId(), testRider.removeOrComplete());
        assertFalse(testRider.getOpenRequests().contains(testRequest));
    }

}

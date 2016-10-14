package com.example.tom.rider;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Tom and Daniel on 10/12/16.
 */

public class RiderTest extends ActivityInstrumentationTestCase2 {
    public RiderTest(){
        super(com.example.tom.rider.MainActivity.class);
    }

    public void requestListTest() {
        ArrayList<Request> requests = new requestList(); 
        Request request = new Request(); 
        requests.add(request);

        assertTrue(requests.contains(request));
    }

    public void openRequestTest() {
        Request request = new Request(); 
        request.setOpen(true); 
        assertTrue(request.isOpen()); 

        request.setOpen(false); 
        assertFalse(request.isOpen());
    }

    public void driverAcceptRequestTest() {
        Request request = new Request(); 
        request.setDriverAccept(true); 
        assertTrue(request.isDriverAccept()); 

        request.setDriverAccept(false); 
        assertFalse(request.isDriverAccept());
    }

    public void riderCancelRequestTest() {
        Request request = new Request(); 
        request.setRiderCancel(true); 
        assertTrue(request.isRiderCancel()); 

        request.setRiderCancel(false); 
        assertFalse(request.isRiderCancel());
    }

    public void getContactInfoTest() {
        Driver driver = new Driver(); 
        driver.setEmail("test@test.com");
        assertEquals(driver.getEmail(), "test@test.com"); 

        driver.setPhone("1234567890");
        assertEquals(driver.getPhone(), "1234567890"); 
    }

    public void getFareTest() {
        Request request = new Request();
        request.setFare(100); 
        assertEquals(request.getFare(), 100); 
    }

    public void riderConfirmCompletionTest() {
        Request request = new Request();
        request.setRiderConfirmCompletion(true); 
        assertTrue(request.getRiderConfirmCompletion());
        assertTrue(request.getEnablePayment());
    }

    public void riderConfirmAcceptanceTest() {
        Request request = new Request();
        Driver driver = new Driver();
        request.addDriver(driver);
        assertEquals(request.getDriver(), driver)
    }
}
package com.example.tom.rider;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Tom and Daniel on 10/12/16.
 */

public class UserProfileTest extends ActivityInstrumentationTestCase2 {
    public UserProfileTest(){
        super(com.example.tom.rider.MainActivity.class);
    }

    public void addUserTest(){
        ArrayList<User> list = new UserList();
        User user = new User(); 

        assertFalse(list.contains(user)); 
        list.add(user); 
        
        try {
            list.add(user);
            fail();
        } catch (IllegalArgumentException e){
            System.out.print("pass");
        }
        assertTrue(list.contains(user));

    }

    public void editContactInfo() {
        User user = new User(); 
        user.setName("TEST");
        user.setEmail("test@test.com");
        user.setPhone("1234567890");

        assertEquals(user.getName(), "TEST");
        assertEquals(user.getEmail(), "test@test.com");
        assertEquals(user.getPhone(), "1234567890");
    }
}
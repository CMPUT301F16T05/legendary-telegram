package com.cmput301fa16t5.legendary_telegram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/5.
 */

public class UserTests {

//    @Test
//    public void testShowInfo(){
//        User user = new User("aaaa", "a@gmail.com", "1234567890");
//        assertEquals(user.showInfo(), user.getUserName() + ":\n" + user.getTelephone() + " " + user.getEmail());
//    }
//
//    @Test
//    public void testSaveChanges(){
//        User user = new User("aaaa", "a@gmail.com", "1234567890");
//        String oldInfo = user.showInfo();
//        User NewUser1 = new User("aaaa", "b@gmail.com", "11234567890");
//        user.saveChanges(NewUser1);
//        String newInfo = user.showInfo();
//        assertEquals(oldInfo,newInfo);
//
//        User NewUser2 = new User("bbbbb", "a@gmail.com", "1234567890");
//        // TO DO
//    }

    @Test
    public void testRiderDriverSwitch() {

        User testUser = new User(null, null, null);
        testUser.setMyDriver(new Driver());

        assertFalse(testUser.askForMode());

        testUser.setAsRider();
        assertFalse(testUser.askForMode());

        testUser.setAsDriver();
        assertTrue(testUser.askForMode());


    }
}

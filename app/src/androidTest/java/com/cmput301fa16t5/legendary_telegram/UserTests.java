package com.cmput301fa16t5.legendary_telegram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/5.
 */

public class UserTests {

    @Test
    public void testShowInfo(){
        User user = new User("aaaa", "a@gmail.com", "1234567890");
        assertEquals(user.showInfo(), user.getUserName() + ":\n" + user.getTelephone() + " " + user.getEmail());
   }

    @Test
    public void testRiderDriverSwitch() {

        User testUser = new User(null, null, null);
        testUser.setAsDriver();

        assertFalse(testUser.askForMode());

        testUser.setAsRider();
        assertFalse(testUser.askForMode());

        testUser.setAsDriver();
        assertTrue(testUser.askForMode());


    }
}

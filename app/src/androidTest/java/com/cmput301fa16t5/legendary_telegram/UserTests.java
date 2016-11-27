package com.cmput301fa16t5.legendary_telegram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by idrea on 2016/11/5.
 * Tests to the User Class, not user stories in general.
 * @author Chuan
 */

public class UserTests {

    // Check if the "toString" works
    @Test
    public void testShowInfo(){
        User user = new User("aaaa", "a@gmail.com", "1234567890");
        assertEquals(user.showInfo(), user.getUserName() + ":\n" + user.getTelephone() + " " + user.getEmail());
   }

    // Test if the Use mode can be switched
    @Test
    public void testRiderDriverSwitch() {

        User testUser = new User(null, null, null);
        testUser.setAsDriver();

        assertTrue(testUser.askForMode());

        testUser.setAsRider();
        assertFalse(testUser.askForMode());

        testUser.setAsDriver();
        assertTrue(testUser.askForMode());

    }
}

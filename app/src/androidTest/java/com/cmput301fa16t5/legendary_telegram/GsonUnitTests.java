package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by keith on 11/4/2016.
 * GsonController Unit Tests.
 * @author kgmills
 */
public class GsonUnitTests {

    /**
     * If the tests fail you must go into the device manager and delete
     * testUser.sav and testLoad.sav from memory.
     */
    @Test
    public void testIfExists() {

        Context testContext = InstrumentationRegistry.getTargetContext();

        assertFalse(GsonController.checkIfExists("testUserName0", testContext));
        assertFalse(GsonController.checkIfExists("testUserName1.sav", testContext));

        String testName = "testUser";
        User testUser = new User(testName, "testEmail", "testPhone");

        GsonController.saveUserToDisk(testUser, testContext);
        assertTrue(GsonController.checkIfExists(testName, testContext));
        assertTrue(testContext.deleteFile(testName + ".sav"));
    }

    @Test
    public void testLoad() {

        Context testContext = InstrumentationRegistry.getTargetContext();
        String[] testParams = {"testLoad", "loadEmail", "loadPhone"};

        assertFalse(GsonController.checkIfExists(testParams[0], testContext));

        GsonController.saveUserToDisk(new User(testParams[0], testParams[1], testParams[2]),
                testContext);
        assertTrue(GsonController.checkIfExists(testParams[0], testContext));

        User testUser = GsonController.loadUserInfo(testParams[0], testContext);

        assertEquals(testParams[0], testUser.getUserName());
        assertEquals(testParams[1], testUser.getEmail());
        assertEquals(testParams[2], testUser.getTelephone());

        assertTrue(testContext.deleteFile(testParams[0] + ".sav"));
    }
}

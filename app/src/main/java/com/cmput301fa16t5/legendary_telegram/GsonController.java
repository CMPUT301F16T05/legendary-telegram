package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 *
 * It's the GsonController.
 * It's accessed typically by the CentralController only.
 * Saves and loads the user information to disk.
 *
 * PLEASE CHECK TTHE EXAMPLE FUNCTION TO SEE OUR SYNTAX FOR SAVING FILES TO DISK.
 *
 */

public class GsonController {

    // Example of how we would check to see if an entered username actually exists on disk.
    public boolean checkIfExists(String userName) {
        String fileName = userName + ".sav";
        //Check if any file named "fileName" actually exists on disk.
        // If so, the user profile exists.
        // If not, it doesn't.

        return false;
    }
}

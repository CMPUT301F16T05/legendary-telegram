package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 */

public class UserProfileController {

    private CentralController centralCommand;

    public UserProfileController() {
        centralCommand = CentralController.getInstance();
    }
    public User GetCurrentUser(){
        return centralCommand.getCurrentUser();
    }
}

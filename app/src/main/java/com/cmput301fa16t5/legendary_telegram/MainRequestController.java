package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 * Corresponds to MainRequestActivity
 */

public class MainRequestController {

    private CentralController centralCommand;
    private User currentUser;

    public MainRequestController() {

        centralCommand = CentralController.getInstance();
    }

    public void setUserAsDriver(){
        currentUser = centralCommand.getCurrentUser();
        currentUser.setAsDriver();
    }

    public void setUserAsRider(){
        currentUser = centralCommand.getCurrentUser();
        currentUser.setAsRider();
    }
}

package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 * Corresponds to MainRequestActivity
 */

public class MainRequestController {

    private CentralController centralCommand;

    public MainRequestController() {

        centralCommand = CentralController.getInstance();
    }
    //set the user as driver
    public void setUserAsDriver(){
        centralCommand.setUserDriver();
    }
    //set the user as rider
    public void setUserAsRider(){
        centralCommand.setUserRider();
    }
}

package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 * See ContactScreenActivity.
 *
 * The guts of said activity, like where a function is called to initiate a phone call, go here.
 */

public class ContactScreenController {

    private CentralController centralCommand;

    public ContactScreenController() {

        centralCommand = CentralController.getInstance();
    }
}

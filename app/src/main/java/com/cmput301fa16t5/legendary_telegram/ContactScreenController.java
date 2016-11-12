package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 * See ContactScreenActivity.
 *
 * The guts of said activity, like where a function is called to initiate a phone call, go here.
 */

public class ContactScreenController {

    private CentralController centralCommand;
    private Request requestOfFocus;

    public ContactScreenController() {

        centralCommand = CentralController.getInstance();
        if (centralCommand.getCurrentUser().askForMode()) {
            this.requestOfFocus = centralCommand.getCurrentUser().getMyDriver().getCurrentRequest();
        }

        else {
            this.requestOfFocus = centralCommand.getCurrentUser().getMyRider().getCurrentRequest();
        }
    }

    public boolean riderOrDriver() {
        return centralCommand.getCurrentUser().askForMode();
    }

    public String[] getEntries() {

        String[] data = new String[4];
        IdentificationCard info = centralCommand.getCurrentUser()
                .getMyDriver().getCurrentRequest().getMyRider();

        data[0] = centralCommand.getCurrentUser().getMyDriver().getCurrentRequest().getId();
        data[1] = info.getName();
        data[2] = info.getPhone();
        data[3] = info.getEmail();
        return data;
    }

    public String setDriverSelectText() {
        if (userAcceptedADriver()) {

        }
        return null;
    }

    public boolean userAcceptedADriver() {
        if (requestOfFocus.getState() == RequestEnum.acceptedADriver) {
            return true;
        }
        return false;
    }
}

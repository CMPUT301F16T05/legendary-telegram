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
    private boolean isDriverOrRider;
    private int index;

    /**
     * Constructor.
     * Determines if it's a Rider or Driver User who accessed this.
     * Gets the appropriate request.
     */
    public ContactScreenController() {

        centralCommand = CentralController.getInstance();
        if (centralCommand.getCurrentUser().askForMode()) {
            isDriverOrRider = true;
            this.requestOfFocus = centralCommand.getCurrentUser().getMyDriver().getCurrentRequest();
        }

        else {
            isDriverOrRider = false;
            this.requestOfFocus = centralCommand.getCurrentUser().getMyRider().getCurrentRequest();
        }
    }

    /**
     * Called when the activity starts to get the information fill it's TextViews.
     * @return String[] containing Request ID, User name, phone, email.
     */
    public String[] getEntries() {

        String[] data = new String[4];
        IdentificationCard info;
        if (isDriverOrRider()) {
            info = requestOfFocus.getMyRider();
        }

        else {
            info = requestOfFocus.getPotentialDrivers().get(index);
        }

        data[0] = requestOfFocus.getId();
        data[1] = info.getName();
        data[2] = info.getPhone();
        data[3] = info.getEmail();
        return data;
    }

    public boolean isDriverOrRider() {
        return isDriverOrRider;
    }

    /**
     * In the Rider case it must know the index of the ID card to parse.
     * @param i
     */
    public void getIndex(String i) {
        this.index = Integer.parseInt(i);
    }

    /**
     * Determines the text of the button on the bottom of the screen.
     * @return A String (or Null, if they already have a committed Driver) to post as text.
     */
    public String setButtonText() {
        if (isDriverOrRider()) {
            if (requestOfFocus.getState() == RequestEnum.acceptedADriver) {
                return "Attempt to Commit";
            }

            else if (requestOfFocus.getState() != RequestEnum.driverHasCommitted) {
                return "Accept Request";
            }

            return null;
        }

        return "Accept Driver";
    }

    /**
     * Called when the button is actually pressed. Since we're modifying the Request now, we
     * have to save the User.
     * @return String that will be Toasted.
     */
    public String commitPress() {
        if (isDriverOrRider()) {
            if (requestOfFocus.getState() == RequestEnum.acceptedADriver) {
                if (centralCommand.getCurrentUser().getMyDriver().checkIfPicked()) {
                    requestOfFocus.commitToRequest();
                    centralCommand.updateRequest(requestOfFocus);
                    centralCommand.saveCurrentUser();
                    return "You've committed to this request. Get going!";
                }
                return "Rider has picked a different Driver";
            }

            else if (requestOfFocus.getState() != RequestEnum.driverHasCommitted) {
                requestOfFocus.addADriver(centralCommand.generateDriverCard());
                centralCommand.updateRequest(requestOfFocus);
                centralCommand.saveCurrentUser();
                return "You've accepted this Request. Wait to see if you are selected.";
            }

            return "Rider has committed Driver already.";
        }

        else {
            requestOfFocus.acceptADriver(index);
            centralCommand.updateRequest(requestOfFocus);
            centralCommand.saveCurrentUser();
            return "Driver Accepted.";
        }
    }
}

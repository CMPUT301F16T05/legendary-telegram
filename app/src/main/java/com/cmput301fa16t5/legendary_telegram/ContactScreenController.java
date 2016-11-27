package com.cmput301fa16t5.legendary_telegram;

/**
 * Contact Screen Controller - Retrieves data for the activity and does logical computations.
 * Separation of where/how info is displayed and what is to be displayed.
 *
 * Not an official pattern, but an implementation of Abram's "OO Butterknife"
 *
 * The guts of said activity, like where a function is called to initiate a phone call, go here.
 * This class, more than others, needs serious refactoring.
 */

public class ContactScreenController {

    private final CentralController centralCommand;
    private final Request requestOfFocus;
    private final boolean isDriverOrRider;
    private int index;

    /**
     * Constructor.
     * Determines if it's a Rider or Driver User who accessed this.
     * Gets the appropriate request.
     */
    public ContactScreenController() {

        centralCommand = CentralController.getInstance();
        this.isDriverOrRider = centralCommand.userMode();
        this.requestOfFocus = centralCommand.requestToShow();
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

    /**
     * Getter for the Driver/Rider boolean for the Activity.
     * @return True if Driver, false if Rider.
     */
    public boolean isDriverOrRider() {
        return isDriverOrRider;
    }

    /**
     * In the Rider case it must know the index of the ID card to parse.
     * @param i Index of the ID in the list
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
            if (requestOfFocus.getState().equals(RequestEnum.acceptedADriver)) {
                return "Attempt to Commit";
            }

            else if (!requestOfFocus.getState().equals(RequestEnum.driverHasCommitted)) {
                return "Accept Request";
            }

            return null;
        }

        else if (requestOfFocus.getState().equals(RequestEnum.driverHasCommitted)) {
            return "Complete Request";
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
            if (requestOfFocus.getState().equals(RequestEnum.acceptedADriver)) {
                if (centralCommand.driverPickCheck()) {
                    requestOfFocus.commitToRequest();
                    centralCommand.updateRequest(requestOfFocus);
                    centralCommand.saveCurrentUser();
                    return "You've committed to this request. Get going!";
                }
                return "Rider has picked a different Driver";
            }

            else if (!requestOfFocus.getState().equals(RequestEnum.driverHasCommitted)) {
                centralCommand.driverAcceptCurrent();
                centralCommand.updateRequest(requestOfFocus);
                centralCommand.saveCurrentUser();
                return "You've accepted this Request. Wait to see if you are selected.";
            }

            return "Rider has committed Driver already.";
        }

        else if (requestOfFocus.getState().equals(RequestEnum.driverHasCommitted)) {

            centralCommand.deleteCurrentRiderRequest();
            centralCommand.setShouldStatusGoBack(true);
            centralCommand.saveCurrentUser();
            return "Request Completed. Pay the man.";
        }

        else {
            requestOfFocus.acceptADriver(index);
            centralCommand.updateRequest(requestOfFocus);
            centralCommand.saveCurrentUser();
            return "Driver Accepted.";
        }
    }

    /**
     * Getter method, which helps ContactScreenActivity to get the Latitude and Longtitude of start and end points.
     * @return request
     * zhimao
     */
    public Request getRequestOfFocus() {
        return requestOfFocus;
    }
}

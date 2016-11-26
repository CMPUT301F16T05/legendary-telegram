package com.cmput301fa16t5.legendary_telegram;

import android.widget.ArrayAdapter;

/**
 * Created by keith on 11/2/2016.
 * Controller for the Request Status screen.
 */
public class RequestStatusController {

    private final CentralController centralCommand;

    public RequestStatusController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Get the ID of a request to display.
     * @return A string containing the ID.
     */
    public String getRequestName() {
        try {
            return centralCommand.requestToShow().getId();
        }
        catch (NullPointerException e) {
            centralCommand.pingTheServer();
            return "Request Failed to Load. Restart App.";
        }
    }

    /**
     * Sets the ArrayAdapter with appropriate information.
     * @param activity: Needed because Android.
     * @return Set ArrayAdapter.
     */
    public ArrayAdapter<IdentificationCard> setRequestAdapter(RequestStatusActivity activity) {
        ArrayAdapter<IdentificationCard> adapt = new ArrayAdapter<>(activity, R.layout.card_items,
                centralCommand.getCards());
        centralCommand.addArrayAdapter(adapt);
        return adapt;
    }

    /**
     * Cancels Request. Tells ESearchController to delete. Saves to disk.
     */
    public void cancel() {
        centralCommand.deleteCurrentRiderRequest();
        centralCommand.saveCurrentUser();
    }

    public void removeAdapter(ArrayAdapter adapt) {
        centralCommand.removeArrayAdapter(adapt);
    }

    /**
     * Called by activity to determine if it should go back onResume. True whenever a request is
     * completed.
     * @return Boolean from CentralController.
     */
    public boolean shouldFinish() {
        return centralCommand.isShouldStatusGoBack();
    }

    /**
     * We go back but we reset the boolean to false so that we can get back to this activity.
     */
    public void resetFinish() {
        centralCommand.setShouldStatusGoBack(false);
    }
}

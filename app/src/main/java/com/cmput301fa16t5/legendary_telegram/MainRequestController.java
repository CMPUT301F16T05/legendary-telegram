package com.cmput301fa16t5.legendary_telegram;

import android.widget.ArrayAdapter;

/**
 * Created by keith on 11/2/2016.
 * Corresponds to MainRequestActivity
 */
public class MainRequestController {

    private final CentralController centralCommand;

    public MainRequestController() {
        centralCommand = CentralController.getInstance();
        centralCommand.pingTheServer();
    }

    /**
     * Sets user as Driver and saves to disk.
     * @return True if the User can be a Driver, false otherwise
     */
    public boolean setUserAsDriver(){
        centralCommand.pingTheServer();
        return centralCommand.canBeDriver();
    }

    /**
     * Sets user as Rider and saves to disk
     */
    public void setUserAsRider(){
        centralCommand.pingTheServer();
        centralCommand.setUserRider();
    }

    /**
     * Sets the ArrayAdapter to show the appropriate requests.
     * @param activity: Needed to set command.
     * @return The set ArrayAdapter
     */
    public ArrayAdapter<Request> setRequestAdapter(MainRequestActivity activity) {
        ArrayAdapter<Request> adapt = new ArrayAdapter<>(activity, R.layout.request_items, centralCommand.getRequests());
        centralCommand.addArrayAdapter(adapt);
        return adapt;
    }

    /**
     * Tells the central controller which request we clicked.
     * @param position Position of the Request.
     * @return True if request was clicked as Driver. False if Rider.
     */
    public boolean clickedARequest(int position) {
        return centralCommand.selectCurrentRequest(position);
    }

    public void removeAdapter(ArrayAdapter<Request> adapt) {
        centralCommand.removeArrayAdapter(adapt);
    }
}

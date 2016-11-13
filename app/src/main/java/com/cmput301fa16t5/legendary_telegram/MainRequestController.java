package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by keith on 11/2/2016.
 * Corresponds to MainRequestActivity
 */
public class MainRequestController {

    private CentralController centralCommand;

    public MainRequestController() {

        centralCommand = CentralController.getInstance();
    }

    /**
     * Sets user as Driver and saves to disk.
     * @param context Needed for GSON.
     * @return
     */
    public boolean setUserAsDriver(Context context){
        return centralCommand.canBeDriver(context);
    }

    /**
     * Sets user as Rider and saves to disk
     * @param context Needed for Gson.
     */
    public void setUserAsRider(Context context){
        centralCommand.setUserRider(context);
    }

    /**
     * Sets the ArrayAdapter to show the appropriate requests.
     * @param activity: Needed to set command.
     * @return: The set ArrayAdapter.
     */
    public ArrayAdapter<Request> setRequestAdapter(MainRequestActivity activity) {
        return new ArrayAdapter(activity, R.layout.request_items, centralCommand.getRequests());
    }

    /**
     * Tells the central controller which request we clicked.
     * @param position Position of the Request.
     * @return True if request was clicked as Driver. False if Rider.
     */
    public boolean clickedARequest(int position) {
        return centralCommand.selectCurrentRequest(position);
    }
}

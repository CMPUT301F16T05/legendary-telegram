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
    //set the user as driver
    public boolean setUserAsDriver(Context context){
        return centralCommand.canBeDriver(context);
    }
    //set the user as rider
    public void setUserAsRider(){
        centralCommand.setUserRider();
    }

    public ArrayAdapter<Request> setRequestAdapter(MainRequestActivity activity) {
        return new ArrayAdapter(activity, R.layout.request_items, centralCommand.getRequests());
    }

    public boolean clickedARequest(int position) {
        return centralCommand.selectCurrentRequest(position);
    }

}

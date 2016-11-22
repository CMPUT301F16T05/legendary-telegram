package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/9/2016.
 * This class exists for the purpose
 * of using Java reflection in user
 * to determine if we're acting
 * as a Rider or Driver.
 * I wanted this class to be ABSTRACT. However, adding that it makes the app crash when Gson tries
 * to create an object of it for User.
 */
public class RiderDriverParent {
    protected ArrayList<Request> openRequests;
    protected Request currentRequest;

    public RiderDriverParent() {    }

    /**
     * Sets a request from a list of Requests.
     * @param index: The index of the request to focus on.
     */
    public void setCurrentRequest(Integer index) {
        this.currentRequest = openRequests.get(index);
    }

    /**
     * Getter for current Request
     * @return A request object
     */
    public Request getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Getter for the open requests to be displayed.
     * @return An ArrayList of Requests
     */
    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    /**
     * Called by user when it gets a list from ESearch.
     * @param openRequests Request ArrayList
     */
    public void setOpenRequests(ArrayList<Request> openRequests) {
        this.openRequests = openRequests;
    }

    /**
     * Gets the IDs of all Open Requests.
     * Used by ArrayObserver for ElasticSearch update downloads.
     * @return A String[] containing the IDs of all requests.
     */
    public String[] getIDArray() {
        String[] myIDs = new String[this.openRequests.size()];

        for (int i = 0; i < this.openRequests.size(); i++) {
            myIDs[i] = this.openRequests.get(i).getId();
        }
        return myIDs;
    }
}

package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider.
 */

public class Rider {

    private Request currentRequest;
    private ArrayList<Request> openRequests;
    // No RequestList, just an arraylist.

    // Constructor
    public Rider() {
        openRequests = new ArrayList<Request>();
    }

    // Add a request
    public void addRequest(Request newRequest) {
        openRequests.add(newRequest);
    }

}

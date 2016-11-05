package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Driver
 */

public class Driver {

    private Request acceptedRequest;
    private ArrayList<Request> acceptedRequests;

    // Constructor
    public Driver() {
        acceptedRequests = new ArrayList<Request>();
    }

    public void addRequest(Request newRequest) {
        acceptedRequests.add(newRequest);
    }
}

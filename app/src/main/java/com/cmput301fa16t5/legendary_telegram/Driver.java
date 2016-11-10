package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Driver
 */

/**
 *  This class is used to record the request that the user accepted as a driver
 *  User can accept, get the infomation for his accepted request
 */

public class Driver {

    private User user;
    private Request acceptedRequest;
    private ArrayList<Request> acceptedRequests;

    // Constructor
    public Driver(User user) {
        this. user = user;
        acceptedRequests = new ArrayList<Request>();
    }

    public Request getAcceptedRequest() {
        return acceptedRequest;
    }

    public void addRequest(Request newRequest) {
        acceptedRequests.add(newRequest);
    }

    public void setAcceptedRequest(Request request){
        this.acceptedRequest = request;
    }
}

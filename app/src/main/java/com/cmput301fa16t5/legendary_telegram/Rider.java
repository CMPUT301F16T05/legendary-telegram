package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider.
 */

/**
 *  This class is used to record the request that the user opened as a rider
 *  User can open, cancel and get the infomation for his current request
 */

public class Rider {

    private User user;
    public Request currentRequest;
    public ArrayList<Request> openRequests;
    // No RequestList, just an arraylist.

    // Constructor
    public Rider(User user) {
        this.user = user;
        openRequests = new ArrayList<Request>();
    }

    // Add a request
    public void addRequest(Request newRequest) {
        openRequests.add(newRequest);
        this.currentRequest = newRequest;
        currentRequest.setCurrentState("Open");
    }

    public Request showCurrentRequest(){
        return currentRequest;
    }

    public void cancelRequest(Request newRequest){
        openRequests.remove(newRequest);
        currentRequest.setCurrentState("Cancelled");
        //should add the nofications to the drivers ??
    }

    public void pickDriver(User user){
        currentRequest.setFinalDriver(user);
        currentRequest.setCurrentState("Working");
        currentRequest.getFinalDriver().setAcceptedRequest(currentRequest);
    }

}

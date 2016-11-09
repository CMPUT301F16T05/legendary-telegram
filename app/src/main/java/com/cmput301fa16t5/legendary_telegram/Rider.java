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


    public Request currentRequest;
    public ArrayList<Request> openRequests;
    // No RequestList, just an arraylist.

    // Constructor
    public Rider() {
        openRequests = new ArrayList<Request>();
    }

    // Add a request
    public void addRequest(Request newRequest) {
        openRequests.add(newRequest);
    }

    public Request showCurrentRequest(){
        return currentRequest;
    }

    public void cancelRequest(Request newRequest){
        openRequests.remove(newRequest);
        //should add the nofications to the drivers ??
    }

    public void pickDriver(User user){
        currentRequest.setFinalDriver(user);
        currentRequest.setCurrentState("Working");
        currentRequest.getFinalDriver().setAcceptedRequest(currentRequest);
    }

}

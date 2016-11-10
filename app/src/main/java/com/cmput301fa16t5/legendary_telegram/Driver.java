package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Driver
 */
public class Driver extends RiderDriverParent {

    private ArrayList<Request> openRequests;
    private Request currentRequest;
    private IdentificationCard cardToRequest;

    /**
     * Constructor does not include instantiation of ArrayList
     * Because the arraylist should be provided by Map+ESearch.
     */
    public Driver() {    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    public void setOpenRequests(ArrayList<Request> openRequests) {
        this.openRequests = openRequests;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public void acceptARequest(Integer index, IdentificationCard me) {
        this.cardToRequest = me;
        this.currentRequest = this.openRequests.get(index);
        this.currentRequest.addADriver(this.cardToRequest);
        this.currentRequest.setOnServer(false);
    }

    public boolean checkIfPicked() {
        return this.currentRequest.checkCommittedDriver(this.cardToRequest);
    }

    public void commit() {
        this.currentRequest.commitToRequest();
        this.currentRequest.setOnServer(false);
    }
}

package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Driver class.
 * User has one of these, calls upon it.
 * Does not have a User.
 */
public class Driver extends RiderDriverParent {

    /**
     * List of Requests to be provided by Map and ESearch
     * Request currently focusing on.
     * The IdentificationCard is to "idiot proof" the
     * code. Basically, what happens if you post an acceptance/commit
     * of a request and then decide to change your user info? For that
     * request, it will always be static.
     */
    private IdentificationCard cardToRequest;

    /**
     * Constructor does not include instantiation of ArrayList
     * Because the arraylist should be provided by Map+ESearch.
     */
    public Driver() {    }

    /**
     * Getter for the open requests to be displayed.
     * @return An ArrayList of Requests
     */
    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }

    /**
     * Called by user when it gets a list from ESearch.
     * @param openRequests
     */
    public void setOpenRequests(ArrayList<Request> openRequests) {
        this.openRequests = openRequests;
    }

    /**
     * Getter for current Request
     * @return A request object
     */
    public Request getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Called when a User clicks on a Request object, as a Driver,
     * that they would like to accept.
     * @param index The index in an ArrayAdapter
     * @param me An identification card created by the User class.
     */
    public void acceptARequest(Integer index, IdentificationCard me) {
        this.cardToRequest = me;
        this.currentRequest = this.openRequests.get(index);
        this.currentRequest.addADriver(this.cardToRequest);
        this.currentRequest.setOnServer(false);
    }

    /**
     * Checks if you're the picked Driver.
     * @return True if you are, false otherwise.
     */
    public boolean checkIfPicked() {
        return this.currentRequest.checkCommittedDriver(this.cardToRequest);
    }

    /**
     * if you're the picked Driver you can commit.
     */
    public void commit() {
        this.currentRequest.commitToRequest();
        this.currentRequest.setOnServer(false);
    }

    /**
     * Setter for current Request.
     * @param index: Index of request to focus on.
     */
    public void setCurrentRequest(Integer index) {
        this.currentRequest = openRequests.get(index);
    }
}

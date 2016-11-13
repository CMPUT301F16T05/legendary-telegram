package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider class. Creates Requests. Selects Drivers. Completes and Cancels Requests.
 */
public class Rider extends RiderDriverParent {

    public Rider() {
        openRequests = new ArrayList<Request>();
    }

    /**
     * Creates new Request.
     * @param me: IdentificationCard for Requests creator.
     * @param start: Start coordinates.
     * @param end: End coordinates.
     * @return: A new Request.
     */
    public Request createNewRequest(IdentificationCard me, LatLng start, LatLng end) {
        Request newR = new Request(me, start, end);
        openRequests.add(newR);
        return newR;
    }

    /**
     * Sets a request from a list of Requests.
     * @param index: The index of the request to focus on.
     */
    public void setCurrentRequest(Integer index) {
        this.currentRequest = openRequests.get(index);
    }

    /**
     * Selects a Driver to accept based on the list of accepted Drivers of the Current Request.
     * @param index
     */
    public void selectDriver(Integer index) {
        this.currentRequest.acceptADriver(index);
    }

    /**
     * Removes Request from list.
     * @return: The request itself so that ESearch knows what to axe.
     */
    public Request removeOrComplete() {
        Request r = this.currentRequest;
        this.currentRequest = null;
        this.openRequests.remove(r);
        return r;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }
}

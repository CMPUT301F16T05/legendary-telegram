package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider.
 */
public class Rider {

    private ArrayList<Request> openRequests;
    private Request currentRequest;

    public Rider() {
        openRequests = new ArrayList<Request>();
    }

    public void createNewRequest(IdentificationCard me, LatLng start, LatLng end) {
        openRequests.add(new Request(me, start, end));
    }

    public void setCurrentRequest(Integer index) {
        this.currentRequest = openRequests.get(index);
    }

    public void selectDriver(Integer index) {
        this.currentRequest.acceptADriver(index);
    }

    // This needs to return a string so that ESearch knows what to banish to the void (delete).
    public String removeOrComplete() {
        this.openRequests.remove(this.currentRequest);
        return this.currentRequest.getId();
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public ArrayList<Request> getOpenRequests() {
        return openRequests;
    }
}

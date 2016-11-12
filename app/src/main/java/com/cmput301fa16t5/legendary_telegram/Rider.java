package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider.
 */
public class Rider extends RiderDriverParent {

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

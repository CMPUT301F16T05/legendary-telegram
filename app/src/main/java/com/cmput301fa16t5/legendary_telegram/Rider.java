package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by keith on 11/2/2016.
 * Rider.
 */
public class Rider extends RiderDriverParent {

<<<<<<< HEAD
    private ArrayList<Request> openRequests;
    private Request currentRequest;

    public Rider() {
        openRequests = new ArrayList<Request>();
    }

    public void createNewRequest(IdentificationCard me, LatLng start, LatLng end) {
        openRequests.add(new Request(me, start, end));
=======
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
>>>>>>> 28cb1ccdb103e62f3ea015c3a0c44d63adb6145a
    }

    public void setCurrentRequest(Integer index) {
        this.currentRequest = openRequests.get(index);
    }

<<<<<<< HEAD
    public void selectDriver(Integer index) {
        this.currentRequest.acceptADriver(index);
=======
    public void cancelRequest(Request newRequest){
        openRequests.remove(newRequest);
        currentRequest.setCurrentState("Cancelled");
        //should add the nofications to the drivers ??
>>>>>>> 28cb1ccdb103e62f3ea015c3a0c44d63adb6145a
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

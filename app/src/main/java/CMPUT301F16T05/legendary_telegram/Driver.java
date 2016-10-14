package CMPUT301F16T05.legendary_telegram;

import java.util.ArrayList;

/**
 * Governs the details exclusive to Driver
 */
/*
Searching

        US 04.01.01
        As a driver, I want to browse and search for open requests by geo-location.

        US 04.02.01
        As a driver, I want to browse and search for open requests by keyword.

        Accepting
        US 05.01.01
        As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.

        US 05.02.01
        As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.

        US 05.03.01
        As a driver, I want to see if my acceptance was accepted.

        US 05.04.01
        As a driver, I want to be notified if my ride offer was accepted.
*/
public class Driver extends User {

    private ArrayList<Request> acceptedRequests = new ArrayList<Request>();
    private ArrayList<Request> availableRequest = new ArrayList<Request>();

    public Driver(){
        super();
    }

    public ArrayList<Request> LoadRequestList(){
        return null;
    }

    public ArrayList<Request> FilterLocation(){
        return null;
    }

    public ArrayList<Request> FilterKeyword(){
        return null;
    }

    public void AcceptRequest(Request request){
        this.acceptedRequests.add(request);
    }

    public ArrayList<Request> ListAvailableJobs(){
        return this.availableRequest;
    }

    public ArrayList<Request> ListActiveJobs(){
        return this.acceptedRequests;
    }

    public void NotifyDriver(){

    }

}

package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Created by keith on 10/30/2016.
 * Request class.
 * Contains fields for ID (given by ESearch),
 * An IdentificationCard of the Rider who made it.
 * IdentificationCards of the Drivers who accepted it.
 * IdentificationCard of the Driver accepted by the Rider.
 * LatLng coordinates and Strings for Searching.
 * Calculates it's own fee estimate.
 * A state, which determines what it will print out in .toString.
 */
public class Request {

    //ID created by ElasticSearchControllerfield noting the ID that ElasticSearch gives it.
    @JestId
    private String id;

    private IdentificationCard myRider;
    private ArrayList<IdentificationCard> potentialDrivers;
    private IdentificationCard myDriver;

    private LatLng startLocation;
    private LatLng endLocation;

    //LatLng encodes on the server in a way that the GeoDistance query
    //does like so they have to be parsed into a format that it does like
    private String elasticStart;
    private String elasticEnd;

    private Double fee;
    private RequestEnum state;

    //Used as an indicator for adding and deleting things from the elasticsearch server
    private Boolean onServer;

    public Request(IdentificationCard me, LatLng start, LatLng end) {
        this.id = null;
        this.myRider = me;
        this.myDriver = null;
        this.startLocation = start;
        this.endLocation = end;
        this.state = RequestEnum.pendingUpload;
        this.potentialDrivers = new ArrayList<IdentificationCard>();
        this.computeEstimate();
        this.onServer = false;

        //parse the server friendly locations
        this.elasticStart = String.valueOf(start.latitude) + "," + String.valueOf(start.longitude);
        this.elasticEnd = String.valueOf(end.latitude) + "," + String.valueOf(end.longitude);
    }

    /**
     * Function that computes the price estimate using Manhattan Diztance
     * https://en.wiktionary.org/wiki/Manhattan_distance
     * https://en.wikipedia.org/wiki/Geographic_coordinate_system
     * This was chosen based on the first Assignment of CMPUT 274, where you use
     * Manhattan distance to list restaurants in order of distance.
     * to get from Lat/Long to kilometers
     */
    public void computeEstimate() {
        Double latDegToKM = (111132.92 - 559.82*Math.cos(2*startLocation.latitude)
            + 1.175*Math.cos(4*startLocation.latitude) - 0.0023*Math.cos(6*startLocation.latitude)) /
                1000;

        Double longDegToKM = (111412.84*Math.cos(startLocation.longitude) -
                93.5*Math.cos(3*startLocation.longitude) + 0.118*Math.cos(5*startLocation.longitude))
                / 1000;

        double manhattanLat = Math.abs(startLocation.latitude-endLocation.latitude) * latDegToKM;
        double manhattanLong = Math.abs(startLocation.longitude-endLocation.longitude) * longDegToKM;

        // $2.25 plus 80 cents on the kilometer
        this.fee = 2.25 + 0.8*(manhattanLat + manhattanLong);

    }

    /**
     * Should only ever be called when acting as Driver.
     * @param newDriver: Card of the new Driver.
     */
    public void addADriver(IdentificationCard newDriver) {
        this.potentialDrivers.add(newDriver);
        this.state = RequestEnum.hasADriver;
        this.setOnServer(false);
    }

    /**
     * Should only ever be called when acting as Rider.
     * @param index: Index of the Driver accepted.
     */
    public void acceptADriver(Integer index) {
        this.myDriver = potentialDrivers.get(index);
        this.state = RequestEnum.acceptedADriver;
        this.setOnServer(false);
    }

    /**
     * Called by Driver actor to see if the committed Driver is in fact them.
     * @param card Card to be compared.
     */
    public boolean checkCommittedDriver(IdentificationCard card) {
        if (this.myDriver == null) {
            return false;
        }
        return this.myDriver.isThisMe(card);
    }

    /**
     * Should only ever be called by Driver actor.
     */
    public void commitToRequest() {
        this.state = RequestEnum.driverHasCommitted;
    }

    /**
     * Here's the purpose of our enum.
     * Request will say different things depending on state.
     * @return A string to be printed in an array adapter depending on state.
     */
    @Override
    public String toString() {

        String stringFee = "$" + String.format("%.2f", this.fee);
        switch (this.state) {

            case openRequest:
                return this.id + "\n" + stringFee + "\nNo Accepting Drivers";

            case hasADriver:
                return this.id + "\n" + stringFee + "\nDrivers: " + this.potentialDrivers.size();

            case acceptedADriver:
                return this.id + "\n" + stringFee + "\nAccepted Driver: " + myDriver.getName();

            case driverHasCommitted:
                return this.id + "\n" + stringFee + "\n" + myDriver.getName() + " has committed.";
        }
        // The default to pendingUpload.
        return "New Request Pending Upload" + "\n" + stringFee;
    }

    public Boolean isOnServer() {
        return onServer;
    }

    /**
     * Called when Request initially put on Server.
     * @param onServer Value to set OnServer variable.
     */
    public void setOnServer(Boolean onServer) {
        if (state == RequestEnum.pendingUpload && onServer) {
            state = RequestEnum.openRequest;
        }
        this.onServer = onServer;
    }

    public Double getFee() {
        return fee;
    }

    public RequestEnum getState() {
        return state;
    }

    public ArrayList<IdentificationCard> getPotentialDrivers() {
        return potentialDrivers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public IdentificationCard getMyRider() {
        return myRider;
    }

    public IdentificationCard getMyDriver() {
        return myDriver;
    }
    
    public String getElasticStart(){
        return elasticStart;
    }

    public String getElasticEnd(){
        return elasticEnd;
    }
}

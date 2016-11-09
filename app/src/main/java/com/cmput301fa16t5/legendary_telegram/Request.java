package com.cmput301fa16t5.legendary_telegram;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Created by keith on 10/30/2016.
 * Request
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

    private Double fee;
    private RequestEnum state;

    //Used as an indicator for adding and deleting things from the elasticsearch server
    private Boolean onServer;

    public Request(IdentificationCard me, LatLng start, LatLng end) {
        this.id = null;
        this.myRider = me;
        this.startLocation = start;
        this.endLocation = end;
        this.state = RequestEnum.openRequest;
        this.potentialDrivers = new ArrayList<IdentificationCard>();
        this.computeEstimate();
    }

    /**
     * Function that computes the price estimate using Manhattan Diztance
     * https://en.wiktionary.org/wiki/Manhattan_distance
     * https://en.wikipedia.org/wiki/Geographic_coordinate_system
     * This was chosen based on the first Assignment of CMPUT 274, where you use
     * Manhattan distance to list restaurants in order of distance.
     * to get from Lat/Long to KM
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
        this.fee = 2.25 + 0.8*(manhattanLat + manhattanLong);

    }

    public void addADriver(IdentificationCard newDriver) {
        this.potentialDrivers.add(newDriver);
        this.state = RequestEnum.hasADriver;
        this.setOnServer(false);
    }

    public void acceptADriver(Integer index) {
        this.myDriver = potentialDrivers.get(index);
        this.state = RequestEnum.AcceptedADriver;
        this.setOnServer(false);
    }

    public boolean checkCommittedDriver(IdentificationCard card) {
        return this.myDriver.isThisMe(card);
    }

    public void commitToRequest() {
        this.state = RequestEnum.DriverHasCommitted;
    }

    public Boolean isOnServer() {
        return onServer;
    }

    public void setOnServer(Boolean onServer) {
        this.onServer = onServer;
    }

    public Double getFee() {
        return fee;
    }

    public RequestEnum getState() {
        return state;
    }
}

package com.cmput301fa16t5.legendary_telegram;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by keith on 10/30/2016.
 * Request
 */

public class Request {

    //I won't put down the coordinates for the map... should be filled out later using a specified
    // way.

    // Should have a

    //ID created by ElasticSearchControllerfield noting the ID that ElasticSearch gives it.
    private String id;
    //Rider that created request and driver that will fulfill it
    private Rider rider;

    private ArrayList<Driver> waitingDrivers;

    private Driver finalDriver;
    //Location rider wishes to be picked up and dropped off at
    private LatLng startLocation;
    private LatLng endLocation;
    // How much the ride will cost. Calculated based on startLocation and endLocation
    private double fee;
    // State of the request. See RequestEnum.java for details.
    private RequestEnum state;
    // Use to calculate the fee (may improve after using the map)
    private double distance;

    //Used as an indicator for adding and deleting things from the elasticsearch server
    private Boolean onServer;

    public Request(Rider rider, LatLng start, LatLng end){

        SecureRandom random = new SecureRandom();
        this.id = new BigInteger(64, random).toString(32);
        this.rider = rider;
        this.startLocation = start;
        this.endLocation = end;
        this.state = RequestEnum.openRequest;
        this.fee = calculateFee();
        //TODO figure out the distance based on start and end location
        this.distance = distance;
        this.onServer = Boolean.FALSE;
    }

    public void setFinalDriver(Driver finalDriver) {
        this.finalDriver = finalDriver;
    }

    public Driver getFinalDriver() {
        return finalDriver;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Driver> getWaitingDrivers() {
        return waitingDrivers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    //TODO, figure out the formula for this (Similar one with the UBER's)
    public double calculateFee(){
        return 2.25 + 0.8 * distance;
    }

    public RequestEnum getState() {
        return state;
    }

    public void setState(RequestEnum state) {
        this.state = state;
    }

    public void addWaitingDrivers(Driver driver){
        waitingDrivers.add(driver);
    }

    public Boolean isOnServer() {
        return onServer;
    }

    public void setOnServer(Boolean onServer) {
        this.onServer = onServer;
    }

}

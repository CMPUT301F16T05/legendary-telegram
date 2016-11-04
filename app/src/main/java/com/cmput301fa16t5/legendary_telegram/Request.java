package com.cmput301fa16t5.legendary_telegram;

import android.location.Location;

/**
 * Created by keith on 10/30/2016.
 * Request
 */

public class Request {

    //I won't put down the coordinates for the map... should be filled out later using a specified
    // way.

    // Should have a field noting the ID that ElasticSearch gives it.

    //ID created by ElasticSearchController
    private String id;
    //Rider that created request and driver that will fulfill it
    private Rider rider;
    private Driver driver;
    //Location rider wishes to be picked up and dropped off at
    private Location startLocation;
    private Location endLocation;
    // How much the ride will cost. Calculated based on startLocation and endLocation
    private double fee;
    // State of the request. See RequestEnum.java for details.
    private RequestEnum state;

    public Request(Rider rider, Location start, Location end){
        this.rider = rider;
        this.startLocation = start;
        this.endLocation = end;
        this.state = RequestEnum.openRequest;
        this.fee = calculateFee(start, end);
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    //TODO, figure out the formula for this
    public double calculateFee(Location start, Location end){
        return 0;
    }

    public RequestEnum getState() {
        return state;
    }

    public void setState(RequestEnum state) {
        this.state = state;
    }

}

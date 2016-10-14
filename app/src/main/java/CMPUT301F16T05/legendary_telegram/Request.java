package CMPUT301F16T05.legendary_telegram;

import android.location.Location;

/**
 *
 */

public class Request {

    private String name;
    private Driver driver = new Driver();
    private Rider rider = new Rider();
    private Double payment;
    private Location endLocation;
    private Location startLocation;

    public Request(String name, Double payment,
                   Location endLocation, Location startLocation){

        this.name = name;
        this.payment = payment;
        this.endLocation = endLocation;
        this.startLocation = startLocation;

    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }
}

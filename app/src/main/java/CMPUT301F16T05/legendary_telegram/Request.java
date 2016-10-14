package CMPUT301F16T05.legendary_telegram;

import android.location.Location;

/**
 *
 */
public class Request {

    private String name;
    private Driver driver = new Driver();
    private Rider rider = new Rider();
    private Float payment;
    private Location endLocation;
    private Location startLocation;

    public Request(String name, Rider rider, Float payment,
               Location endLocation, Location startLocation){

        this.name = name;
        this.rider = rider;
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

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
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

package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/2/2016.
 * User class.
 * Holds name, phone number, email.
 * Talks with CentralController, a lot.
 * Or should I say, is manipulated by, and manipulates what it has, based on what the user enters on the screen.
 */

public class User {

    private String userName;
    private String email;
    private String telephone;
    private String vehicle;
    private Rider myRider;
    private Driver myDriver;

    public User(String userName, String email, String telephone) {
        this.userName = userName;
        this.email = email;
        this.telephone = telephone;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    // Used for both adding a vehicle and modifying existing.
    public void modifyVehicle(String vehicle) {
        if (vehicle.equals("")) {
            this.vehicle = "";
            this.myDriver = null;
        }

        else {
            this.vehicle = vehicle;
            if (this.myDriver.equals(null)) {
                this.myDriver = new Driver();
            }
        }

    }
}

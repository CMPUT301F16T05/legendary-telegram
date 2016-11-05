package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

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

    // Constructor - Initialize all attributes
    public User(String userName, String email, String telephone) {
        this.userName = userName;
        this.email = email;
        this.telephone = telephone;
        this.vehicle = "N/A"; // can use to check if the user can be a driver
        this.myDriver = null;
        this.myRider = new Rider();
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

    public String getVehicle() {
        return vehicle;
    }

    public Rider getMyRider() {
        return myRider;
    }

    public Driver getMyDriver() {
        return myDriver;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setMyRider(Rider myRider) {
        this.myRider = myRider;
    }

    public void setMyDriver(Driver myDriver) {
        this.myDriver = myDriver;
    }

    public void saveChanges(User  NewUser){
        if (this.getUserName() == NewUser.getUserName()){
            this.setEmail(NewUser.getEmail());
            this.setTelephone(NewUser.getTelephone());
            this.setVehicle(NewUser.getVehicle());
            this.setEmail(NewUser.getEmail());
            this.setMyRider(NewUser.getMyRider());
            this.setMyDriver(NewUser.getMyDriver());
        }
        else{
            // Delete Username from the local file need GSON stuff(wait for the function)
            // GsonController.deleteUserInDisk(user,"");
            // Add new Username and information to the local file(maybe also the server)
            // Context context = new Context;
            // GsonController.saveUserToDisk(NewUser,context);
        }
    }

    public String showInfo(){
        return this.userName + ":\n" + this.telephone + " " + this.email;
    }

    public void checkUserName(String userName){

    }
}

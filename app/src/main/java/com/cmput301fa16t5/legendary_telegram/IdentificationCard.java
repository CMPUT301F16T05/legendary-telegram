package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by keith on 11/9/2016.
 * Instead of passing around an entire user, rider, or driver object
 * This will contain the relevant fields.
 * This is used instead of sending a tuple with the information fields.
 */
public class IdentificationCard {

    private String name;
    private String phone;
    private String email;
    private String vehicle;
    // False if Rider, True if Driver
    // Do not confuse.
    private Boolean isROrD;

    public IdentificationCard(String n, String p, String e) {
        this.name = n;
        this.phone = p;
        this.email = e;
        this.isROrD = false;
    }

    public IdentificationCard(String n, String p, String e, String v) {
        this.name = n;
        this.phone = p;
        this.email = e;
        this.vehicle = v;
        this.isROrD = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Boolean getROrD() {
        return isROrD;
    }

    public void setROrD(Boolean ROrD) {
        isROrD = ROrD;
    }

    public boolean isThisMe(IdentificationCard card) {
        return ((this.name.equals(card.getName())) &&
            this.phone.equals(card.getPhone()) &&
            this.email.equals(card.getEmail()));
    }
}

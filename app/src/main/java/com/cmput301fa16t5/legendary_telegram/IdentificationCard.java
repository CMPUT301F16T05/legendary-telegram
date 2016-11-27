package com.cmput301fa16t5.legendary_telegram;

/**
 * Small object that simply holds Username, Phone, Email, Vehicle fields.
 *
 * Instead of passing around an entire user, rider, or driver object
 * This will contain the relevant fields.
 * This is used instead of sending a tuple with the information fields.
 */
public class IdentificationCard {

    private String name;
    private String phone;
    private String email;
    private String vehicle;

    /**
     * Two constructors
     * One for Rider, one for Driver
     * Polymorphism at play.
     */

    /**
     * Rider constructor.
     * @param n Name from User.
     * @param p Phone number from user.
     * @param e Email from User.
     */
    public IdentificationCard(String n, String p, String e) {
        this.name = n;
        this.phone = p;
        this.email = e;
        this.vehicle = null;
    }

    /**
     * Driver Constructor
     * @param n Name from User.
     * @param p Phone number from User.
     * @param e Email from User.
     * @param v Vehicle from User.
     */
    public IdentificationCard(String n, String p, String e, String v) {
        this.name = n;
        this.phone = p;
        this.email = e;
        this.vehicle = v;
    }

    /**
     * Compares two and see if they're the same.
     * @param card Another card to be compared.
     * @return True if a match, false otherwise.
     */
    public boolean equals(IdentificationCard card) {
        return ((this.name.equals(card.getName())) &&
                this.phone.equals(card.getPhone()) &&
                this.email.equals(card.getEmail()));
    }

    /**
     * Called on the Request Status activity.
     * For ArrayAdapter.
     * @return String describing the card.
     */
    @Override
    public String toString() {
        String defaultAnswer = this.getName() + "\n" +
                this.getEmail() + "\n" +
                this.getPhone();

        if (this.vehicle != null) {
            defaultAnswer += "\n" + this.vehicle;
        }

        return defaultAnswer;
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

}

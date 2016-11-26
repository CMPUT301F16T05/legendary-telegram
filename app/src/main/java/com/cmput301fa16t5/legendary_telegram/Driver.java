package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Driver class. Accepts and Commits Requests. Asks if it has been picked.
 * @author keith
 */
public class Driver extends RiderDriverParent {

    /**
     * List of Requests to be provided by Map and ESearch
     * Request currently focusing on.
     * The IdentificationCard is to "idiot proof" the
     * code. Basically, what happens if you post an acceptance/commit
     * of a request and then decide to change your user info? For that
     * request, it will always be static.
     */
    private IdentificationCard cardToRequest;

    /**
     * Even though we'll be getting ArrayLists from ESearch,
     * we include one because we need it to avoid a NullPointerException in MainRequestActivity.
     */
    public Driver() {
        super();
    }

    /**
     * Called when a User clicks on a Request object, as a Driver,
     * that they would like to accept.
     * @param me An identification card created by the User class.
     */
    public void acceptARequest(IdentificationCard me) {
        this.cardToRequest = me;
        this.currentRequest.addADriver(this.cardToRequest);
    }

    /**
     * Checks if you're the picked Driver.
     * @return True if you are, false otherwise.
     */
    public boolean checkIfPicked() {
        return this.currentRequest.checkCommittedDriver(this.cardToRequest);
    }

    /**
     * if you're the picked Driver you can commit.
     */
    public void commit() {
        this.currentRequest.commitToRequest();
        this.currentRequest.setOnServer(false);
    }
}

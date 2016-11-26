package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Rider class. Creates Requests. Selects Drivers. Completes and Cancels Requests.
 * @author keith
 */
public class Rider extends RiderDriverParent {

    public Rider() {
        super();
    }

    /**
     * Creates new Request.
     * @param me: IdentificationCard for Requests creator.
     * @param start: Start coordinates.
     * @param end: End coordinates.
     * @return A new Request.
     */
    public Request createNewRequest(IdentificationCard me, LatLng start, LatLng end) {
        Request newR = new Request(me, start, end);
        openRequests.add(newR);
        return newR;
    }

    /**
     * Creates new Request with an optional description field.
     * @param me: IdentificationCard for Requests creator.
     * @param start: Start coordinates.
     * @param end: End coordinates.
     * @param description: Description provided for the request
     * @return A new Request with a filled in description
     */
    public Request createNewRequest(IdentificationCard me, LatLng start, LatLng end, String description) {
        Request newR = new Request(me, start, end, description);
        openRequests.add(newR);
        return newR;
    }

    /**
     * Selects a Driver to accept based on the list of accepted Drivers of the Current Request.
     * @param index Index of the accepted driver.
     */
    public void selectDriver(Integer index) {
        this.currentRequest.acceptADriver(index);
    }

    /**
     * Removes Request from list. Use either when completing or cancelling.
     * @return The request itself so that ESearch knows what to axe.
     */
    public Request removeOrComplete() {
        Request r = this.currentRequest;
        this.currentRequest = null;
        this.openRequests.remove(r);
        return r;
    }
}

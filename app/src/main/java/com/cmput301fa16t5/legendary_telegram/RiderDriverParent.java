package com.cmput301fa16t5.legendary_telegram;

import java.util.ArrayList;

/**
 * Created by keith on 11/9/2016.
 * This class exists for the purpose
 * of using Java reflection in user
 * to determine if we're acting
 * as a Rider or Driver.
 * Probably needs a lot of Refactoring between itself, Rider, Driver, and User.
 */
public class RiderDriverParent {
    protected ArrayList<Request> openRequests;
    protected Request currentRequest;

    public RiderDriverParent() {    }
}

package com.cmput301fa16t5.legendary_telegram;

/**
 * Enum describing the states of Request
 *
 * Request is initially made set to openRequest.
 *
 * hasADriver is the state it takes when it is accepting Drivers.
 *
 * acceptedADriver is when a Rider has picked a Driver, and can no longer accept new Drivers.
 *
 * Driver has committed confirms that the Driver is on their way.
 *
 * There is no "complete" option; as the ride comes to a close the Rider's Client will
 * wipe the request from ESearch.
 * @author kgmills
 */

public enum RequestEnum {
    openRequest,
    hasADriver,
    acceptedADriver,
    driverHasCommitted
}

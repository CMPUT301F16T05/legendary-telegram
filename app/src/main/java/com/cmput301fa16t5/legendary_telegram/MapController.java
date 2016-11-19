package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * MapController class is the controller of the map.
 * @author zhimao
 */

public class MapController {

    private CentralController centralCommand;

    public MapController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Sends the Central Controller coordinates depending on whether a new Request will be made
     * (Rider) or whether requests will be looked for (Driver).
     * @param positionPair: An ArrayList of Coordinates.
     */
    public void sendCoordinates(ArrayList<LatLng> positionPair) {
        if (positionPair.size() == 1) {
            centralCommand.searchRequests(positionPair);
        } else {
            centralCommand.createRequest(positionPair);
        }
    }
}

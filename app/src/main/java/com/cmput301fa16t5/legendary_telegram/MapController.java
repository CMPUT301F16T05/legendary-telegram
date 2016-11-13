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
     * @param context Needed for GSON, since the User is saved afterwords.
     */
    public void sendCoordinates(ArrayList<LatLng> positionPair, Context context) {
        if (positionPair.size() == 1) {
            centralCommand.searchRequests(positionPair, context);
        } else {
            centralCommand.createRequest(positionPair, context);
        }

    }


}

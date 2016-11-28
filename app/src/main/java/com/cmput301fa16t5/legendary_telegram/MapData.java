package com.cmput301fa16t5.legendary_telegram;

import com.google.android.gms.maps.model.LatLng;

/**
 * Data class created to hold the start and end location data so it can
 * be passed more easily between the map and any activities it might call.
 * Views are dumb and doesn't need to know anything other than where to find this
 *
 * From the commit that made this:
 *
 * Much easier to pass around if needed and better satisfies MVC. No logic
 * was touched in this pass, just a data class with getters and setters.
 */

public class MapData {
    private LatLng start;
    private LatLng end;
    private String startAddress;
    private String endAddress;

    public MapData(){
        //Basic constructor, expects nothing
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}

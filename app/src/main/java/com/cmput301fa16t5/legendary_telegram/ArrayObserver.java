package com.cmput301fa16t5.legendary_telegram;

import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Purpose of this class is to hold the Requests of Rider and Driver and sync them with
 * the views in the activites while biting ElasticSearch's heel and asking "any new stuff"
 * @author keith
 */
public class ArrayObserver {

    private CentralController centralCommand;
    private ArrayList<ArrayAdapter> myAdapters;

    public ArrayObserver(CentralController myC) {
        this.myAdapters = new ArrayList<>();
        this.centralCommand = myC;
    }

    /**
     * Updates the known ArrayAdapters.
     */
    private void update() {
        for (ArrayAdapter v: myAdapters) {
            v.notifyDataSetChanged();
        }
    }

    /**
     * Adds an adapter that can be updated.
     * @param adapt ArrayAdapter to be added.
     */
    public void addAdapter(ArrayAdapter adapt) {
        this.myAdapters.add(adapt);
        update();
    }

    /**
     * Removes an ArrayAdapter
     * @param adapt ArrayAdapter to be removed.
     */
    public void removeAdapter(ArrayAdapter adapt) {
        if (this.myAdapters.contains(adapt)) {
            this.myAdapters.remove(adapt);
        }
    }

    /**
     * This function should be called typically when activities change.
     * Bascially, the bulk of it's functionality and subfunctions have not be written yet
     * It's goal is to query ElasticSearch for updates to the openRequest lists of Rider and
     * Driver, then parse them. If there is a change, I.E. a Request now has an additional
     * Driver, or a Request's state has changed, it will update the relevant ArrayAdapters.
     */
    public void onButtonPress() {
        // A bunch of stuff about ElasticSearch and what not.
        // like "if stuff from ESearch is new"
        update();
    }
}

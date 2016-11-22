package com.cmput301fa16t5.legendary_telegram;

import android.os.AsyncTask;
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
     * US 01.03.01
     * As a rider, I want to be notified if my request is accepted.
     */
    public void sendUpdateNotification(RiderDriverParent rdp) {
        String message = "Requests Updated for Role: ";
        if (rdp.getClass().equals(Driver.class)) {
            message = message + "Driver";
        }

        else {
            message = message + "Rider";
        }

        update();
        centralCommand.nonActivityToast(message);
        centralCommand.saveCurrentUser();
    }

    /**
     * This function should be called typically when activities change.
     * It's goal is to query ElasticSearch for updates to the openRequest lists of Rider and
     * Driver, then parse them. If there is a change, I.E. a Request now has an additional
     * Driver, or a Request's state has changed, it will update the relevant ArrayAdapters,
     * replace the old lists with the updated ones, and give out a notification.
     */
    public void onButtonPress() {

        // We only bother if the Rider/Driver lists have any content in them.
        if (!centralCommand.getCurrentUser().getMyRider().getOpenRequests().isEmpty()) {
            ArrayObserver.NotificationUpdate riderUpdate = new ArrayObserver.NotificationUpdate();
            riderUpdate.execute(centralCommand.getCurrentUser().getMyRider());
        }

        if (!centralCommand.getCurrentUser().getMyDriver().getOpenRequests().isEmpty()) {
            ArrayObserver.NotificationUpdate driverUpdate = new ArrayObserver.NotificationUpdate();
            driverUpdate.execute(centralCommand.getCurrentUser().getMyDriver());
        }
    }

    /**
     * Takes in a RiderDriverParent and updates them, performing all the calculations in it's
     * doInBackground function.
     */
    private class NotificationUpdate extends AsyncTask<RiderDriverParent, Void, Void> {

        /**
         * Checks if any of the existing requests in a RiderDriverParent's ArrayList are
         * not on the server. Attempts to rectify that situation if so. If it cannot put requests on
         * the server, it gives up (couldn't push 1, why bother trying to pull 10?)
         *
         * Then if it can, it will try to get the updated Requests. It then compares the old and the
         * new. If the new is updated, it notifies the user and replaces the old list with the new.
         * @param rdp The RiderDriverParent (Rider or Driver) with a list to update
         * @return Nothing.
         */
        @Override
        protected Void doInBackground(RiderDriverParent... rdp) {

            RiderDriverParent myOperand = rdp[0];
            ArrayList<Request> requests = myOperand.getOpenRequests();

            // Check if any existing requests in the list are not on the server in their updated
            // forms ans rectify.
            for (Request r: requests) {
                if (!r.isOnServer()) {
                    r.setOnServer(centralCommand.updateRequest(r));
                }
            }

            // If we couldn't put a request not on the server on the server, just stop. Internet or
            // Hindle's computer isn't working :P
            for (Request r: requests) {
                if (!r.isOnServer()) {
                    return null;
                }
            }

            // Get the updated Requests.
            ArrayList<Request> newList = centralCommand.getRequestsByID(myOperand.getIDArray());

            // We didn't get anything. Stop.
            if ((newList.size() == 1) && (newList.get(0).getMyRider() == null)) {
                return null;
            }

            // The size is different. Clearly at least one of them has been deleted (and therefore
            // changed). So we must update.
            else if (newList.size() < requests.size()) {
                myOperand.setOpenRequests(newList);
                sendUpdateNotification(myOperand);
            }

            // The size is the same. Are they updated with new info? Or the same as the instances
            // currently in store. We must check. If one is different, it's enough to issue a
            // notification
            else {
                for (int i = 0; i < requests.size(); i++) {
                    if (!requests.get(i).equals(newList.get(i))) {
                        myOperand.setOpenRequests(newList);
                        sendUpdateNotification(myOperand);
                        return null;
                    }
                }
            }

            return null;
        }
    }
}

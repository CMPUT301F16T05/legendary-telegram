package com.cmput301fa16t5.legendary_telegram;

import android.widget.ArrayAdapter;

/**
 * Created by keith on 11/2/2016.
 * Controller for the Request Status screen.
 */
public class RequestStatusController {

    private CentralController centralCommand;

    public RequestStatusController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Get the ID of a request to display.
     * @return A string containing the ID.
     */
    public String getRequestName() {
        return centralCommand.getCurrentUser().getMyRider().getCurrentRequest().getId();
    }

    /**
     * Sets the ArrayAdapter with appropriate information.
     * @param activity: Needed because Android.
     * @return: Set ArrayAdapter.
     */
    public ArrayAdapter<IdentificationCard> setRequestAdapter(RequestStatusActivity activity) {
        ArrayAdapter<IdentificationCard> adapt = new ArrayAdapter<>(activity, R.layout.card_items,
                centralCommand.getCards());
        centralCommand.addArrayAdapter(adapt);
        return adapt;
    }

    /**
     * Cancels Request. Tells ESearchController to delete. Saves to disk.
     */
    public void cancel() {
        Request cancelledRequest = centralCommand.getCurrentUser().getMyRider().removeOrComplete();
        ElasticSearchController.DeleteRequestsTask delRequest = new ElasticSearchController.DeleteRequestsTask();
        delRequest.execute(cancelledRequest);
        centralCommand.saveCurrentUser();
        centralCommand.pingTheServer();
    }

    public void removeAdapter(ArrayAdapter adapt) {
        centralCommand.removeArrayAdapter(adapt);
    }
}

package com.cmput301fa16t5.legendary_telegram;

import android.widget.ArrayAdapter;

/**
 * Created by keith on 11/2/2016.
 */

public class RequestStatusController {

    private CentralController centralCommand;

    public RequestStatusController() {
        centralCommand = CentralController.getInstance();
    }

    public String getRequestName() {
        return centralCommand.getCurrentUser().getMyRider().getCurrentRequest().getId();
    }

    public ArrayAdapter<IdentificationCard> setRequestAdapter(RequestStatusActivity activity) {
        return new ArrayAdapter(activity, R.layout.card_items, centralCommand.getCards());
    }

    public void cancel() {
        Request cancelledRequest = centralCommand.getCurrentUser().getMyRider().removeOrComplete();
        ElasticSearchController.DeleteRequestsTask delRequest = new ElasticSearchController.DeleteRequestsTask();
        delRequest.execute(cancelledRequest);
    }
}

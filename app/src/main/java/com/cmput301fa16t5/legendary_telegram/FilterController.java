package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yutang and chuan1 on 22/11/26.
 * The filter controller for the activity
 * Update the current requests for the driver using the filter
 * Talk to the central controller
 */
public class FilterController {

    private CentralController centralCommand;

    public FilterController() {
        centralCommand = CentralController.getInstance();
    }

    /**
     * Update the current opening request for the driver
     * @param maxPrice  Max value for the range
     * @param minPrice  Min value for the range
     * @param option    Check using the total price or price/km
     * @param keyword   Keyword searching for
     */
    public void feeOption(Double maxPrice, Double minPrice,String option, String keyword){
        ArrayList<Request> priceFilterRequest;
        ArrayList<Request> keywordFilterRequest = new ArrayList<>();
        ArrayList<Request> filterRequest = new ArrayList<>();

        if (option.equals("Price")){
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, false);
        } else{
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, true);
        }

        if (!keyword.equals("")) {
            keywordFilterRequest = centralCommand.getRequestsByKeyword(keyword.toLowerCase());
        }

        //Values were found for both filters
        if ((!keywordFilterRequest.isEmpty()) && (!priceFilterRequest.isEmpty())) {
            Log.d("TEST", "both");
            for (int i = 0; i < priceFilterRequest.size(); i++) {
                if (keywordFilterRequest.contains(priceFilterRequest.get(i))) {
                    filterRequest.add(priceFilterRequest.get(i));
                }
            }
            //Only keyword was found to have a filter attached
        }else if (!keywordFilterRequest.isEmpty()){
            Log.d("TEST", "both");
            Collections.copy(filterRequest, keywordFilterRequest);
            //Only price was found to have a filter attached
        }else if (!priceFilterRequest.isEmpty()){
            Log.d("TEST", "both");
            Collections.copy(filterRequest, priceFilterRequest);
        }else{
            //Nothing was found to have a filter attached or nothing was found
        }

        centralCommand.getCurrentUser().getMyDriver().setOpenRequests(filterRequest);
    }
}

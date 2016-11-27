package com.cmput301fa16t5.legendary_telegram;

import android.util.Log;

import java.util.ArrayList;

/**
 * Filter Controller, processes inputs to Filter Activity.
 *
 * The filter controller for the activity
 * Update the current requests for the driver using the filter
 * Talk to the central controller
 * @author yutang
 * @author chuan1
 * @author baron
 */
public class FilterController {

    private final CentralController centralCommand;

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
        ArrayList<Request> priceFilterRequest = new ArrayList<>();
        ArrayList<Request> keywordFilterRequest = new ArrayList<>();
        ArrayList<Request> filterRequest = new ArrayList<>();
        Boolean usedKeyword = Boolean.FALSE;
        Boolean usedPrice = Boolean.FALSE;

        //Assume price filter is not being used
        Boolean priceIsAllowed = Boolean.FALSE;
        if (minPrice != null){
            if (maxPrice != null){
                //Valid values were given for both max and min
                priceIsAllowed = Boolean.TRUE;
            } else{
                //Valid value was given for min only
                priceIsAllowed = Boolean.TRUE;
                maxPrice = 100000.0;
            }
        } else if (maxPrice != null){
            //valid price was given for max only
            priceIsAllowed = Boolean.TRUE;
            minPrice = 0.0;
        }

        if (priceIsAllowed){
            Log.d("TEST", "Price search");
            if (option.equals("Price")){
                priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, false);
                usedPrice = Boolean.TRUE;
            } else{
                priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, true);
                usedPrice = Boolean.TRUE;
            }
        }

        if (!keyword.equals("")) {
            Log.d("TEST","Keyword search");
            keywordFilterRequest = centralCommand.getRequestsByKeyword(keyword);
            usedKeyword = Boolean.TRUE;
        }


        if ((usedKeyword) && (usedPrice)) {
            //Values were found for both filters
            Log.d("TEST", "both");
            for (Request rp:priceFilterRequest){
                for (Request rk:keywordFilterRequest){
                    if (rp.getId().equals(rk.getId())){
                        filterRequest.add(rp);
                    }
                }
            }
        }else if (usedKeyword){
            //Only keyword was found to have a filter attached
            Log.d("TEST", "keyword");
            filterRequest = keywordFilterRequest;
        }else if (usedPrice){
            //Only price was found to have a filter attached
            Log.d("TEST", "price");
            filterRequest = priceFilterRequest;
        }else{
            Log.d("TEST","FAILURE");
            //Nothing was found to have a filter attached or nothing was found
        }

        centralCommand.getCurrentUser().getMyDriver().setOpenRequests(filterRequest);
    }
}

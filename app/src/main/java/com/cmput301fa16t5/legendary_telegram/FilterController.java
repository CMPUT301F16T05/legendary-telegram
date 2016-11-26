package com.cmput301fa16t5.legendary_telegram;

import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by tony on 22/11/16.
 */
public class FilterController {

    private CentralController centralCommand;

    public FilterController() {
        centralCommand = CentralController.getInstance();
    }

    public void feeOption(String maxPriceStr, String minPriceStr, RadioButton option){
        double maxPrice = Double.parseDouble(maxPriceStr);
        double minPrice = Double.parseDouble(minPriceStr);

        ArrayList<Request> filterRequest;
        if (option.getText().equals("Price")){
            filterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, false);
        }
        else{
            filterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, true);
        }

        centralCommand.getCurrentUser().getMyDriver().setOpenRequests(filterRequest);
    }
}

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

    public void feeOption(String maxPriceStr, String minPriceStr, RadioButton option, String keyword){
        double maxPrice = 100000000;
        double minPrice = 0;
        if (!maxPriceStr.equals(null)) {
            maxPrice = Double.parseDouble(maxPriceStr);
        }
        if (!minPriceStr.equals(null)) {
            minPrice = Double.parseDouble(minPriceStr);;
        }

        ArrayList<Request> priceFilterRequest;
        ArrayList<Request> keywordFilterRequest;
        ArrayList<Request> filterRequest = new ArrayList<>();
        if (option.getText().equals("Price")){
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, false);
        } else{
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, true);
        }

        if (!keyword.equals(null)) {
            keywordFilterRequest = centralCommand.getRequestsByKeyword(keyword);
            for (int i = 0; i < priceFilterRequest.size(); i++){
                if (keywordFilterRequest.contains(priceFilterRequest.get(i))){
                    filterRequest.add(priceFilterRequest.get(i));
                }
            }
        }else{
            filterRequest = priceFilterRequest;
        }

        centralCommand.getCurrentUser().getMyDriver().setOpenRequests(filterRequest);
    }
}

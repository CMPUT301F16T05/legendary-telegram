package com.cmput301fa16t5.legendary_telegram;

import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by yutang and chuan1 on 22/11/26.
 */
public class FilterController {

    private CentralController centralCommand;

    public FilterController() {
        centralCommand = CentralController.getInstance();
    }

    public void feeOption(Double maxPrice, Double minPrice,String option, String keyword){
        ArrayList<Request> priceFilterRequest;
        ArrayList<Request> keywordFilterRequest;
        ArrayList<Request> filterRequest = new ArrayList<>();

        if (option.equals("Price")){
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, false);
        } else{
            priceFilterRequest = centralCommand.getRequestsByFee(minPrice, maxPrice, true);
        }

        if (!keyword.equals("")) {
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

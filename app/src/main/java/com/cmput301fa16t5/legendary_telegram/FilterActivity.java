package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FilterActivity extends AppCompatActivity {

    private EditText location_ET;
    private EditText keyword_ET;
    private EditText max_distance_ET;
    private EditText min_distance_ET;
    private EditText max_price_ET;
    private EditText min_price_ET;
    private Button filter_B;
    private FilterController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        location_ET = (EditText)findViewById(R.id.locationET);
        keyword_ET = (EditText)findViewById(R.id.keywordET);
        max_distance_ET= (EditText)findViewById(R.id.max_distanceET);
        min_distance_ET = (EditText)findViewById(R.id.min_distanceET);
        max_price_ET = (EditText)findViewById(R.id.max_priceET);
        min_price_ET = (EditText)findViewById(R.id.min_priceET);
        filter_B = (Button)findViewById(R.id.filter_button);



    }
}

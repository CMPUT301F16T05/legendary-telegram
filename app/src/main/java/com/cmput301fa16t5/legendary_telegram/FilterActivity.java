package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by yutang and chuan1 on 22/11/26.
 * Filter Activity
 * For drivers to search the proper requests that they want
 * Using the keyword and price search
 * Filled out by Chuan and Yutang
 */
public class FilterActivity extends AppCompatActivity {

    private EditText location_ET;
    private EditText keyword;
    private RadioGroup option;
    private RadioButton feeButton;
    private EditText max_price_ET;
    private EditText min_price_ET;
    private Button filter_B;
    private FilterController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        location_ET = (EditText)findViewById(R.id.locationET);
        keyword = (EditText)findViewById(R.id.keywordET);
        option = (RadioGroup)findViewById(R.id.radioGroup);
        max_price_ET = (EditText)findViewById(R.id.MaxET);
        min_price_ET = (EditText)findViewById(R.id.MinET);
        filter_B = (Button)findViewById(R.id.filter_button);

        myController = new FilterController();

        location_ET.setText(getIntent().getStringExtra("Location"));

        filter_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int feeOption = option.getCheckedRadioButtonId();
                feeButton = (RadioButton) findViewById(feeOption);

                double maxPrice = 100000000;
                double minPrice = 0;

                if (!max_price_ET.getText().toString().equals("")) {
                    maxPrice = Double.parseDouble(max_price_ET.getText().toString());
                }
                if (!min_price_ET.getText().toString().equals("")) {
                    minPrice = Double.parseDouble(min_price_ET.getText().toString());;
                }
                myController.feeOption(maxPrice, minPrice, feeButton.getText().toString(),
                        keyword.getText().toString());
                finish();
            }
        });
    }
}

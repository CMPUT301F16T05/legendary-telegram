package com.cmput301fa16t5.legendary_telegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

        String location = getIntent().getStringExtra("Location");

        location_ET.setText(location);

        filter_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int feeOption = option.getCheckedRadioButtonId();
                feeButton = (RadioButton) findViewById(feeOption);


                double maxPrice = 100000000;
                double minPrice = 0;
                String keywordstr = keyword.getText().toString();
                if (!max_price_ET.getText().toString().equals("")) {
                    maxPrice = Double.parseDouble(max_price_ET.getText().toString());
                }
                if (!min_price_ET.getText().toString().equals("")) {
                    minPrice = Double.parseDouble(min_price_ET.getText().toString());;
                }
                myController.feeOption(maxPrice, minPrice, feeButton.getText().toString(), keywordstr);
                finish();
            }
        });
    }
}

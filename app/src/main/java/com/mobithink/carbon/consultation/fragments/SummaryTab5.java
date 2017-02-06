package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.R;


public class SummaryTab5 extends Fragment {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mAtmoNumberTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;
    TextView mDirectionNameTextView;
    TextView mEnteredTimeTextView;
    TextView mEnteredDateTextView;

    Button mPDFButton;

    public SummaryTab5() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mWeatherImageView = (ImageView) container.findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) container.findViewById(R.id.weatherTemperatureTextView);
        mAtmoNumberTextView = (TextView) container.findViewById(R.id.atmoNumberTextView);
        mCityNameTextView = (TextView) container.findViewById(R.id.cityNameTextView);
        mLineNameTextView = (TextView) container.findViewById(R.id.lineNameTextView);
        mDirectionNameTextView = (TextView) container.findViewById(R.id.directionNameTextView);
        mEnteredTimeTextView = (TextView) container.findViewById(R.id.enteredTimeTextView);
        mEnteredDateTextView = (TextView) container.findViewById(R.id.enteredDateTextView);

        mPDFButton = (Button) container.findViewById(R.id.pdfButton);
        /*mPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPDFByEmail();
            }
        });*/

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_tab5, container, false);
    }

}

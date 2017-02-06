package com.mobithink.carbon.driving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.station.StationActivity;

/**
 * Created by mplaton on 01/02/2017.
 */

public class DrivingActivity extends Activity {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mActualTime;
    TextView mActualDate;
    TextView mAtmoNumberTextView;
    TextView mCourseTimeTextView;
    TextView mSectionTimeTextView;
    TextView mNextStationNameTextView;

    Button mEventButton;

    RelativeLayout mNextStationRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);

        mWeatherImageView = (ImageView) findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) findViewById(R.id.weatherTemperatureTextView);
        mActualTime = (TextView) findViewById(R.id.actualTime);
        mActualDate = (TextView) findViewById(R.id.actualDate);
        mAtmoNumberTextView = (TextView) findViewById(R.id.atmoNumberTextView);
        mCourseTimeTextView = (TextView) findViewById(R.id.courseTimeTextView);
        mSectionTimeTextView = (TextView) findViewById(R.id.sectionTimeTextView);
        mNextStationNameTextView = (TextView) findViewById(R.id.nextStationNameTextView);
        mNextStationNameTextView.setText("Jean-Jaures");

        mEventButton = (Button) findViewById(R.id.eventButton);

        mNextStationRelativeLayout = (RelativeLayout) findViewById(R.id.nextStationRelativeLayout);
        mNextStationNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStationPage();
            }
        });

    }

    public void goToStationPage(){
        Intent toStationPage = new Intent (this, StationActivity.class);
        this.startActivity(toStationPage);

    }
}

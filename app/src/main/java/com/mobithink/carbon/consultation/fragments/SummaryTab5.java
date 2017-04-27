package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.mobithink.carbon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class SummaryTab5 extends GenericTabFragment {

    //private ImageView mWeatherImageView;
    //private TextView mWeatherTemperatureTextView;
    private TextView mCityNameTextView;
    private TextView mLineNameTextView;
    private TextView mDirectionNameTextView;
    private TextView mEnteredTimeTextView;
    private TextView mEnteredDateTextView;

    private RadarChart mRadarChart;

    public SummaryTab5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_summary_tab5, container, false);

        //mWeatherImageView = (ImageView) rootView.findViewById(R.id.weatherImageView);

        //mWeatherTemperatureTextView = (TextView) rootView.findViewById(R.id.weatherTemperatureTextView);
        mCityNameTextView = (TextView) rootView.findViewById(R.id.cityNameTextView);
        mLineNameTextView = (TextView) rootView.findViewById(R.id.lineNameTextView);
        mDirectionNameTextView = (TextView) rootView.findViewById(R.id.directionNameTextView);
        mEnteredTimeTextView = (TextView) rootView.findViewById(R.id.enteredTimeTextView);
        mEnteredDateTextView = (TextView) rootView.findViewById(R.id.enteredDateTextView);

        mRadarChart = (RadarChart) rootView.findViewById(R.id.radarChart);
        mRadarChart.setBackgroundColor(Color.WHITE);
        mRadarChart.getDescription().setEnabled(false);
        mRadarChart.setWebLineWidth(1f);
        mRadarChart.setWebColor(Color.LTGRAY);
        mRadarChart.setWebLineWidthInner(1f);
        mRadarChart.setWebColorInner(Color.LTGRAY);
        mRadarChart.setWebAlpha(100);

        mRadarChart.getYAxis().setDrawLabels(false);
        mRadarChart.getYAxis().setAxisMinimum(0f);
        mRadarChart.getYAxis().setAxisMaximum(5f);

        setData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

//        if (getTripDTO().getWeather() != null) {
//            int resID = getResources().getIdentifier("drawable/icon_" + getTripDTO().getWeather(), "drawable", getContext().getPackageName());
//            mWeatherImageView.setImageResource(resID);
//        }
//
//        mWeatherTemperatureTextView.setText(getTripDTO().getTemperature());
        mCityNameTextView.setText(getTripDTO().getCityName());
        mLineNameTextView.setText(getTripDTO().getLineName());
        mDirectionNameTextView.setText("dir. " + getTripDTO().getDirection());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        mEnteredTimeTextView.setText(timeFormat.format(getTripDTO().getStartTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM", Locale.FRANCE);
        mEnteredDateTextView.setText(dateFormat.format(getTripDTO().getStartTime()));
    }

    public void setData(){

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(4f, 1));
        entries.add(new RadarEntry(5f, 2));
        entries.add(new RadarEntry(2f, 3));
        entries.add(new RadarEntry(4f, 4));
        entries.add(new RadarEntry(1f, 5));
        entries.add(new RadarEntry(5f, 6));

        RadarDataSet dataSet = new RadarDataSet(entries, "Trip");
        dataSet.setColor(Color.rgb(0, 102, 128));
        dataSet.setDrawFilled(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(dataSet);

        RadarData radarData = new RadarData(sets);
        mRadarChart.setData(radarData);
        radarData.setValueTextSize(5f);
        radarData.setDrawValues(false);


        mRadarChart.animate();

        mRadarChart.setData(radarData);
        mRadarChart.invalidate();

    }

}

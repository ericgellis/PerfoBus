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
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;

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

        mRadarChart.getXAxis().setTextSize(15f);

        mRadarChart.getYAxis().setDrawLabels(false);
        mRadarChart.getYAxis().setAxisMinimum(0f);
        mRadarChart.getYAxis().setAxisMaximum(10f);

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

        double tripTotalTime = getTripDTO().getEndTime() -getTripDTO().getStartTime();

        ArrayList<Double> speedTab = new ArrayList<>() ;
        ArrayList<Double> timeinStationTab = new ArrayList<>() ;
        double speedAdd = 0;
        double averageSpeed;
        double totalTimeInstation = 0;
        int i = 0;

        if (getTripDTO().getStationDataDTOList() != null) {
            for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
                speedTab.add(stationDataDTO.getSpeed());
                double timeInStation = stationDataDTO.getEndTime()-stationDataDTO.getStartTime();
                timeinStationTab.add(timeInStation);
                totalTimeInstation += timeInStation;
            }
        }
        averageSpeed = speedAdd/(getTripDTO().getStationDataDTOList().size()*10);
        double timeInStations = (totalTimeInstation*10)/tripTotalTime;

        ArrayList<Long> eventTimeSavingTab = new ArrayList<>();
        long totalTimeSaving = 0;
        ArrayList<Double> timeInCrossRoadTab = new ArrayList<>() ;
        double totalTimeInCrossroad = 0;

        if (getTripDTO().getEventDTOList() != null){
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()){
                eventTimeSavingTab.add(eventDTO.getTimeSaving());
                totalTimeSaving += eventDTO.getTimeSaving();
                if (eventDTO.getEventType().equals("Evènement en carrefour")){
                    double timeInCrossroad = eventDTO.getEndTime() - eventDTO.getStartTime();
                    timeInCrossRoadTab.add(timeInCrossroad);
                    totalTimeInCrossroad += timeInCrossroad;
                }
            }
        }
        double timePerformance = (((totalTimeSaving+tripTotalTime)/tripTotalTime)-1)*10;
        double timeInCrossRoad = totalTimeInCrossroad*10/tripTotalTime;

        int totalPerson = 0;
        int sum = 0;
        int capacityMore33Count = 0;
        int capacityMore33Percent;
        ArrayList<Integer> busPersonTab = new ArrayList<>();

        int count = 0;
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            int comeIn = stationDataDTO.getNumberOfComeIn();
            int comeOut = stationDataDTO.getNumberOfGoOut();
            totalPerson = totalPerson + comeIn - comeOut;
            sum = sum + totalPerson;
            if (totalPerson > getTripDTO().getVehicleCapacity() / 3) {
                capacityMore33Count++;
            }
            busPersonTab.add(totalPerson);
            count++;
        }

        capacityMore33Percent = (capacityMore33Count*10)/(count);

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry((long) averageSpeed, 1));
        entries.add(new RadarEntry((long) timeInCrossRoad, 2));
        entries.add(new RadarEntry((long) timeInStations, 3));
        entries.add(new RadarEntry(capacityMore33Percent, 4));
        entries.add(new RadarEntry((long) timePerformance, 5));
        entries.add(new RadarEntry(5f, 6));

        RadarDataSet dataSet = new RadarDataSet(entries, "Trip");
        dataSet.setColor(Color.rgb(0, 102, 128));
        dataSet.setDrawFilled(false);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(dataSet);

        RadarData radarData = new RadarData(sets);
        mRadarChart.setData(radarData);
        radarData.setValueTextSize(5f);
        radarData.setDrawValues(false);
        radarData.setLabels(labels);
        mRadarChart.animate();
        mRadarChart.getLegend().setEnabled(false);


        mRadarChart.setData(radarData);
        mRadarChart.invalidate();

    }

}

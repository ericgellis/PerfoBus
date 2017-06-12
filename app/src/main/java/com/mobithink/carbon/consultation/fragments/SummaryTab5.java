package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class SummaryTab5 extends GenericTabFragment {

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
        mRadarChart.setTouchEnabled(false);

        setData();

        mRadarChart.getXAxis().setTextSize(14f);
        mRadarChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"1", "2", "3", "4", "5", "6"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });

        mRadarChart.getYAxis().setDrawLabels(true);
        mRadarChart.getYAxis().setSpaceTop(10f);
        mRadarChart.getYAxis().setAxisMinimum(0f);
        mRadarChart.getYAxis().setAxisMaximum(9f);




        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

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
                speedAdd += stationDataDTO.getSpeed();
                double timeInStation = stationDataDTO.getEndTime()-stationDataDTO.getStartTime();
                timeinStationTab.add(timeInStation);
                totalTimeInstation += timeInStation;
            }
        }
        averageSpeed = speedAdd/(getTripDTO().getStationDataDTOList().size()*10);
        double timeInStations = (totalTimeInstation*10)/tripTotalTime;
        //This condition is used to obtain a chart as if the value is negative.
        if (timeInStations > 0){
            timeInStations = timeInStations;
        } else {
            timeInStations = 0;
        }

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
        float timePerformanceFloat = (float) timePerformance;
        //This condition is used to obtain a chart as if the value is negative.
        if (timePerformanceFloat > 0){
            timePerformanceFloat = timePerformanceFloat;
        } else {
            timePerformanceFloat = 0;
        }

        double timeInCrossRoad = totalTimeInCrossroad*10/tripTotalTime;
        //This condition is used to obtain a chart as if the value is negative.
        if (timeInCrossRoad > 0){
            timeInCrossRoad = timeInCrossRoad;
        } else {
            timeInCrossRoad = 0;
        }

        int totalPerson = 0;
        int sum = 0;
        Double capacityMore33Count = 0.0;
        double capacityMore33Percent;
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
        //This condition is used to obtain a chart as if the value is negative.
        if (capacityMore33Percent > 0){
            capacityMore33Percent = capacityMore33Percent;
        } else {
            capacityMore33Percent = 0;
        }

        int confortRating = 0;
        int securityRating = 0;

        double confortWeightedRating = 0;
        double securityWeightedRating = 0;

        double confortSecurityRating = 0.0;

        if (getTripDTO().getEventDTOList() != null){
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()){
                if (eventDTO.getEventName().equals("Giration difficile") || eventDTO.getEventName().equals("Chicane, écluse") ||  eventDTO.getEventName().equals("Dos d'âne, trapézoïdal")
                        || eventDTO.getEventName().equals("Pavé trop rugueux") || eventDTO.getEventName().equals("Stationnement illicite") || eventDTO.getEventName().equals("Stationnement alterné (effet chicane)")
                        || eventDTO.getEventName().equals("Itinéraire en tiroir ou boucle") || eventDTO.getEventName().equals("Itinéraire sinueux") || eventDTO.getEventName().equals("Trafic")
                        || eventDTO.getEventName().equals("Panne") || eventDTO.getEventName().equals("Giratoire : giration trop importante") || eventDTO.getEventName().equals("Trop d'arrêts")
                        || eventDTO.getEventName().equals("Attente pour correspondance") || eventDTO.getEventName().equals("Capacité station") || eventDTO.getEventName().equals("Foule")
                        || eventDTO.getEventName().equals("Incivilité") || eventDTO.getEventName().equals("Réinsertion dans la circulation")){

                    confortRating += 1;
                }

                if (eventDTO.getEventName().equals("Voie étroite") || eventDTO.getEventName().equals("Stationnement latéral") ||  eventDTO.getEventName().equals("Passage à niveau")
                        || eventDTO.getEventName().equals("Panne") || eventDTO.getEventName().equals("Carrefour à feux : ligne de feu trop avancée") || eventDTO.getEventName().equals("Stationnement illicite")
                        || eventDTO.getEventName().equals("Capacité station") || eventDTO.getEventName().equals("Foule") || eventDTO.getEventName().equals("Incivilité") || eventDTO.getEventName().equals("Incident technique")){

                    securityRating += 1;
                }
            }
            confortWeightedRating = confortRating * 0.27777778;
            securityWeightedRating = securityRating * 0.5;
        }

        confortSecurityRating = confortWeightedRating + securityWeightedRating;
        //This condition is used to obtain a chart as if the value is negative.
        if (confortSecurityRating > 0){
            confortSecurityRating = confortSecurityRating;
        } else {
            confortSecurityRating = 0;
        }

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry( (float) averageSpeed, 0));
        entries.add(new RadarEntry( (float) timeInCrossRoad, 1));
        entries.add(new RadarEntry((float) timeInStations, 2));
        entries.add(new RadarEntry((float) capacityMore33Percent, 3));
        entries.add(new RadarEntry(timePerformanceFloat, 4));
        entries.add(new RadarEntry((float) confortSecurityRating, 5));

        RadarDataSet dataSet = new RadarDataSet(entries, "Trip");
        dataSet.setColor(Color.rgb(0, 102, 128));
        dataSet.setFillAlpha(255);
        dataSet.setDrawFilled(false);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(dataSet);

        RadarData radarData = new RadarData(sets);
        radarData.setValueTextSize(5f);
        radarData.setDrawValues(false);
        mRadarChart.getLegend().setEnabled(false);

        mRadarChart.setData(radarData);
        mRadarChart.invalidate();

    }

}

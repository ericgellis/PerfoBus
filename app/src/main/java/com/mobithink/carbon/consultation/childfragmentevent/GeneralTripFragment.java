package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.PreferenceManager;
import com.mobithink.carbon.utils.Mathematics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by mplaton on 27/04/2017.
 */

public class GeneralTripFragment extends GenericTabFragment implements OnChartValueSelectedListener {

    public static final int MY_REQUEST_CODE = 0;

    private TextView mTotalTripTimeTextView;
    private TextView mAverageSpeedTextView;
    private TextView mLossOfTotalTimeTextView;
    private TextView mSavingOfPossibleTimeTextView;
    private TextView mSavingInEuroTextView;

    private PieChart mDecompositionPieChart;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    private long interStationObjective = 600;
    private long timeInStation;
    private long totalTimeInStation = 0;
    private long averageTimeInStation;

    private double tripBetweenStationsDistance = 0;
    private long tripDistance = 0;

    public GeneralTripFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_fragment_trip, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mTotalTripTimeTextView = (TextView) rootView.findViewById(R.id.totalTripTimeTextView);
        mAverageSpeedTextView = (TextView) rootView.findViewById(R.id.averageSpeedTextView);
        mLossOfTotalTimeTextView = (TextView) rootView.findViewById(R.id.lossOfTotalTimeTextView);
        mSavingOfPossibleTimeTextView = (TextView) rootView.findViewById(R.id.savingOfPossibleTimeTextView);
        mSavingInEuroTextView = (TextView) rootView.findViewById(R.id.savingInEuroTextView);

        mDecompositionPieChart = (PieChart) rootView.findViewById(R.id.decompositionPieChart);

        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        showTotalTripInformations();
    }

    public void showTotalTripInformations(){

        long tripTime = getTripDTO().getEndTime() - getTripDTO().getStartTime();
        mTotalTripTimeTextView.setText(timeFormat.format(tripTime));

        ArrayList<Double> distanceTab = new ArrayList<>() ;
        if (getTripDTO().getStationDataDTOList() != null) {
            for (int i = 0; i + 1 < getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = getTripDTO().getStationDataDTOList().get(i).getEndTime() - getTripDTO().getStationDataDTOList().get(i).getStartTime();
                totalTimeInStation += timeInStation;
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLong()));
                distanceTab.add(tripBetweenStationsDistance);
                tripDistance += tripBetweenStationsDistance;
            }
        }
        double averageTripSpeed = (tripDistance*0.001)/(tripTime*0.00000027778);
        mAverageSpeedTextView.setText(String.valueOf(averageTripSpeed)+ " km/h");

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();
        long timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        int timeSavingInMinutes = (int) (((timeSavingResult / 1000)/60) % 60);

        mSavingOfPossibleTimeTextView.setText(timeFormat.format(timeSavingInMinutes)+ " min");
        mSavingInEuroTextView.setText(Math.round(timeSavingInMinutes* PreferenceManager.getInstance().getCostOfProductionByMinute())  + " euro");


        ArrayList<Double> eventInStationTimeTab = new ArrayList<>() ;
        ArrayList<Double> eventInCrossroadTimeTab = new ArrayList<>() ;
        ArrayList<Double> eventInDrivingTimeTab = new ArrayList<>();
        long eventInStationTotalTime = 0;
        long eventInCrossroadTotalTime = 0;
        long eventInDrivingTotalTime = 0;
        if (getTripDTO().getEventDTOList() != null){
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()){
                if (Objects.equals(eventDTO.getEventType(), "Evènement en station")){
                    double eventInStationTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInStationTimeTab.add(eventInStationTime);
                    eventInStationTotalTime += eventInStationTime;
                } else if (eventDTO.getEventType().equals("Evènement en carrefour")){
                    double eventInCrossroadTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInCrossroadTimeTab.add(eventInCrossroadTime);
                    eventInCrossroadTotalTime += eventInCrossroadTime;
                } else if (eventDTO.getEventType().equals("Evènement en section courante")){
                    double eventInDrivingTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInDrivingTimeTab.add(eventInDrivingTime);
                    eventInDrivingTotalTime += eventInDrivingTime;
                } else {
                    Log.e(TAG, "Impossible case");
                }
            }
        }

        long onlyTripTime = tripTime-totalTimeInStation-eventInDrivingTotalTime-eventInCrossroadTotalTime;

        mDecompositionPieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        yvalues.add(new PieEntry(onlyTripTime, 0));
        yvalues.add(new PieEntry(totalTimeInStation-eventInStationTotalTime, 1));
        yvalues.add(new PieEntry(eventInDrivingTotalTime, 2));
        yvalues.add(new PieEntry(eventInCrossroadTotalTime, 3));
        yvalues.add(new PieEntry(eventInStationTotalTime, 4));

        PieDataSet dataSet = new PieDataSet(yvalues, "Décomposition");

        final ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Trajet");
        xVals.add("Station");
        xVals.add("Evènement section courante");
        xVals.add("Evènement carrefours");
        xVals.add("Evènement station");

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
//        data.setValueFormatter(new IValueFormatter() {
//            private String[] mActivities = new String[]{"Trajet", "Station", "Evènement section courante", "Evènement carrefours", "Evènement station"};
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return mActivities[(int) value % mActivities.length];
//            }
//        });

        mDecompositionPieChart.setHoleRadius(58f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        Legend l = mDecompositionPieChart.getLegend();
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mDecompositionPieChart.setData(data);
        mDecompositionPieChart.setDrawHoleEnabled(true);
        mDecompositionPieChart.setTransparentCircleRadius(58f);
        mDecompositionPieChart.setEntryLabelColor(Color.WHITE);
        mDecompositionPieChart.setEntryLabelTextSize(12f);
        mDecompositionPieChart.setRotationEnabled(false);
        mDecompositionPieChart.setHighlightPerTapEnabled(false);
        mDecompositionPieChart.getDescription().setEnabled(false);

        mDecompositionPieChart.setOnChartValueSelectedListener(this);

        ArrayList<Double> eventTime = new ArrayList<>();
        Double eventTotalTime= 0.0;
        if (getTripDTO().getEventDTOList()!= null) {
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()) {
                double eventTimeCalculation = eventDTO.getEndTime()-eventDTO.getStartTime();
                eventTime.add(eventTimeCalculation);
                eventTotalTime += eventTimeCalculation;
            }
        }
        mLossOfTotalTimeTextView.setText(timeFormat.format(eventTotalTime) + " min");

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}

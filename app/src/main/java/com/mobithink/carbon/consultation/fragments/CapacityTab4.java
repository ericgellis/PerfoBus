package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.driving.adapters.MyXAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class CapacityTab4 extends GenericTabFragment implements OnChartValueSelectedListener {

    private TextView maxCapacityTextView;
    private TextView maxPeopleTextView;
    private TextView averagePeopleTextView;
    private TextView minPeopleTextView;
    private TextView capacityLess50TextView;

    private CombinedChart mCombinedChart;

    private String[] namesTab;
    private List<Integer> busPersonTab;


    public CapacityTab4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_capacity_tab4, container, false);

        maxCapacityTextView = (TextView) rootView.findViewById(R.id.maxCapacity);
        maxPeopleTextView = (TextView) rootView.findViewById(R.id.maxPeople);
        averagePeopleTextView = (TextView) rootView.findViewById(R.id.averagePeople);
        minPeopleTextView = (TextView) rootView.findViewById(R.id.minPeople);
        capacityLess50TextView = (TextView) rootView.findViewById(R.id.capacityLess50);
        mCombinedChart = (CombinedChart) rootView.findViewById(R.id.combined_chart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        calculateStat();

        mCombinedChart.getDescription().setEnabled(false);
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setPinchZoom(false);
        mCombinedChart.setDoubleTapToZoomEnabled(false);
        mCombinedChart.setAutoScaleMinMaxEnabled(true);
        mCombinedChart.setHighlightFullBarEnabled(false);
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        mCombinedChart.setOnChartValueSelectedListener(this);

        Legend legend = mCombinedChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        ArrayList<String> names = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            names.add(stationDataDTO.getStationName());
        }
        Log.d(TAG, "names size : " + names.size());
        namesTab = names.toArray(new String[names.size()]);


        CombinedData data = new CombinedData();

        data.setData(generateBarData());
        data.setData(generateLineData());

        XAxis xAxis = mCombinedChart.getXAxis();

        //xAxis max and min
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(data.getXMax() + 0.5f);

        //xAxis labels
        xAxis.setLabelRotationAngle(-90);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(namesTab));
        xAxis.setDrawLabels(true);
        xAxis.setAvoidFirstLastClipping(true);

        //xAxis line
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(-210.0f);

        //mCombinedChart.setVisibleXRangeMaximum(data.getXMax());
        //mCombinedChart.setVisibleYRangeMinimum(15f, YAxis.AxisDependency.LEFT);
        mCombinedChart.setDragEnabled(true);
        mCombinedChart.getXAxis().setDrawGridLines(false);
        mCombinedChart.getAxisRight().setEnabled(false);

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }

    private void calculateStat() {

        int min = 0;
        int max = 0;
        int totalPerson = 0;
        int sum = 0;
        int capacityLess50Count = 0;
        int average;
        int capacityLess50Percent;
        busPersonTab = new ArrayList<>();

        int count = 0;
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            int comeIn = stationDataDTO.getNumberOfComeIn();
            int comeOut = stationDataDTO.getNumberOfGoOut();
            totalPerson = totalPerson + comeIn - comeOut;
            if (count == 0) {
                min = totalPerson;
            } else {
                min = Math.min(min,totalPerson);
            }
            max = Math.max(max,totalPerson);
            sum = sum + totalPerson;
            if (totalPerson < getTripDTO().getVehicleCapacity() / 2) {
                capacityLess50Count++;
            }
            busPersonTab.add(totalPerson);
            count++;
        }

        average = sum/count;
        capacityLess50Percent = (capacityLess50Count*100)/(count);

        maxCapacityTextView.setText(String.valueOf(getTripDTO().getVehicleCapacity()));
        maxPeopleTextView.setText(String.valueOf(max));
        minPeopleTextView.setText(String.valueOf(min));
        averagePeopleTextView.setText(String.valueOf(average));
        capacityLess50TextView.setText(String.format("%s%%", String.valueOf(capacityLess50Percent)));
    }

    private LineData generateLineData() {
        LineData data = new LineData();

        ArrayList<Entry> allEntry = new ArrayList<>();

        int i = 0;
        for (int busPerson : busPersonTab) {
            Entry entry = new Entry(i,busPerson);
            allEntry.add(entry);
            i++;
        }

        LineDataSet set = new LineDataSet(allEntry, "nombre de personnes dans le véhicule");
        set.setColor(Color.rgb(255, 127, 39));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 127, 39));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(255, 127, 39));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(255, 127, 39));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        data.addDataSet(set);

        return data;
    }


    private BarData generateBarData() {

        ArrayList<BarEntry> allEntry = new ArrayList<>();

        int i = 0;
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            float comeIn = (float) stationDataDTO.getNumberOfComeIn();
            float comeOut = (float) (-1 * stationDataDTO.getNumberOfGoOut());
            // IMPORTANT: When using negative values in stacked bars, always make sure the negative values are in the array first
            BarEntry entry = new BarEntry(i,new float[]{comeOut, comeIn} );
            allEntry.add(entry);
            i++;
        }

        BarDataSet set = new BarDataSet(allEntry, "Charge");
        set.setDrawIcons(false);
        //set.setValueFormatter(new MyXAxisValueFormatter(namesTab));
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(Color.rgb(250, 110, 112), Color.rgb(167, 224, 165));
        set.setStackLabels(new String[]{
                "Personnes descendant du bus", "Personnes montant dans le bus"
        });

        BarData data = new BarData(set);
        data.setBarWidth(1f); // x2 dataset

        return data;

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e != null) {
            Log.i(TAG,"Value Selected : " + Math.abs(e.getX()));
        } else {
            Log.i(TAG,"null value selected");
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "NOTING SELECTED");
    }
}


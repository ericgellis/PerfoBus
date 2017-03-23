package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.data.LineData;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.driving.adapters.MyXAxisValueFormatter;


import java.util.ArrayList;


public class CapacityTab4 extends GenericTabFragment {


    TextView maxCapacityTextView;
    TextView maxPeopleTextView;
    TextView averagePeopleTextView;
    TextView minPeopleTextView;
    TextView capacityLess50TextView;
    TextView capacityLess50;

    private CombinedChart mCombinedChart;

    String[] namesTab;

    public CapacityTab4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_capacity_tab4, container, false);

        maxCapacityTextView = (TextView) rootView.findViewById(R.id.maxCapacity);
        maxPeopleTextView = (TextView) rootView.findViewById(R.id.maxPeople);
        averagePeopleTextView = (TextView) rootView.findViewById(R.id.averagePeople);
        minPeopleTextView = (TextView) rootView.findViewById(R.id.minPeople);
        capacityLess50TextView = (TextView) rootView.findViewById(R.id.capacityLess50TextView);
        capacityLess50 = (TextView) rootView.findViewById(R.id.capacityLess50);
        mCombinedChart = (CombinedChart) rootView.findViewById(R.id.combined_chart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        maxCapacityTextView.setText(String.valueOf(getTripDTO().getVehicleCapacity()));

        mCombinedChart.getDescription().setEnabled(false);
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setPinchZoom(false);
        mCombinedChart.setDoubleTapToZoomEnabled(false);
        mCombinedChart.setAutoScaleMinMaxEnabled(true);
        mCombinedChart.setHighlightFullBarEnabled(true);
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        Legend l = mCombinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        CombinedData data = new CombinedData();

        data.setData(generateBarCharts());
        //data.setData(generateinBusData());

        ArrayList<String> names = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            names.add(stationDataDTO.getStationName());
        }

        namesTab = names.toArray(new String[names.size()]);

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(namesTab));
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1f);

        xAxis.setLabelRotationAngle(-90);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mCombinedChart.setVisibleXRangeMaximum(7f);
        mCombinedChart.setVisibleYRangeMinimum(2f, YAxis.AxisDependency.LEFT);
        mCombinedChart.getCenter();
        mCombinedChart.setDragEnabled(true);
        mCombinedChart.getXAxis().setDrawGridLines(false);
        mCombinedChart.getAxisRight().setEnabled(false);
        mCombinedChart.setData(data);
        mCombinedChart.invalidate();

    }


    private BarData generateBarCharts() {

        ArrayList<BarEntry> comeInEntry = new ArrayList<BarEntry>();
        ArrayList<BarEntry> goOutEntry = new ArrayList<BarEntry>();

        int i = 0;
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            BarEntry entry = new BarEntry(i, Float.valueOf(stationDataDTO.getNumberOfComeIn()));
            comeInEntry.add(entry);
            BarEntry entry1 = new BarEntry(i, Float.valueOf(-1 * stationDataDTO.getNumberOfGoOut()));
            goOutEntry.add(entry1);
            i++;
        }

        BarDataSet set1 = new BarDataSet(comeInEntry, "Personnes montant dans le bus");
        set1.setColor(Color.rgb(167, 224, 165));
        set1.setDrawValues(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(goOutEntry, "Personnes descendant du bus");
        set2.setColor(Color.rgb(250, 110, 112));
        set2.setDrawValues(false);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.5f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;

    }

    private LineData generateinBusData() {
        return null;
    }



}


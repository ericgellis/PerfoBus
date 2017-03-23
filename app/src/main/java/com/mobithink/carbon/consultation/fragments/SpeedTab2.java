package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.driving.adapters.MyXAxisValueFormatter;


import java.util.ArrayList;


public class SpeedTab2 extends GenericTabFragment implements OnChartValueSelectedListener {

    TextView maxSpeedValueTextView;
    TextView averageSpeedValueTextView;
    TextView minSpeedValueTextView;

    private LineChart mMultiLineChart;
    String[] namesTab;

    public SpeedTab2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speed_tab2, container, false);

        maxSpeedValueTextView = (TextView) rootView.findViewById(R.id.maxSpeedValue);
        averageSpeedValueTextView = (TextView) rootView.findViewById(R.id.averageSpeedValue);
        minSpeedValueTextView = (TextView) rootView.findViewById(R.id.minSpeedValue);
        mMultiLineChart = (LineChart) rootView.findViewById(R.id.multi_line_chart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        mMultiLineChart.setOnChartValueSelectedListener(this);

        mMultiLineChart.setDrawGridBackground(false);
        mMultiLineChart.getDescription().setEnabled(false);
        mMultiLineChart.setDrawBorders(false);
        mMultiLineChart.getAxisLeft().setEnabled(true);
        mMultiLineChart.getAxisLeft().setDrawGridLines(false);
        mMultiLineChart.getAxisRight().setEnabled(false);
        mMultiLineChart.getAxisRight().setDrawAxisLine(false);
        mMultiLineChart.getAxisRight().setDrawGridLines(false);
        mMultiLineChart.setVisibleXRangeMaximum(7f);


        ArrayList<String> names = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            names.add(stationDataDTO.getStationName());
        }

        namesTab = names.toArray(new String[names.size()]);

        XAxis xAxis =  mMultiLineChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(namesTab));
        xAxis.setLabelRotationAngle(-90);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1f);


        // enable touch gestures
        //mMultiLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mMultiLineChart.setDragEnabled(false);
        //mMultiLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        //mMultiLineChart.setPinchZoom(false);

        Legend l = mMultiLineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        mMultiLineChart.setData(generateLineChart ());

    }

    private LineData generateLineChart (){

        ArrayList<Entry> maxSpeedEntry = new ArrayList<Entry>();
        ArrayList<Entry> minSpeedEntry = new ArrayList<Entry>();
        ArrayList<Entry> tripSpeedEntry = new ArrayList<>();

        int i = 0;
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            Entry entry = new Entry(i, Float.valueOf(stationDataDTO.getNumberOfComeIn()));
            maxSpeedEntry.add(entry);
            Entry entry1 = new Entry(i, Float.valueOf(stationDataDTO.getNumberOfGoOut()));
            tripSpeedEntry.add(entry1);
            Entry entry2 = new Entry(i, Float.valueOf(stationDataDTO.getNumberOfGoOut()));
            minSpeedEntry.add(entry2);
            i++;
        }

        LineDataSet set1 = new LineDataSet(maxSpeedEntry, "Vitesse maximale");
        set1.setColor(Color.rgb(167, 224, 165));
        set1.setDrawValues(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawCircles(false);
        set1.setLineWidth(3f);

        LineDataSet set2 = new LineDataSet(tripSpeedEntry, "Vitesse pendant le trajet");
        set2.setColor(Color.rgb(0, 0, 0));
        set2.setDrawValues(false);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setDrawCircles(false);
        set2.setLineWidth(3f);

        LineDataSet set3 = new LineDataSet(minSpeedEntry, "Vitesse minimale");
        set3.setColor(Color.rgb(250, 110, 112));
        set3.setDrawValues(false);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setDrawCircles(false);
        set3.setLineWidth(3f);

        LineData ld = new LineData(set1, set2, set3);

        return ld;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

}

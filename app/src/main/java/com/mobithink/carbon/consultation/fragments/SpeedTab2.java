package com.mobithink.carbon.consultation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;


public class SpeedTab2 extends GenericTabFragment implements OnChartValueSelectedListener {

    private TextView maxSpeedValueTextView;
    private TextView averageSpeedValueTextView;
    private TextView minSpeedValueTextView;

    private LineChart mMultiLineChart;
    private String[] namesTab;

    public SpeedTab2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

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

        YAxis yAxis =  mMultiLineChart.getAxisLeft();
        yAxis.setAxisMaximum(120f);
        yAxis.setAxisMinimum(0f);

        mMultiLineChart.setDragEnabled(true);

        Legend l = mMultiLineChart.getLegend();
        l.setEnabled(false);

        mMultiLineChart.setData(generateLineChart ());

    }

    private LineData generateLineChart (){

        ArrayList<Entry> maxSpeedEntry = new ArrayList<>();
        ArrayList<Entry> minSpeedEntry = new ArrayList<>();
        ArrayList<Entry> tripSpeedEntry = new ArrayList<>();

        ArrayList<Double> speedTab = new ArrayList<>() ;
        double speedAdd = 0;
        double averageSpeed;
        int i = 0;

        if (getTripDTO().getStationDataDTOList() != null) {
            for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
                speedTab.add(stationDataDTO.getSpeed());
                double maxVal = Collections.max(speedTab);
                Entry entry = new Entry(i, (float) Math.round(maxVal));
                maxSpeedEntry.add(entry);
                Entry entry1 = new Entry(i, (float) Math.round(stationDataDTO.getSpeed()));
                tripSpeedEntry.add(entry1);
                double minVal = Collections.min(speedTab);
                Entry entry2 = new Entry(i, (float) Math.round(minVal));
                minSpeedEntry.add(entry2);
                speedAdd += stationDataDTO.getSpeed();
                i++;

            }
        }

        LineDataSet set1 = new LineDataSet(maxSpeedEntry, "Vitesse maximale");
        set1.setColor(Color.rgb(191, 252, 189));
        set1.setDrawValues(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawCircles(false);
        set1.setLineWidth(2f);

        LineDataSet set2 = new LineDataSet(tripSpeedEntry, "Vitesse pendant le trajet");
        set2.setColor(Color.rgb(0, 0, 0));
        set2.setDrawValues(false);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setDrawCircles(false);
        set2.setLineWidth(3f);
        set2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineDataSet set3 = new LineDataSet(minSpeedEntry, "Vitesse minimale");
        set3.setColor(Color.rgb(255, 173, 174));
        set3.setDrawValues(false);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setDrawCircles(false);
        set3.setLineWidth(2f);

        LineData ld = new LineData(set1, set2, set3);

        double minSpeed = Collections.min(speedTab);
        double maxSpeed = Collections.max(speedTab);

        minSpeedValueTextView.setText(String.valueOf(Math.round(minSpeed))+" km/h");
        maxSpeedValueTextView.setText(String.valueOf(Math.round(maxSpeed))+" km/h");

        averageSpeed = speedAdd/getTripDTO().getStationDataDTOList().size();
        averageSpeedValueTextView.setText(String.valueOf(Math.round(averageSpeed))+" km/h");

        return ld;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

}

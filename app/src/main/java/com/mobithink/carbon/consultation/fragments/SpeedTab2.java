package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;


public class SpeedTab2 extends GenericTabFragment {

    TextView maxSpeedValueTextView;
    TextView averageSpeedValueTextView;
    TextView minSpeedValueTextView;

    LinearLayout speedChartLayout;

    GraphicalView speedChartGraphicalView;

    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[] {new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0), new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 1), new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 2)};
    // Creating a dataset to hold each series
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    // Creating a XYMultipleSeriesRenderer to customize the whole chart
    XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

    public SpeedTab2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speed_tab2, container, false);

        maxSpeedValueTextView = (TextView) rootView.findViewById(R.id.maxSpeedValue);
        averageSpeedValueTextView = (TextView) rootView.findViewById(R.id.averageSpeedValue);
        minSpeedValueTextView = (TextView) rootView.findViewById(R.id.minSpeedValue);

        speedChartLayout = (LinearLayout) rootView.findViewById(R.id.speedChartLayout);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        if (speedChartGraphicalView == null) {
            initCombinedChart();
            speedChartGraphicalView = ChartFactory.getCombinedXYChartView(getContext(), dataset, multiRenderer, types);
            speedChartLayout.addView(speedChartGraphicalView);
        } else {
            speedChartGraphicalView.repaint();
        }
    }

    public void initCombinedChart() {

        String[] mStationName;
        Integer[] mStationNumber;

        Integer[] maxSpeed;
        Integer[] minSpeed;
        Integer[] tripSpeed;

        ArrayList<String> names = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            names.add(stationDataDTO.getStationName());
        }
        mStationName = new String[names.size()];
        mStationName = names.toArray(mStationName);

        ArrayList<Integer> stationNb = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            stationNb.add(stationDataDTO.getStationStep());
        }
        mStationNumber = new Integer[stationNb.size()];
        mStationNumber = stationNb.toArray(mStationNumber);



        // Creating an  XYSeries for maximal speed
        XYSeries maxSpeedSeries = new XYSeries("Vitesse maximale");
        // Creating an  XYSeries for minimal speed
        XYSeries minSpeedSeries = new XYSeries("Vitesse minimale");
        // Creating an  XYSeries for bus speed for trip
        XYSeries speedSeries = new XYSeries("Vitesse pendant le trajet");

        // Adding data to XYSeries for maximal speed, XYSeries for minimal speed, XYSeries for bus speed for trip
        /*for (int i = 0; i < mStationNumber.length; i++) {
            maxSpeedSeries.add(mStationNumber[i], maxSpeed[i]);
            minSpeedSeries.add(mStationNumber[i], minSpeed[i]);
            speedSeries.add(mStationNumber[i], tripSpeed[i]);
        }*/

        // Adding Series to the dataset
        dataset.addSeries(maxSpeedSeries);
        dataset.addSeries(minSpeedSeries);
        dataset.addSeries(speedSeries);

        // Creating XYSeriesRenderer to customize maxSpeedSeries
        XYSeriesRenderer maxSpeedRenderer = new XYSeriesRenderer();
        maxSpeedRenderer.setColor(R.color.green_chart);
        maxSpeedRenderer.setLineWidth(2);
        maxSpeedRenderer.setDisplayChartValues(true);

        // Creating XYSeriesRenderer to customize minSpeedSeries
        XYSeriesRenderer minSpeedRenderer = new XYSeriesRenderer();
        minSpeedRenderer.setColor(R.color.red_chart);
        minSpeedRenderer.setLineWidth(2);
        minSpeedRenderer.setDisplayChartValues(true);

        // Creating XYSeriesRenderer to customize busSpeedSeries
        XYSeriesRenderer busSpeedRenderer = new XYSeriesRenderer();
        busSpeedRenderer.setColor(R.color.black);
        busSpeedRenderer.setLineWidth(2);
        busSpeedRenderer.setDisplayChartValues(true);


        multiRenderer.setXLabels(0);
        multiRenderer.setXTitle("km");
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        for(int i=0;i<mStationNumber.length;i++){
            multiRenderer.addXTextLabel(i, mStationName[i]);
        }

        multiRenderer.addSeriesRenderer(maxSpeedRenderer);
        multiRenderer.addSeriesRenderer(minSpeedRenderer);
        multiRenderer.addSeriesRenderer(busSpeedRenderer);
    }

}

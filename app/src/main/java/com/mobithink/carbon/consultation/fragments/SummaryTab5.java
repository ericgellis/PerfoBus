package com.mobithink.carbon.consultation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class SummaryTab5 extends GenericTabFragment {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;
    TextView mDirectionNameTextView;
    TextView mEnteredTimeTextView;
    TextView mEnteredDateTextView;

    TripDTO tripDTO;
    CityDTO cityDTO;
    BusLineDTO busLineDTO;
    StationDTO mDirection;

    RadarChart mRadarChart;

    Button mPDFButton;

    public SummaryTab5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_summary_tab5, container, false);

        mWeatherImageView = (ImageView) rootView.findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) rootView.findViewById(R.id.weatherTemperatureTextView);
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

        mPDFButton = (Button) rootView.findViewById(R.id.pdfButton);
        mPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPDFByEmail();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        Picasso.with(getContext()).load(getTripDTO().getWeather()).into(mWeatherImageView);

        mWeatherTemperatureTextView.setText(getTripDTO().getTemperature());
        //mCityNameTextView.setText(busLineDTO.getCityDto().getName());
        //mLineNameTextView.setText(busLineDTO.getName());
        //mDirectionNameTextView.setText(mDirection.getStationName());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        mEnteredTimeTextView.setText(timeFormat.format(getTripDTO().getStartTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM", Locale.FRANCE);
        mEnteredDateTextView.setText(dateFormat.format(getTripDTO().getStartTime()));
    }

    public void sendPDFByEmail() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
        final EditText edittext = new EditText(getContext());
        alertDialog.setTitle("Générer un rapport PDF ?");
        alertDialog.setView(edittext);
        alertDialog.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alertDialog.show();
    }

    public void setData(){

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(4f, 1));
        entries.add(new RadarEntry(5f, 2));
        entries.add(new RadarEntry(2f, 3));
        entries.add(new RadarEntry(4f, 4));
        entries.add(new RadarEntry(1f, 5));
        entries.add(new RadarEntry(5f, 6));
        entries.add(new RadarEntry(3f, 7));

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

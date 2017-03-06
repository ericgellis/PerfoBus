package com.mobithink.carbon.consultation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class SummaryTab5 extends GenericTabFragment {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mAtmoNumberTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;
    TextView mDirectionNameTextView;
    TextView mEnteredTimeTextView;
    TextView mEnteredDateTextView;

    TripDTO tripDTO;
    CityDTO cityDTO;
    BusLineDTO busLineDTO;
    StationDTO mDirection;

    Button mPDFButton;

    public SummaryTab5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_summary_tab5, container, false);

        mWeatherImageView = (ImageView) rootView.findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) rootView.findViewById(R.id.weatherTemperatureTextView);
        mAtmoNumberTextView = (TextView) rootView.findViewById(R.id.atmoNumberTextView);
        mCityNameTextView = (TextView) rootView.findViewById(R.id.cityNameTextView);
        mLineNameTextView = (TextView) rootView.findViewById(R.id.lineNameTextView);
        mDirectionNameTextView = (TextView) rootView.findViewById(R.id.directionNameTextView);
        mEnteredTimeTextView = (TextView) rootView.findViewById(R.id.enteredTimeTextView);
        mEnteredDateTextView = (TextView) rootView.findViewById(R.id.enteredDateTextView);

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
        mAtmoNumberTextView.setText(String.valueOf(getTripDTO().getAtmo()));
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

}

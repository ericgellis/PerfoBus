package com.mobithink.carbon.consultation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.Locale;


public class SummaryTab5 extends Fragment {

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

        mWeatherImageView = (ImageView) container.findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) container.findViewById(R.id.weatherTemperatureTextView);
        mAtmoNumberTextView = (TextView) container.findViewById(R.id.atmoNumberTextView);
        mCityNameTextView = (TextView) container.findViewById(R.id.cityNameTextView);
        mLineNameTextView = (TextView) container.findViewById(R.id.lineNameTextView);
        mDirectionNameTextView = (TextView) container.findViewById(R.id.directionNameTextView);
        mEnteredTimeTextView = (TextView) container.findViewById(R.id.enteredTimeTextView);
        mEnteredDateTextView = (TextView) container.findViewById(R.id.enteredDateTextView);

        mPDFButton = (Button) container.findViewById(R.id.pdfButton);
        /*mPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPDFByEmail();
            }
        });*/

        return inflater.inflate(R.layout.fragment_summary_tab5, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*mWeatherTemperatureTextView.setText(tripDTO.getTemperature());
        mAtmoNumberTextView.setText(tripDTO.getAtmo());
        mCityNameTextView.setText(busLineDTO.getCityDto().getName());
        mLineNameTextView.setText(busLineDTO.getName());
        mDirectionNameTextView.setText(mDirection.getStationName());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        mEnteredTimeTextView.setText(timeFormat.format(tripDTO.getStartTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM", Locale.FRANCE);
        mEnteredDateTextView.setText(dateFormat.format(tripDTO.getStartTime()));*/
    }

    public void sendPDFByEmail(){
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

package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.R;


public class EventTab3 extends Fragment {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mAtmoNumberTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;
    TextView mDirectionNameTextView;
    TextView mEnteredTimeTextView;
    TextView mEnteredDateTextView;
    TextView mCourseDetailsTextView;
    TextView mLossDetailsTextView;
    TextView mSectionDetailsTextView;
    TextView mLossSectionDetailsTextView;
    TextView mDetourDistanceTextView;

    Button mPDFButton;

    public EventTab3() {

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
        mCourseDetailsTextView = (TextView) container.findViewById(R.id.courseDetailsTextView);
        mLossDetailsTextView = (TextView) container.findViewById(R.id.lossDetailsTextView);
        mSectionDetailsTextView = (TextView) container.findViewById(R.id.sectionDetailsTextView);
        mLossSectionDetailsTextView = (TextView) container.findViewById(R.id.lossSectionDetailsTextView);
        mDetourDistanceTextView = (TextView) container.findViewById(R.id.detourDistanceTextView);

        mPDFButton = (Button) container.findViewById(R.id.pdfButton);
        /*mPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPDFByEmail();
            }
        });*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_tab3, container, false);
    }

    public void sendPDFByEmail(){

        /*AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setCancelable(true);
        alert.setTitle("Générer un rapport PDF ?");

        // Create EditText for entry
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                Toast.makeText(getActivity(), "PDF envoyé", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
             alert.cancel();
            }
        });

        alert.show();*/
    }


}

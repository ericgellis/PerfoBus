package com.mobithink.carbon.driving;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.driving.adapters.StationAdapter;
import com.mobithink.carbon.services.LocationService;
import com.mobithink.carbon.station.StationActivity;
import com.mobithink.carbon.utils.CarbonUtils;

import java.util.List;

/**
 * Created by mplaton on 01/02/2017.
 */

public class DrivingActivity extends Activity {

    ImageView mWeatherImageView;

    TextView mWeatherTemperatureTextView;
    TextView mActualTime;
    TextView mActualDate;
    TextView mAtmoNumberTextView;
    TextView mNextStationNameTextView;

    Toolbar mDrivingToolBar;
    TextView mDirectionNameTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;

    Button mCancelButton;
    Button mEventButton;

    Chronometer mCourseChronometer;
    Chronometer mSectionChronometer;

    RecyclerView mStationRecyclerView;
    StationAdapter mStationAdapter;

    RelativeLayout mNextStationRelativeLayout;

    List<StationDTO> mStationList;
    StationDTO mDirection;
    CityDTO mCity;
    FragmentManager fm = getFragmentManager();
    private BusLineDTO mLine;
    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving);

        final View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(CarbonUtils.dpToPx(70));

        mDrivingToolBar = (Toolbar) findViewById(R.id.drivingToolBar);
        mDirectionNameTextView = (TextView) findViewById(R.id.directionNameTextView);
        mCityNameTextView = (TextView) findViewById(R.id.cityNameTextView);
        mLineNameTextView = (TextView) findViewById(R.id.lineNameTextView);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        mStationRecyclerView = (RecyclerView) findViewById(R.id.station_recyclerview);
        mStationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStationAdapter = new StationAdapter();
        mStationRecyclerView.setAdapter(mStationAdapter);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        mWeatherImageView = (ImageView) findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) findViewById(R.id.weatherTemperatureTextView);
        mActualTime = (TextView) findViewById(R.id.actualTime);
        mActualDate = (TextView) findViewById(R.id.actualDate);
        mAtmoNumberTextView = (TextView) findViewById(R.id.atmoNumberTextView);

        //Chrono
        mCourseChronometer = (Chronometer) findViewById(R.id.chronometerCourse);
        mSectionChronometer = (Chronometer) findViewById(R.id.chronometerSection);

        mNextStationNameTextView = (TextView) findViewById(R.id.nextStationNameTextView);

        mEventButton = (Button) findViewById(R.id.eventButton);
        mEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChooseEvent();
            }
        });

        mNextStationRelativeLayout = (RelativeLayout) findViewById(R.id.nextStationRelativeLayout);
        mNextStationNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStationPage();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStationList = (List<StationDTO>) extras.getSerializable("listStation");
            if(mStationList.size()>0){
                mStationAdapter.setData(mStationList);
                mStationAdapter.notifyDataSetChanged();
            }
            mCity = (CityDTO) extras.getSerializable("city");
            mDirection = (StationDTO) extras.getSerializable("direction");
            mLine = (BusLineDTO) extras.getSerializable("line");
        }

        startService(new Intent(getApplicationContext(),LocationService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCityNameTextView.setText(mCity.getName());
        mLineNameTextView.setText(mLine.getName());
        mDirectionNameTextView.setText(mDirection.getStationName());
        mNextStationNameTextView.setText(mStationList.get(0).getStationName());

        mWeatherImageView.setImageResource(R.drawable.meteo);
        mWeatherTemperatureTextView.setText("12°C");
        mActualTime.setText("13:34");
        mActualDate.setText ("Lun. 9 Janv.");
        mAtmoNumberTextView.setText("5");

        mCourseChronometer.start();
        mSectionChronometer.start();
    }

    public void goToStationPage(){
        Intent toStationPage = new Intent (this, StationActivity.class);
        this.startActivity(toStationPage);

    }

    public void deleteData(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Supprimer la saisie ?");
        alertDialogBuilder.setMessage("Toutes les données de la saisie seront perdues. Vous ne pourrez pas annuler cette action.");
        alertDialogBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void goToChooseEvent(){

        EventDialogFragment dialogFragment = new EventDialogFragment();
        dialogFragment.show(fm, "Choisir un évènement");
    }
}

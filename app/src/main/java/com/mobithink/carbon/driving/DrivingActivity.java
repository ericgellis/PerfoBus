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
import android.widget.Toast;

import com.mobithink.carbon.R;
import com.mobithink.carbon.SplashScreenActivity;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.mobithink.carbon.driving.adapters.StationAdapter;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.services.LocationService;
import com.mobithink.carbon.services.WeatherService;
import com.mobithink.carbon.services.WeatherServiceCallback;
import com.mobithink.carbon.services.weatherdata.Channel;
import com.mobithink.carbon.services.weatherdata.Item;
import com.mobithink.carbon.station.StationActivity;
import com.mobithink.carbon.utils.CarbonUtils;
import com.mobithink.carbon.webservices.TripService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 01/02/2017.
 */

public class DrivingActivity extends Activity implements WeatherServiceCallback {

    private static final String TAG = "DrivingActivity";

    private WeatherService weatherService;

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
                showQuitConfirmationDialog();
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

        weatherService = new WeatherService(this);
        weatherService.refreshWeather(mCity.getName() + ", France");

        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCityNameTextView.setText(mCity.getName());
        mLineNameTextView.setText(mLine.getName());
        mDirectionNameTextView.setText(mDirection.getStationName());
        mNextStationNameTextView.setText(mStationList.get(0).getStationName());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE. d MMM.");
        String dateString = dateFormat.format(c.getTime());
        mActualDate.setText (dateString);

        mActualTime.setText("13:34");

        mAtmoNumberTextView.setText("5");

        mCourseChronometer.start();
        mSectionChronometer.start();
    }

    public void goToStationPage(){

        StationDataDTO stationDataDTO = new StationDataDTO();
        stationDataDTO.setStationName( mNextStationNameTextView.getText().toString());
        long stationStartTime = System.currentTimeMillis();
        stationDataDTO.setStartTime(stationStartTime);
        long stationId = DatabaseManager.getInstance().createNewStation(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);

        Bundle bundle = new Bundle();
        bundle.putSerializable("stationId", stationId);
        bundle.putSerializable("stationName", stationDataDTO.getStationName());
        bundle.putSerializable("stationStep", mStationAdapter.getStep());
        bundle.putSerializable("stationStartTime", stationStartTime);
        Intent toStationPage = new Intent (this, StationActivity.class);
        toStationPage.putExtras(bundle);
        this.startActivity(toStationPage);

    }

    private void showQuitConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Supprimer la saisie ?");
        alertDialogBuilder.setMessage("Toutes les données de la saisie seront perdues. Vous ne pourrez pas annuler cette action.");
        alertDialogBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopTrip();
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

    public void stopTrip() {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setEndTime(System.currentTimeMillis());
        tripDTO.setAtmo(Integer.parseInt(mAtmoNumberTextView.getText().toString()));
        if (mWeatherTemperatureTextView != null){
            tripDTO.setTemperature(mWeatherTemperatureTextView.getText().toString());}
        else {
            tripDTO.setTemperature("200");
        }

        long tripId = DatabaseManager.getInstance().updateTrip(CarbonApplicationManager.getInstance().getCurrentTripId(), tripDTO);

        stopService(new Intent(this, LocationService.class));
        sendTripDto(tripId);
    }

    @Override
    public void onBackPressed() {
        //intercept backpress
    }

    private void sendTripDto(long tripId) {
        TripDTO tripDto = DatabaseManager.getInstance().getFullTripDTODataToSend(tripId);

        TripService tripService = RetrofitManager.build().create(TripService.class);

        Call<Void> call = tripService.register(tripDto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 201:
                        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    public void goToChooseEvent(){

        EventDialogFragment dialogFragment = new EventDialogFragment();
        dialogFragment.show(fm, "Choisir un évènement");
    }

    @Override
    public void ServiceSuccess(Channel channel) {

        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());
        mWeatherImageView.setImageResource(resourceId);
        mWeatherTemperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
    }

    @Override
    public void ServiceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();

    }
}

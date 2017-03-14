package com.mobithink.carbon.driving;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mplaton on 01/02/2017.
 */

public class DrivingActivity extends Activity implements WeatherServiceCallback, LocationListener, SensorEventListener {

    private static final String TAG = "DrivingActivity";

    private static final int ANALYSE_STATION_ACTION = 7;
    private static final int SHAKE_THRESHOLD = 2300;

    View mRootView;
    ImageView mWeatherImageView;
    TextView mWeatherTemperatureTextView;
    TextView mActualTime;
    TextView mActualDate;
    TextView mSpeedTextView;
    TextView mNextStationTextView;
    TextView mNextStationNameTextView;
    Toolbar mDrivingToolBar;
    TextView mDirectionNameTextView;
    TextView mCityNameTextView;
    TextView mLineNameTextView;
    Button mCancelButton;
    Button mEventButton;
    Button mAddUnexpectedStation;
    Chronometer mCourseChronometer;
    Chronometer mSectionChronometer;
    RecyclerView mStationRecyclerView;
    StationAdapter mStationAdapter;
    RelativeLayout mNextStationRelativeLayout;
    List<StationDTO> mStationList;
    StationDTO mDirection;
    CityDTO mCity;
    FragmentManager fm = getFragmentManager();
    int resourceId;
    int step = 0;
    private Button mUnrealizedStopButton;
    private WeatherService weatherService;
    private BusLineDTO mLine;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private float currentSpeed = 0.0f;
    private long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_driving);

        mRootView = findViewById(R.id.rootview);

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

        mNextStationTextView = (TextView) findViewById(R.id.nextStationTextView);
        mWeatherImageView = (ImageView) findViewById(R.id.weatherImageView);

        mWeatherTemperatureTextView = (TextView) findViewById(R.id.weatherTemperatureTextView);
        mActualTime = (TextView) findViewById(R.id.actualTime);
        mActualDate = (TextView) findViewById(R.id.actualDate);
        mSpeedTextView = (TextView) findViewById(R.id.speedTextView);

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
                if (step < mStationList.size()) {
                    goToStationPage(false);
                } else {
                    endTrip();
                }
            }
        });

        mAddUnexpectedStation = (Button) findViewById(R.id.add_unexpectedstation_button);
        mAddUnexpectedStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStationPage(true);
            }
        });

        mUnrealizedStopButton = (Button) findViewById(R.id.unrealizedStopButton);
        mUnrealizedStopButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                skipStation();
                return false;
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        turnOnGps();

        mCityNameTextView.setText(mCity.getName());
        mLineNameTextView.setText(mLine.getName());
        mDirectionNameTextView.setText(mDirection.getStationName());

        updateNextStationName();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM", Locale.FRANCE);
        String dateString = dateFormat.format(c.getTime());
        mActualDate.setText(dateString);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeString = timeFormat.format(c.getTime());
        mActualTime.setText(timeString);

        mCourseChronometer.start();
        mSectionChronometer.start();
    }

    private void updateNextStationName() {
        if (step < mStationList.size()) {
            mNextStationNameTextView.setText(mStationList.get(step).getStationName());
        } else {
            mNextStationTextView.setVisibility(View.GONE);
            mNextStationNameTextView.setText("Terminus");
            mUnrealizedStopButton.setVisibility(View.GONE);
        }
    }

    public void skipStation() {

        StationDataDTO stationDataDTO = new StationDataDTO();

        stationDataDTO.setStationName(mNextStationNameTextView.getText().toString());
        long stationId = DatabaseManager.getInstance().createNewStation(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);
        stationDataDTO.setId(stationId);
        stationDataDTO.setEndTime(System.currentTimeMillis());
        stationDataDTO.setEndTime(System.currentTimeMillis());

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            stationDataDTO.setGpsLat((long) location.getLatitude());
            stationDataDTO.setGpsLong((long) location.getLongitude());

        }

        DatabaseManager.getInstance().updateStationData(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);

        makeStep();

    }

    public void goToStationPage(boolean isUnexpectedStation) {

        StationDataDTO stationDataDTO = new StationDataDTO();
        if (!isUnexpectedStation) {
            stationDataDTO.setStationName(mNextStationNameTextView.getText().toString());
        } else {
            stationDataDTO.setStationName("Arrêt imprévu");
        }
        long stationStartTime = System.currentTimeMillis();
        stationDataDTO.setStartTime(stationStartTime);
        long stationId = DatabaseManager.getInstance().createNewStation(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isUnexpected", isUnexpectedStation);
        bundle.putSerializable("stationId", stationId);
        bundle.putSerializable("stationName", stationDataDTO.getStationName());
        bundle.putSerializable("stationStep", mStationAdapter.getStep());
        bundle.putSerializable("stationStartTime", stationStartTime);
        Intent toStationPage = new Intent (this, StationActivity.class);
        toStationPage.putExtras(bundle);
        this.startActivityForResult(toStationPage, ANALYSE_STATION_ACTION);

    }

    private void showQuitConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Supprimer la saisie ?");
        alertDialogBuilder.setMessage("Toutes les données de la saisie seront perdues. Vous ne pourrez pas annuler cette action.");
        alertDialogBuilder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTrip();
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

    public void endTrip() {
        TripDTO tripDTO = new TripDTO();

        tripDTO.setTemperature(mWeatherTemperatureTextView.getText().toString());
        tripDTO.setEndTime(System.currentTimeMillis());
        tripDTO.setWeather(Integer.toString(resourceId));
        tripId = DatabaseManager.getInstance().updateTrip(CarbonApplicationManager.getInstance().getCurrentTripId(), tripDTO);

        stopService(new Intent(this, LocationService.class));

        showSendDataDialog();
    }

    private void showSendDataDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Saisie terminé");
        alertDialogBuilder.setMessage("Fin de saisie pour la ligne, souhaitez vous envoyer les données au serveur ?");
        alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendTripDto(tripId);
                dialog.cancel();
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
                        setResult(RESULT_OK);
                        finish();
                        break;
                    default:
                        Snackbar.make(mRootView, "L'envoi de donnée à échoué, veuillez réésayer", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(mRootView, "L'envoi de donnée à échoué, veuillez réésayer", Snackbar.LENGTH_LONG).show();
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
        resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());
        mWeatherImageView.setImageResource(resourceId);
        mWeatherTemperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
    }

    @Override
    public void ServiceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ANALYSE_STATION_ACTION && resultCode == Activity.RESULT_OK) {
            makeStep();
        }
    }

    private void makeStep() {
        step++;
        updateNextStationName();
        if (step < mStationList.size()) {
            mStationAdapter.makeStep();
            mStationAdapter.notifyDataSetChanged();
            mSectionChronometer.stop();
            mSectionChronometer.start();
        }
    }

    public void deleteTrip() {
        DatabaseManager.getInstance().deleteTrip(CarbonApplicationManager.getInstance().getCurrentTripId());
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        currentSpeed = location.getSpeed() * 3.6f;
        mSpeedTextView.setText(new DecimalFormat("#.##").format(currentSpeed));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void turnOnGps() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed < SHAKE_THRESHOLD) {
                    if (currentSpeed > 0) {
                        currentSpeed = currentSpeed - 1;
                    } else {
                        currentSpeed = 0;
                    }
                    mSpeedTextView.setText(new DecimalFormat("#.##").format(currentSpeed) + " km/h");

                } else {

                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

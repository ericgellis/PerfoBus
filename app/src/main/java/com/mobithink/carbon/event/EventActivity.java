package com.mobithink.carbon.event;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.driving.DrivingActivity;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * Created by mplaton on 13/02/2017.
 */

public class EventActivity extends Activity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int UPDATE_LOCATION_INTERVAL = 10000;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    MapView mEventMap;
    Button mCancelledTimecodeButton;
    Button mVoiceMemoButton;
    Button mEventPhotoButton;
    Button mConfirmPositionEventButton;
    EventDTO eventDTO;
    long eventId;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Error: " + what + ", " + extra);
        }
    };
    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Warning: " + what + ", " + extra);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        mEventMap = (MapView) findViewById(R.id.event_map_mapview);
        mEventMap.onCreate(savedInstanceState);
        mEventMap.onResume();

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEventMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                mGoogleMap = mMap;
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                mGoogleMap.setBuildingsEnabled(true);

                enableLocationForGoogleMap();
            }
        });

        eventDTO = new EventDTO();
        Bundle extras = getIntent().getExtras();
        eventId = (long) extras.getSerializable("eventId");
        eventDTO.setId(eventId);


        mCancelledTimecodeButton = (Button) findViewById(R.id.cancelled_timecode_button);
        mVoiceMemoButton = (Button) findViewById(R.id.voice_memo_button);
        mEventPhotoButton = (Button) findViewById(R.id.event_photo_button);
        mConfirmPositionEventButton = (Button) findViewById(R.id.confirm_position_event_button);

        mCancelledTimecodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEvent();
            }
        });
        mVoiceMemoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        AppLog.logString("Start Recording");
                        startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        AppLog.logString("stop Recording");
                        stopRecording();
                        break;
                }
                return false;
            }
        });
        mEventPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        mConfirmPositionEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEvent();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onLocationAuthorizationGranted() {

        enableLocationForGoogleMap();
        requestLocationUpdate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        /*mGoogleMap.addMarker(new MarkerOptions().position(
                new LatLng(eventDTO.getEventLocation().getLatitude(), eventDTO.getEventLocation().getLongitude())).icon(
                BitmapDescriptorFactory.defaultMarker()));*/


    }

    private void enableLocationForGoogleMap() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        BFConstant.PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdate();
    }

    private void requestLocationUpdate(){

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_LOCATION_INTERVAL);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("BFMapFragment", "GoogleApiClient connection has been suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        moveCamera(location);

    }

    private void moveCamera(Location location) {

        if (mGoogleMap != null) {
            LatLng latlong = new LatLng(location.getLatitude(),
                    location.getLongitude());

            CameraPosition newCamPos = new CameraPosition(latlong,
                    15.5f,
                    mGoogleMap.getCameraPosition().tilt, //use old tilt
                    mGoogleMap.getCameraPosition().bearing); //use old bearing
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1250, null);
        }
    }

    public void cancelEvent(){

        //DatabaseManager.getInstance().updateEvent(CarbonApplicationManager.getInstance().getCurrentEventId());
        Toast toast = Toast.makeText(this, "Incident supprimé", Toast.LENGTH_SHORT);
        toast.show();

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);
    }

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording(){
        if(null != recorder){
            recorder.stop();
            recorder.reset();
            recorder.release();

            recorder = null;
        }
    }


    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void confirmEvent(){

        eventDTO.setId(eventId);
        eventDTO.setEndTime(System.currentTimeMillis());

        DatabaseManager.getInstance().updateEvent(CarbonApplicationManager.getInstance().getCurrentTripId(),eventDTO);

        Log.i(TAG, "onChildClick: event registered");

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);
    }
}

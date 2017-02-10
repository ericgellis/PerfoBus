package com.mobithink.carbon.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mobithink.carbon.managers.PreferenceManager;

/**
 * Created by jpaput on 06/02/2017.
 */
public class LocationService extends Service implements LocationListener {

    private static final String TAG = "LocationService";
    private static LocationService instance = null;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean locationServiceAvailable  = false;

    Location mLastKnowlocation; // mLastKnowlocation
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meter

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private long mTimeInterval = -1;
    private LocationManager mLocationManager;


    public static LocationService getLocationManager(Context context)     {
        if (instance == null) {
            instance = new LocationService(context);
        }
        return instance;
    }

    private LocationService(Context context) {
        initLocationService(context);
    }

    private void initLocationService(Context context) {

        if (
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try   {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled)    {
                this.locationServiceAvailable = false;
            }
            {
                this.locationServiceAvailable = true;

                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            getTimeInterval(),
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)  {
                        mLastKnowlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }else if(isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                getTimeInterval(),
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null)   {
                            mLastKnowlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                }
            }
        } catch (Exception ex)  {

        }
    }

    private long getTimeInterval() {
        if(mTimeInterval == -1){
            mTimeInterval =  PreferenceManager.getInstance().getTimeFrequency() * 1000;
        }

        return mTimeInterval;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(this.getClass().getName(), "New location ! long  : " + location.getLongitude() + ", lat : " +location.getLatitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //TODO Store Location in Database

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, PreferenceManager.getInstance().getTimeFrequency() * 1000, 10,
                    this);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, PreferenceManager.getInstance().getTimeFrequency() * 1000, 10,
                    this);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
                if (
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.removeUpdates(this);

                    Log.i(TAG, "fail to remove location listners, ignore");

            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}

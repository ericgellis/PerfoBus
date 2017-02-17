package com.mobithink.carbon.managers;

import android.location.Location;

import com.mobithink.carbon.database.model.TripDTO;

/**
 * Created by jpaput on 06/02/2017.
 */

public class CarbonApplicationManager {

    private static CarbonApplicationManager mInstance;

    private Location mLastKnowLocation;

    private long mCurrentTripId = -1;

    public static CarbonApplicationManager getInstance() {

        if (mInstance == null)
        {
            mInstance = new CarbonApplicationManager();
        }
        return mInstance;

    }

    public void clearApp() {
        //TODO
    }

    public void init() {
        PreferenceManager.getInstance();
        DatabaseManager.getInstance();
        ServiceManager.getInstance();
    }

    public TripDTO getCurrentTrip(){
        return DatabaseManager.getInstance().getTrip(mCurrentTripId);
    }

    public long getCurrentTripId() {
        return mCurrentTripId;
    }

    public void setCurrentTripId(long tripId) {
        mCurrentTripId = tripId;
    }

    public Location getLastKnowLocation() {
        return mLastKnowLocation;
    }

    public void setLastKnowLocation(Location location) {
        mLastKnowLocation = location;
    }

}

package com.mobithink.carbon.managers;

import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;

/**
 * Created by jpaput on 06/02/2017.
 */

public class CarbonApplicationManager {

    private static CarbonApplicationManager mInstance;

    private long mCurrentTripId = -1;

    private String mCurrentStationDataName = null;

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

    public void setCurrentTripId(long tripId) {
        mCurrentTripId = tripId;
    }

    public TripDTO getCurrentTrip(){
        return DatabaseManager.getInstance().getTrip(mCurrentTripId);
    }

    public long getCurrentTripId() {
        return mCurrentTripId;
    }


    public void setCurrentStationDataName(String stationDataDTOName) {
        mCurrentStationDataName = stationDataDTOName;}

    public StationDataDTO getCurrentStationData(){
        return DatabaseManager.getInstance().getStationData(mCurrentStationDataName);
    }

    public String getCurrentStationDataName(){
        return mCurrentStationDataName;
    }
}

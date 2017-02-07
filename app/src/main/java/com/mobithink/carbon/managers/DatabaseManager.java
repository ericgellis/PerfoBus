package com.mobithink.carbon.managers;

import com.mobithink.carbon.CarbonApplication;
import com.mobithink.carbon.database.DatabaseHelper;
import com.mobithink.carbon.database.model.Trip;

/**
 * Created by jpaput on 06/02/2017.
 */

public class DatabaseManager {

    private static DatabaseManager mInstance;

    private static DatabaseHelper mDataBase;

    public static DatabaseManager getInstance() {
        if (mInstance == null)
        {
            mInstance = new DatabaseManager();
            mDataBase = new DatabaseHelper(CarbonApplication.getInstance());
        }
        return mInstance;
    }

    public DatabaseHelper getDataBase(){
        return mDataBase;
    }

    /*************** Trip **************/

    public void startNewTrip(int lineID){
        long tripId = mDataBase.createTrip(lineID);
        CarbonApplicationManager.getInstance().setCurrentTripId(tripId);
    }

    public Trip getTrip(long tripId){
        return mDataBase.getTrip(tripId);
    }


    public void updateTrip(Trip trip){
        mDataBase
                .updateTrip(trip);
    }

    public void finishCurrentTrip(){
        mDataBase.finishCurrentTrip(
                CarbonApplicationManager.getInstance().getCurrentTripId());
    }

}

package com.mobithink.carbon.managers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.mobithink.carbon.CarbonApplication;
import com.mobithink.carbon.database.DatabaseHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.TripDTO;

/**
 * Created by jpaput on 06/02/2017.
 */

public class DatabaseManager {

    private static DatabaseManager mInstance;

    private static DatabaseHelper mDataBase;
    private SQLiteDatabase openedDatabase;

    public static DatabaseManager getInstance() {
        if (mInstance == null)
        {
            mInstance = new DatabaseManager();
            mDataBase = new DatabaseHelper(CarbonApplication.getInstance());
        }
        return mInstance;
    }

    public void open(){

         openedDatabase = mDataBase.getWritableDatabase();
    }

    private SQLiteDatabase getOpenedDatabase(){
        if(openedDatabase == null){
            open();
        }
        return  openedDatabase;
    }

    /*************** TripDTO **************/

    public void startNewTrip(long lineID){

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_LINE_ID, lineID);
        values.put(DatabaseHelper.KEY_START_DATETIME, getDateTime());

        // insert row
        long tripId = getOpenedDatabase().insert(DatabaseHelper.TABLE_TRIP, null, values);

        CarbonApplicationManager.getInstance().setCurrentTripId(tripId);
    }

    public TripDTO getTrip(long tripId){
        return mDataBase.getTrip(tripId);
    }


    public void updateTrip(TripDTO tripDTO){
        mDataBase
                .updateTrip(tripDTO);
    }

    public void finishCurrentTrip(){


        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_END_DATETIME, getDateTime());

        getOpenedDatabase().update(
                DatabaseHelper.TABLE_TRIP, values, DatabaseHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(CarbonApplicationManager.getInstance().getCurrentTripId())});

    }

    /*************************** Utils *****************************/
    private Long getDateTime() {
        return System.currentTimeMillis();
    }



    /*************************** EVENT **************************************/

   public void startNewEvent (Long tripID){

       /*ContentValues values = new ContentValues();
       values.put(DatabaseHelper.KEY_TRIP_ID, tripID);
       values.put(DatabaseHelper.KEY_EVENT_NAME, eventDTO.getEventName());
       values.put(DatabaseHelper.KEY_START_DATETIME, getDateTime());
       values.put(DatabaseHelper.KEY_LATITUDE,);
       values.put(DatabaseHelper.KEY_LONGITUDE,);

       long eventId = getOpenedDatabase().insert(DatabaseHelper.TABLE_EVENT, null, values);*/


       return;
   }

}

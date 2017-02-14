package com.mobithink.carbon.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobithink.carbon.CarbonApplication;
import com.mobithink.carbon.database.DatabaseHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;

import static com.mobithink.carbon.database.DatabaseHelper.KEY_COME_IN;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_END_DATETIME;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_GO_OUT;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_ID;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_LATITUDE;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_LONGITUDE;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_START_DATETIME;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_STATION_ID;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_STEP;
import static com.mobithink.carbon.database.DatabaseHelper.KEY_TRIP_ID;
import static com.mobithink.carbon.database.DatabaseHelper.TABLE_EVENT;
import static com.mobithink.carbon.database.DatabaseHelper.TABLE_STATION;
import static com.mobithink.carbon.database.DatabaseHelper.TABLE_STATION_TRIP_DATA;

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
        values.put(KEY_START_DATETIME, getDateTime());

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
        values.put(KEY_END_DATETIME, getDateTime());

        getOpenedDatabase().update(
                DatabaseHelper.TABLE_TRIP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(CarbonApplicationManager.getInstance().getCurrentTripId())});

    }

    /*************************** Utils *****************************/
    private Long getDateTime() {
        return System.currentTimeMillis();
    }



    /*************************** EVENT **************************************/

   public long createNewEvent (long tripId, EventDTO eventDTO){

       ContentValues values = new ContentValues();
       values.put(DatabaseHelper.KEY_TRIP_ID, tripId);
       values.put(DatabaseHelper.KEY_EVENT_NAME, eventDTO.getEventName());
       values.put(DatabaseHelper.KEY_START_DATETIME, eventDTO.getStartTime());
       values.put(DatabaseHelper.KEY_LATITUDE,eventDTO.getGpsLat());
       values.put(DatabaseHelper.KEY_LONGITUDE, eventDTO.getGpsLong());

       long eventId = getOpenedDatabase().insert(TABLE_EVENT, null, values);

       return eventId;
   }

    public void updateEvent (long eventId, long tripId, EventDTO eventDTO){
        ContentValues values = new ContentValues();
        values.put(KEY_END_DATETIME, eventDTO.getEndTime());
    }

    public void deleteEvent (long eventId){
        SQLiteDatabase db = mDataBase.getWritableDatabase();

        db.delete(TABLE_EVENT, KEY_ID + " = ?",
                new String[] { String.valueOf(eventId) });
    }


    /*************************** STATION **************************************/

    public long createNewStation (StationDTO stationDTO){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_STATION_NAME, stationDTO.getStationName());

        long stationId = getOpenedDatabase().insert(TABLE_STATION, null, values);
        return stationId;
    }

    public void updateStation (long stationId, long tripId, StationDataDTO stationDataDTO){
        SQLiteDatabase db = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATION_ID, stationId);
        values.put(KEY_TRIP_ID, tripId);
        values.put(KEY_COME_IN, stationDataDTO.getNumberOfComeIn());
        values.put(KEY_GO_OUT, stationDataDTO.getNumberOfGoOut());
        values.put(KEY_STEP, stationDataDTO.getStationStep());
        values.put(KEY_START_DATETIME, stationDataDTO.getStartTime());
        values.put(KEY_END_DATETIME, stationDataDTO.getEndTime());
        values.put(KEY_LATITUDE, stationDataDTO.getGpsLat());
        values.put(KEY_LONGITUDE, stationDataDTO.getGpsLong());

        db.update(TABLE_STATION_TRIP_DATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(stationDataDTO.getId())});
    }

}

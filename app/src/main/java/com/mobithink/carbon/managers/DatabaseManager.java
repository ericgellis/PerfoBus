package com.mobithink.carbon.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobithink.carbon.CarbonApplication;
import com.mobithink.carbon.database.DatabaseOpenHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.RollingPointDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpaput on 06/02/2017.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private static DatabaseManager mInstance;

    private static DatabaseOpenHelper mDataBase;
    private SQLiteDatabase openedDatabase;

    public static DatabaseManager getInstance() {
        if (mInstance == null)
        {
            mInstance = new DatabaseManager();
            mDataBase = new DatabaseOpenHelper(CarbonApplication.getInstance());
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
        values.put(DatabaseOpenHelper.KEY_LINE_ID, lineID);
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, getDateTime());

        // insert row
        long tripId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_TRIP, null, values);

        Log.i(TAG, "A new Trip have been created : id = " + tripId + ", for LineId " + lineID);

        CarbonApplicationManager.getInstance().setCurrentTripId(tripId);
    }


    public long finishCurrentTrip() {

        long tripId = CarbonApplicationManager.getInstance().getCurrentTripId();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, getDateTime());

        getOpenedDatabase().update(
                DatabaseOpenHelper.TABLE_TRIP, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(tripId)});


        return tripId;
    }


    TripDTO getTrip(long tripId) {

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_TRIP + " WHERE "
                + DatabaseOpenHelper.KEY_ID + " = " + tripId;

        Cursor c = getOpenedDatabase().rawQuery(selectQuery, null);

        if (c == null) {
            return null;
        }

        c.moveToFirst();

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_ID)));
        tripDTO.setTripName((c.getString(c.getColumnIndex(DatabaseOpenHelper.KEY_TRIP_NAME))));
        tripDTO.setStartTime(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_START_DATETIME)));
        tripDTO.setEndTime(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_END_DATETIME)));
        tripDTO.setAtmo(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_ATMO)));
        tripDTO.setTemperature(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_TEMPERATURE)));
        tripDTO.setWeather(c.getString(c.getColumnIndex(DatabaseOpenHelper.KEY_WEATHER)));
        tripDTO.setVehiculeCapacity(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_CAPACITY)));
        tripDTO.setBusLineId(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_LINE_ID)));

        c.close();

        return tripDTO;
    }


    public void updateTrip(TripDTO tripDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_ATMO, tripDTO.getAtmo());
        values.put(DatabaseOpenHelper.KEY_CAPACITY, tripDTO.getVehiculeCapacity());
        values.put(DatabaseOpenHelper.KEY_TEMPERATURE, tripDTO.getTemperature());
        values.put(DatabaseOpenHelper.KEY_WEATHER, tripDTO.getWeather());

        getOpenedDatabase().update(DatabaseOpenHelper.TABLE_TRIP, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(tripDTO.getId())});
    }

    public void deleteTrip(long tripId) {

        getOpenedDatabase().delete(DatabaseOpenHelper.TABLE_TRIP, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(tripId)});
    }

    public TripDTO getFullTripDTODataToSend(long tripId) {

        TripDTO tripDto = getTrip(tripId);

        tripDto.rollingPointDTOList = getRollingPointsForTripId(tripId);
        tripDto.eventDTOList = getEventsForTripId(tripId);
        tripDto.stationDataDTOList = getStationDatasForTripId();

        return tripDto;

    }


    /***************
     * Rolling Point
     *****************/

    public void registerRollingPoint(RollingPointDTO rollingPointDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, rollingPointDTO.getTripId());
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, rollingPointDTO.getGpsLong());
        values.put(DatabaseOpenHelper.KEY_LATITUDE, rollingPointDTO.getGpsLat());
        values.put(DatabaseOpenHelper.KEY_CREATION_DATE, getDateTime());

        // insert row
        long rollingPointId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_ROLLING_POINT, null, values);

        Log.i(TAG, "A new rollingPoint have been created : id = " + rollingPointId + " ,with longitude [" + rollingPointDTO.getGpsLong() + "] and lattitude [" + rollingPointDTO.getGpsLat() + "]");
    }

    private List<RollingPointDTO> getRollingPointsForTripId(long tripId) {

        List<RollingPointDTO> rollingPoints = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_ROLLING_POINT + " WHERE "
                + DatabaseOpenHelper.KEY_TRIP_ID + " = " + tripId;

        Cursor cursor = getOpenedDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                RollingPointDTO rp = new RollingPointDTO();

                rp.setTripId(tripId);
                rp.setGpsLong(cursor.getDouble(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LONGITUDE)));
                rp.setGpsLat(cursor.getDouble(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LATITUDE)));
                rp.setPointTime(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_CREATION_DATE)));

                rollingPoints.add(rp);

                cursor.moveToNext();
            }
        }

        cursor.close();

        return rollingPoints;
    }


    /***************************
     * Events
     ****************************/

    private List<EventDTO> getEventsForTripId(long tripId) {
        //TODO

        List<EventDTO> eventDTO = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_EVENT + " WHERE "
                + DatabaseOpenHelper.KEY_TRIP_ID + " = " + tripId;

        Cursor cursor = getOpenedDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                EventDTO ev = new EventDTO();

                //ev.setTripId(tripId);

                eventDTO.add(ev);

                cursor.moveToNext();
            }
        }

        cursor.close();

        return eventDTO;

    }


    /***************************
     * StationData
     ***********************/


    private List<StationDataDTO> getStationDatasForTripId() {
        //TODO
        /*List<StationDataDTO> stationDataDTOList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_STATION_TRIP_DATA + " WHERE "
                + DatabaseOpenHelper.KEY_TRIP_ID + " = " + tripId;

        Cursor cursor = getOpenedDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                StationDataDTO sdDTO = new StationDataDTO();

                //sdDTO.setStation(stationId);

                stationDataDTOList.add(sdDTO);

                cursor.moveToNext();
            }
        }

        cursor.close();

        return stationDataDTOList;*/

        return null;
    }

    /*************************** Utils *****************************/
    private Long getDateTime() {
        return System.currentTimeMillis();
    }


    /***************************
     * EVENT
     **************************************/

    public long createNewEvent(long tripId, EventDTO eventDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, tripId);
        values.put(DatabaseOpenHelper.KEY_EVENT_NAME, eventDTO.getEventName());
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, eventDTO.getStartTime());
        values.put(DatabaseOpenHelper.KEY_LATITUDE, eventDTO.getGpsLat());
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, eventDTO.getGpsLong());

        long eventId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_EVENT, null, values);

        return eventId;
    }

    public void updateEvent(EventDTO eventDTO) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, eventDTO.getEndTime());

        getOpenedDatabase().update(DatabaseOpenHelper.TABLE_EVENT, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(eventDTO.getId())});

    }

    public void deleteEvent(long eventId) {
        SQLiteDatabase db = mDataBase.getWritableDatabase();

        db.delete(DatabaseOpenHelper.TABLE_EVENT, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(eventId)});
    }


    /***************************
     * STATION
     **************************************/

    public long createNewStation(StationDTO stationDTO) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_STATION_NAME, stationDTO.getStationName());

        long stationId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_STATION, null, values);
        return stationId;
    }

    public void updateStationData(long stationId, long tripId, StationDataDTO stationDataDTO) {
        SQLiteDatabase db = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_STATION_ID, stationId);
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, tripId);
        values.put(DatabaseOpenHelper.KEY_STATION_NAME, stationDataDTO.getStationName());
        values.put(DatabaseOpenHelper.KEY_COME_IN, stationDataDTO.getNumberOfComeIn());
        values.put(DatabaseOpenHelper.KEY_GO_OUT, stationDataDTO.getNumberOfGoOut());
        values.put(DatabaseOpenHelper.KEY_STEP, stationDataDTO.getStationStep());
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, stationDataDTO.getStartTime());
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, stationDataDTO.getEndTime());
        values.put(DatabaseOpenHelper.KEY_LATITUDE, stationDataDTO.getGpsLat());
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, stationDataDTO.getGpsLong());

        db.update(DatabaseOpenHelper.TABLE_STATION_TRIP_DATA, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(stationDataDTO.getId())});
    }

    public void deleteStationData(long stationId, StationDataDTO stationDataDTO){
        SQLiteDatabase db = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_COME_IN, "null");
        values.put(DatabaseOpenHelper.KEY_GO_OUT, "null");
        values.put(DatabaseOpenHelper.KEY_STEP, "null");
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, "null");
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, "null");

        db.update(DatabaseOpenHelper.TABLE_STATION_TRIP_DATA, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(stationDataDTO.getId())});
    }

}

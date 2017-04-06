package com.mobithink.carbon.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobithink.carbon.CarbonApplication;
import com.mobithink.carbon.database.DatabaseOpenHelper;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.RollingPointDTO;
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

    public void startNewTrip(long lineID, TripDTO tripDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_LINE_ID, lineID);
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, getDateTime());
        values.put(DatabaseOpenHelper.KEY_CAPACITY, tripDTO.getVehicleCapacity());

        // insert row
        long tripId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_TRIP, null, values);

        Log.i(TAG, "A new Trip have been created : id = " + tripId + ", for LineId " + lineID);

        CarbonApplicationManager.getInstance().setCurrentTripId(tripId);
    }


    public long updateTrip(long tripId, TripDTO tripDTO) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, getDateTime());
        values.put(DatabaseOpenHelper.KEY_ATMO, tripDTO.getAtmo());
        values.put(DatabaseOpenHelper.KEY_TEMPERATURE, tripDTO.getTemperature());
        values.put(DatabaseOpenHelper.KEY_WEATHER, tripDTO.getWeather());

        getOpenedDatabase().update(DatabaseOpenHelper.TABLE_TRIP, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(tripId)});

        Log.i(TAG, "A new Trip have been updated : id = " + tripId + ", with temperature " + tripDTO.getTemperature());
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
        tripDTO.setTemperature(c.getString(c.getColumnIndex(DatabaseOpenHelper.KEY_TEMPERATURE)));
        tripDTO.setWeather(c.getString(c.getColumnIndex(DatabaseOpenHelper.KEY_WEATHER)));
        tripDTO.setVehicleCapacity(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_CAPACITY)));
        tripDTO.setBusLineId(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_LINE_ID)));

        c.close();

        return tripDTO;
    }


    public void deleteTrip(long tripId) {

        getOpenedDatabase().delete(DatabaseOpenHelper.TABLE_TRIP, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(tripId)});
    }

    public TripDTO getFullTripDTODataToSend(long tripId) {

        TripDTO tripDto = getTrip(tripId);

        tripDto.rollingPointDTOList = getRollingPointsForTripId(tripId);
        tripDto.eventDTOList = getEventsForTripId(tripId);
        tripDto.stationDataDTOList = getStationDatasForTripId(tripId);

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

        List<EventDTO> eventDTOList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_EVENT + " WHERE "
                + DatabaseOpenHelper.KEY_TRIP_ID + " = " + tripId;

        Cursor cursor = getOpenedDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                EventDTO ev = new EventDTO();

                ev.setEventName(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.KEY_EVENT_NAME)));
                ev.setStartTime(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_START_DATETIME)));
                ev.setEndTime(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_END_DATETIME)));
                ev.setGpsLong(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LONGITUDE)));
                ev.setGpsLat(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LATITUDE)));
                ev.setStationName(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.KEY_STATION_DATA_NAME)));

                eventDTOList.add(ev);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return eventDTOList;

    }


    /***************************
     * StationData
     ***********************/

    private List<StationDataDTO> getStationDatasForTripId(long tripId) {
        //TODO
        List<StationDataDTO> stationDataDTOList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_STATION_TRIP_DATA + " WHERE "
                + DatabaseOpenHelper.KEY_TRIP_ID + " = " + tripId;

        Cursor cursor = getOpenedDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                StationDataDTO sdDTO = new StationDataDTO();

                sdDTO.setNumberOfComeIn(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.KEY_COME_IN)));
                sdDTO.setNumberOfGoOut(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.KEY_GO_OUT)));
                sdDTO.setStartTime(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_START_DATETIME)));
                sdDTO.setEndTime(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_END_DATETIME)));
                sdDTO.setStationStep(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.KEY_STEP)));
                sdDTO.setGpsLong(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LONGITUDE)));
                sdDTO.setGpsLat(cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LATITUDE)));
                sdDTO.setStationName(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.KEY_STATION_NAME)));

                stationDataDTOList.add(sdDTO);

                cursor.moveToNext();
            }
        }

        cursor.close();

        return stationDataDTOList;

    }

    /*************************** Utils *****************************/
    private Long getDateTime() {
        return System.currentTimeMillis();
    }


    /***************************
     * EVENT
     **************************************/

    public long createNewEvent(long tripId, String StationDataDTOName, EventDTO eventDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, tripId);
        values.put(DatabaseOpenHelper.KEY_EVENT_NAME, eventDTO.getEventName());
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, eventDTO.getStartTime());
        values.put(DatabaseOpenHelper.KEY_STATION_DATA_NAME, StationDataDTOName);
        values.put(DatabaseOpenHelper.KEY_LATITUDE, eventDTO.getGpsLat());
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, eventDTO.getGpsLong());

        long eventId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_EVENT, null, values);
        CarbonApplicationManager.getInstance().setCurrentStationDataName(StationDataDTOName);
        Log.i(TAG, "A activity_station event has been created : id = " + eventDTO.getId() + ", for TripId " + tripId + ", for StationDataName " + StationDataDTOName + ", with endTime " + eventDTO.getEndTime());

        return eventId;
    }

    public void updateEvent(long tripId, String StationDataDTOName, EventDTO eventDTO) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, eventDTO.getEndTime());
        values.put(DatabaseOpenHelper.KEY_END_LATITUDE, eventDTO.getGpsEndLat());
        values.put(DatabaseOpenHelper.KEY_END_LONGITUDE, eventDTO.getGpsEndLong());

        try {
            getOpenedDatabase().update(DatabaseOpenHelper.TABLE_EVENT, values, DatabaseOpenHelper.KEY_ID + " = ?",
                    new String[]{String.valueOf(eventDTO.getId())});
            Log.i(TAG, "A event has been updated : id = " + eventDTO.getId() + ", for TripId " + tripId + ", for StationDataName " + StationDataDTOName + ", with endTime " + eventDTO.getEndTime());
        } catch (Exception e)  {
            throw e;
        }

    }

    public void deleteEvent(long tripId, String StationDataDTOName, EventDTO eventDTO) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, "null");
        values.put(DatabaseOpenHelper.KEY_LATITUDE, "null");
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, "null");

        getOpenedDatabase().delete(DatabaseOpenHelper.TABLE_EVENT, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(eventDTO.getId())});
    }


    /***************************
     * STATION
     **************************************/

    public long createNewStation(long tripId, StationDataDTO stationDataDTO) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, tripId);
        values.put(DatabaseOpenHelper.KEY_STATION_NAME, stationDataDTO.getStationName());
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, stationDataDTO.getStartTime());

        long stationDataId = getOpenedDatabase().insert(DatabaseOpenHelper.TABLE_STATION_TRIP_DATA, null, values);
        Log.i(TAG, "A new activity_station has been created : id = " + stationDataId + ", for TripId " + tripId);
        CarbonApplicationManager.getInstance().setCurrentStationDataName(stationDataDTO.getStationName());
        return stationDataId;
    }

    public void updateStationData(long tripId, StationDataDTO stationDataDTO) {
        openedDatabase = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_COME_IN, stationDataDTO.getNumberOfComeIn());
        values.put(DatabaseOpenHelper.KEY_GO_OUT, stationDataDTO.getNumberOfGoOut());
        values.put(DatabaseOpenHelper.KEY_STEP, stationDataDTO.getStationStep());
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, stationDataDTO.getEndTime());
        values.put(DatabaseOpenHelper.KEY_LATITUDE, stationDataDTO.getGpsLat());
        values.put(DatabaseOpenHelper.KEY_LONGITUDE, stationDataDTO.getGpsLong());
        values.put(DatabaseOpenHelper.KEY_STATION_NAME, stationDataDTO.getStationName());

        openedDatabase.update(DatabaseOpenHelper.TABLE_STATION_TRIP_DATA, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(stationDataDTO.getId())});

        Log.i(TAG, "A activity_station has been updated : id = " + stationDataDTO.getId() + ", for TripId " + tripId + ", with endTime " + stationDataDTO.getEndTime());
    }

    public void deleteStationData(long tripId, StationDataDTO stationDataDTO) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.KEY_TRIP_ID, tripId);
        values.put(DatabaseOpenHelper.KEY_COME_IN, "null");
        values.put(DatabaseOpenHelper.KEY_GO_OUT, "null");
        values.put(DatabaseOpenHelper.KEY_STEP, "null");
        values.put(DatabaseOpenHelper.KEY_START_DATETIME, "null");
        values.put(DatabaseOpenHelper.KEY_END_DATETIME, "null");

        getOpenedDatabase().update(DatabaseOpenHelper.TABLE_STATION_TRIP_DATA, values, DatabaseOpenHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(stationDataDTO.getId())});
    }

    StationDataDTO getStationData(String stationDataName) {

        String selectQuery = "SELECT  * FROM " + DatabaseOpenHelper.TABLE_STATION_TRIP_DATA + " WHERE "
                + DatabaseOpenHelper.KEY_STATION_DATA_NAME + " = " + stationDataName;

        Cursor c = getOpenedDatabase().rawQuery(selectQuery, null);

        if (c == null) {
            return null;
        }

        c.moveToFirst();

        StationDataDTO stationDataDTO = new StationDataDTO();

        stationDataDTO.setId(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_ID)));
        stationDataDTO.setStartTime(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_START_DATETIME)));
        stationDataDTO.setEndTime(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_END_DATETIME)));
        stationDataDTO.setNumberOfComeIn(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_COME_IN)));
        stationDataDTO.setNumberOfGoOut(c.getInt(c.getColumnIndex(DatabaseOpenHelper.KEY_GO_OUT)));
        stationDataDTO.setGpsLong(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_LONGITUDE)));
        stationDataDTO.setGpsLat(c.getLong(c.getColumnIndex(DatabaseOpenHelper.KEY_LATITUDE)));

        c.close();

        return stationDataDTO;
    }
}

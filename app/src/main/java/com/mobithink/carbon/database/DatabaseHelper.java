package com.mobithink.carbon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobithink.carbon.database.model.Trip;

import java.util.Date;

/**
 * Created by jpaput on 07/02/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mobithink";

    // Table Names
    private static final String TABLE_CITY = "cities";
    private static final String TABLE_LINE = "lines";
    private static final String TABLE_STATION = "stations";
    private static final String TABLE_LINE_STATION = "lines_stations";
    private static final String TABLE_TRIP = "trips";
    private static final String TABLE_STATION_TRIP_DATA = "stationTripDatas";
    private static final String TABLE_EVENT = "events";
    private static final String TABLE_ROLLING_POINT = "rollingPoints";



    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_CREATION_DATE = "creationDate";
    private static final String KEY_TRIP_ID = "trip_id";
    private static final String KEY_LINE_ID = "line_id";
    private static final String KEY_START_DATETIME = "startDatetime";
    private static final String KEY_END_DATETIME = "endDatetime";
    private static final String KEY_STEP = "step";

    // CITY Table - column names
    private static final String KEY_CITY_NAME = "cityName";

    // LINE Table - column names
    private static final String KEY_LINE_NAME = "lineName";
    private static final String KEY_CITY_ID = "city_id";

    // STATION Table - column names
    private static final String KEY_STATION_NAME = "stationName";

    // LINE_STATION Table - column names
    private static final String KEY_STATION_ID = "station_id";

    // TRIP Table - column names
    private static final String KEY_TRIP_NAME = "tripName";
    private static final String KEY_ATMO = "atmo";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_WEATHER = "weather";
    private static final String KEY_CAPACITY = "capacity";

    // ROLLING_POINT Table - column names
    private static final String KEY_TRAFFIC = "traffic";

    // STATION_TRIP_DATA Table - column names
    private static final String KEY_COME_IN = "comeIn";
    private static final String KEY_GO_OUT = "goOut";

    // EVENT Table - column names
    private static final String KEY_EVENT_NAME = "eventName";
    private static final String KEY_STATION_DATA_ID = "stationData_id";


    // Create table CITY
    private static final String CREATE_TABLE_CITY =
            "CREATE TABLE " + TABLE_CITY
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_CITY_NAME  + " TEXT" + ")";

    // Create table LINE
    private static final String CREATE_TABLE_LINE =
            "CREATE TABLE " + TABLE_LINE
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_LINE_NAME + " TEXT,"
                    + KEY_CREATION_DATE + " DATETIME"
                    + KEY_CITY_ID + " INTEGER" +")";

    // Create table STATION
    private static final String CREATE_TABLE_STATION =
            "CREATE TABLE " + TABLE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_STATION_NAME  + " TEXT" + ")";

    // Create table LINE_STATION
    private static final String CREATE_TABLE_LINE_STATION =
            "CREATE TABLE " + TABLE_LINE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_LINE_ID + " INTEGER,"
                    + KEY_STATION_ID + " INTEGER,"
                    + KEY_STEP + "INTEGER" + ")";

    // Create table TRIP
    private static final String CREATE_TABLE_TRIP =
            "CREATE TABLE " + TABLE_TRIP
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TRIP_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_ATMO + " INTEGER,"
                    + KEY_TEMPERATURE + " INTEGER,"
                    + KEY_WEATHER + " TEXT,"
                    + KEY_CAPACITY + " INTEGER,"
                    + KEY_LINE_ID + " INTEGER," +")";

    // Create table ROLLING_POINT
    private static final String CREATE_TABLE_ROLLING_POINT =
            "CREATE TABLE " + TABLE_ROLLING_POINT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TRAFFIC + " INTEGER,"
                    + KEY_CREATION_DATE + " DATETIME,"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER," +")";

    // Create table STATION_TRIP_DATA
    private static final String CREATE_TABLE_STATION_TRIP_DATA =
            "CREATE TABLE " + TABLE_STATION_TRIP_DATA
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_COME_IN + " INTEGER,"
                    + KEY_GO_OUT + " DATETIME,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_STEP + "INTEGER"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER," +")";

    // Create table EVENT
    private static final String CREATE_TABLE_EVENT =
            "CREATE TABLE " + TABLE_EVENT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_EVENT_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER,"
                    + KEY_STATION_DATA_ID + " INTEGER,"+")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_LINE);
        db.execSQL(CREATE_TABLE_STATION);
        db.execSQL(CREATE_TABLE_LINE_STATION);
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_STATION_TRIP_DATA);
        db.execSQL(CREATE_TABLE_ROLLING_POINT);
        db.execSQL(CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /*************************** TRIP **************************************/

    public long createTrip(int lineId ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LINE_ID, lineId);
        values.put(KEY_START_DATETIME, getDateTime());

        // insert row
        long tripId = db.insert(TABLE_TRIP, null, values);

        return tripId;
    }


    public Trip getTrip(long tripId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIP + " WHERE "
                + KEY_ID + " = " + tripId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null){
            return null;
        }

        c.moveToFirst();

        Trip trip = new Trip();
        trip.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        trip.setTripName((c.getString(c.getColumnIndex(KEY_TRIP_NAME))));
        trip.setTripStartTime(c.getLong(c.getColumnIndex(KEY_START_DATETIME)));
        trip.setTripEndTime(c.getLong(c.getColumnIndex(KEY_END_DATETIME)));
        trip.setAtmo(c.getInt(c.getColumnIndex(KEY_ATMO)));
        trip.setTemperature(c.getInt(c.getColumnIndex(KEY_TEMPERATURE)));
        trip.setWeather(c.getString(c.getColumnIndex(KEY_WEATHER)));
        trip.setCapacity(c.getInt(c.getColumnIndex(KEY_CAPACITY)));
        trip.setLineId(c.getInt(c.getColumnIndex(KEY_LINE_ID)));

        return trip;
    }

    public void finishCurrentTrip(long currentTripId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_END_DATETIME, getDateTime());

        db.update(TABLE_TRIP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(currentTripId)});
    }

    public void updateTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ATMO, trip.getAtmo());
        values.put(KEY_CAPACITY, trip.getCapacity());
        values.put(KEY_TEMPERATURE, trip.getTemperature());
        values.put(KEY_WEATHER, trip.getWeather());

        db.update(TABLE_TRIP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(trip.getId())});
    }

    public void deleteTrip(long tripId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRIP, KEY_ID + " = ?",
                new String[] { String.valueOf(tripId) });
    }

    /*************************** Utils *****************************/
    private Long getDateTime() {
        return System.currentTimeMillis();
    }


}

package com.mobithink.carbon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobithink.carbon.database.model.TripDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jpaput on 07/02/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Names
    public static final String TABLE_CITY = "cities";
    public static final String TABLE_LINE = "lines";
    public static final String TABLE_STATION = "stations";
    public static final String TABLE_LINE_STATION = "lines_stations";
    public static final String TABLE_TRIP = "trips";
    public static final String TABLE_STATION_TRIP_DATA = "stationTripDatas";
    public static final String TABLE_EVENT = "events";
    public static final String TABLE_ROLLING_POINT = "rollingPoints";
    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_CREATION_DATE = "creationDate";
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_LINE_ID = "line_id";
    public static final String KEY_START_DATETIME = "startDatetime";
    public static final String KEY_END_DATETIME = "endDatetime";
    public static final String KEY_STEP = "step";
    // CITY Table - column names
    public static final String KEY_CITY_NAME = "cityName";
    // LINE Table - column names
    public static final String KEY_LINE_NAME = "lineName";
    public static final String KEY_CITY_ID = "city_id";
    // STATION Table - column names
    public static final String KEY_STATION_NAME = "stationName";
    // LINE_STATION Table - column names
    public static final String KEY_STATION_ID = "station_id";
    // TRIP Table - column names
    public static final String KEY_TRIP_NAME = "tripName";
    public static final String KEY_ATMO = "atmo";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_WEATHER = "weather";
    public static final String KEY_CAPACITY = "vehiculeCapacity";
    // ROLLING_POINT Table - column names
    public static final String KEY_TRAFFIC = "traffic";
    // STATION_TRIP_DATA Table - column names
    public static final String KEY_COME_IN = "comeIn";
    public static final String KEY_GO_OUT = "goOut";
    // EVENT Table - column names
    public static final String KEY_EVENT_NAME = "eventName";
    public static final String KEY_STATION_DATA_ID = "stationData_id";
    // Create table CITY
    public static final String CREATE_TABLE_CITY =
            "CREATE TABLE " + TABLE_CITY
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_CITY_NAME  + " TEXT" + ")";
    // Create table LINE
    public static final String CREATE_TABLE_LINE =
            "CREATE TABLE " + TABLE_LINE
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_LINE_NAME + " TEXT,"
                    + KEY_CREATION_DATE + " DATETIME"
                    + KEY_CITY_ID + " INTEGER" +")";
    // Create table STATION
    public static final String CREATE_TABLE_STATION =
            "CREATE TABLE " + TABLE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_STATION_NAME  + " TEXT" + ")";
    // Create table LINE_STATION
    public static final String CREATE_TABLE_LINE_STATION =
            "CREATE TABLE " + TABLE_LINE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_LINE_ID + " INTEGER,"
                    + KEY_STATION_ID + " INTEGER,"
                    + KEY_STEP + "INTEGER" + ")";
    // Create table TRIP
    public static final String CREATE_TABLE_TRIP =
            "CREATE TABLE " + TABLE_TRIP
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_TRIP_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_ATMO + " INTEGER,"
                    + KEY_TEMPERATURE + " INTEGER,"
                    + KEY_WEATHER + " TEXT,"
                    + KEY_CAPACITY + " INTEGER,"
                    + KEY_LINE_ID + " INTEGER" +")";
    // Create table ROLLING_POINT
    public static final String CREATE_TABLE_ROLLING_POINT =
            "CREATE TABLE " + TABLE_ROLLING_POINT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_TRAFFIC + " INTEGER,"
                    + KEY_CREATION_DATE + " DATETIME,"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER" +")";
    // Create table STATION_TRIP_DATA
    public static final String CREATE_TABLE_STATION_TRIP_DATA =
            "CREATE TABLE " + TABLE_STATION_TRIP_DATA
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_COME_IN + " INTEGER,"
                    + KEY_GO_OUT + " DATETIME,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_STEP + "INTEGER"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER" +")";
    // Create table EVENT
    public static final String CREATE_TABLE_EVENT =
            "CREATE TABLE " + TABLE_EVENT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_EVENT_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_LATITUDE + " INTEGER,"
                    + KEY_LONGITUDE + " INTEGER,"
                    + KEY_TRIP_ID + " INTEGER,"
                    + KEY_STATION_DATA_ID + " INTEGER"+")";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mobithink.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)  {

        // creating required tables
        /*db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_LINE);
        db.execSQL(CREATE_TABLE_STATION);
        db.execSQL(CREATE_TABLE_LINE_STATION);
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_STATION_TRIP_DATA);
        db.execSQL(CREATE_TABLE_ROLLING_POINT);
        db.execSQL(CREATE_TABLE_EVENT);*/

            updateDatabase(db,0, DATABASE_VERSION);
    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion <= 1){
            db.execSQL(CREATE_TABLE_CITY);
            db.execSQL(CREATE_TABLE_LINE);
            db.execSQL(CREATE_TABLE_STATION);
            db.execSQL(CREATE_TABLE_LINE_STATION);
            db.execSQL(CREATE_TABLE_TRIP);
            db.execSQL(CREATE_TABLE_STATION_TRIP_DATA);
            db.execSQL(CREATE_TABLE_ROLLING_POINT);
            db.execSQL(CREATE_TABLE_EVENT);
        }
        if (oldVersion<2){
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_LINE);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STATION);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_LINE_STATION);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRIP);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STATION_TRIP_DATA);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ROLLING_POINT);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EVENT);

            db.execSQL(CREATE_TABLE_CITY);
            db.execSQL(CREATE_TABLE_LINE);
            db.execSQL(CREATE_TABLE_STATION);
            db.execSQL(CREATE_TABLE_LINE_STATION);
            db.execSQL(CREATE_TABLE_TRIP);
            db.execSQL(CREATE_TABLE_STATION_TRIP_DATA);
            db.execSQL(CREATE_TABLE_ROLLING_POINT);
            db.execSQL(CREATE_TABLE_EVENT);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }


    /*************************** TRIP **************************************/

   /* public long createTrip(long lineId ) {


        return tripId;
    }*/


    public TripDTO getTrip(long tripId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIP + " WHERE "
                + KEY_ID + " = " + tripId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null){
            return null;
        }

        c.moveToFirst();

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        tripDTO.setTripName((c.getString(c.getColumnIndex(KEY_TRIP_NAME))));
        tripDTO.setStartTime(c.getLong(c.getColumnIndex(KEY_START_DATETIME)));
        tripDTO.setEndTime(c.getLong(c.getColumnIndex(KEY_END_DATETIME)));
        tripDTO.setAtmo(c.getInt(c.getColumnIndex(KEY_ATMO)));
        tripDTO.setTemperature(c.getInt(c.getColumnIndex(KEY_TEMPERATURE)));
        tripDTO.setWeather(c.getString(c.getColumnIndex(KEY_WEATHER)));
        tripDTO.setVehiculeCapacity(c.getInt(c.getColumnIndex(KEY_CAPACITY)));
        tripDTO.setLineId(c.getInt(c.getColumnIndex(KEY_LINE_ID)));

        return tripDTO;
    }


    public void updateTrip(TripDTO tripDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ATMO, tripDTO.getAtmo());
        values.put(KEY_CAPACITY, tripDTO.getVehiculeCapacity());
        values.put(KEY_TEMPERATURE, tripDTO.getTemperature());
        values.put(KEY_WEATHER, tripDTO.getWeather());

        db.update(TABLE_TRIP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tripDTO.getId())});
    }

    public void deleteTrip(long tripId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRIP, KEY_ID + " = ?",
                new String[] { String.valueOf(tripId) });
    }



}

package com.mobithink.carbon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by jpaput on 15/02/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

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
    public static final String KEY_ID = "_id";
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
    public static final String KEY_STATION_DATA_NAME = "stationData_name";

    // Create table CITY
    private static final String CREATE_TABLE_CITY =
            "CREATE TABLE " + TABLE_CITY
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_CITY_NAME + " TEXT" + ")";
    // Create table LINE
    private static final String CREATE_TABLE_LINE =
            "CREATE TABLE " + TABLE_LINE
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_LINE_NAME + " TEXT,"
                    + KEY_CREATION_DATE + " DATETIME"
                    + KEY_CITY_ID + " INTEGER" + ")";
    // Create table STATION
    private static final String CREATE_TABLE_STATION =
            "CREATE TABLE " + TABLE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_STATION_NAME + " TEXT" + ")";
    // Create table LINE_STATION
    private static final String CREATE_TABLE_LINE_STATION =
            "CREATE TABLE " + TABLE_LINE_STATION
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_LINE_ID + " INTEGER,"
                    + KEY_STATION_ID + " INTEGER,"
                    + KEY_STEP + "INTEGER" + ")";
    // Create table TRIP
    private static final String CREATE_TABLE_TRIP =
            "CREATE TABLE " + TABLE_TRIP
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_TRIP_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_ATMO + " INTEGER,"
                    + KEY_TEMPERATURE + " TEXT,"
                    + KEY_WEATHER + " TEXT,"
                    + KEY_CAPACITY + " INTEGER,"
                    + KEY_LINE_ID + " INTEGER" + ")";
    // Create table ROLLING_POINT
    private static final String CREATE_TABLE_ROLLING_POINT =
            "CREATE TABLE " + TABLE_ROLLING_POINT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_TRAFFIC + " INTEGER,"
                    + KEY_CREATION_DATE + " DATETIME,"
                    + KEY_LATITUDE + " DOUBLE PRECISION,"
                    + KEY_LONGITUDE + " DOUBLE PRECISION,"
                    + KEY_TRIP_ID + " INTEGER" + ")";
    // Create table STATION_TRIP_DATA
    private static final String CREATE_TABLE_STATION_TRIP_DATA =
            "CREATE TABLE " + TABLE_STATION_TRIP_DATA
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_STATION_NAME + " TEXT,"
                    + KEY_COME_IN + " INTEGER,"
                    + KEY_GO_OUT + " INTEGER,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_STEP + " INTEGER,"
                    + KEY_LATITUDE + " DOUBLE PRECISION,"
                    + KEY_LONGITUDE + " DOUBLE PRECISION,"
                    + KEY_TRIP_ID + " INTEGER" + ")";
    // Create table EVENT
    private static final String CREATE_TABLE_EVENT =
            "CREATE TABLE " + TABLE_EVENT
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + KEY_EVENT_NAME + " TEXT,"
                    + KEY_START_DATETIME + " DATETIME,"
                    + KEY_END_DATETIME + " DATETIME,"
                    + KEY_LATITUDE + " DOUBLE PRECISION,"
                    + KEY_LONGITUDE + " DOUBLE PRECISION,"
                    + KEY_TRIP_ID + " INTEGER,"
                    + KEY_STATION_DATA_NAME + " TEXT" + ")";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mobithink.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateDatabase(db, 0, DATABASE_VERSION);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 1) {
            db.execSQL(CREATE_TABLE_CITY);
            db.execSQL(CREATE_TABLE_LINE);
            db.execSQL(CREATE_TABLE_STATION);
            db.execSQL(CREATE_TABLE_LINE_STATION);
            db.execSQL(CREATE_TABLE_TRIP);
            db.execSQL(CREATE_TABLE_STATION_TRIP_DATA);
            db.execSQL(CREATE_TABLE_ROLLING_POINT);
            db.execSQL(CREATE_TABLE_EVENT);
        }
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINE_STATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION_TRIP_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLLING_POINT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

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


}

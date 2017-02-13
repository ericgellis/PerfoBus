package com.mobithink.carbon.managers;

import android.content.SharedPreferences;

import com.mobithink.carbon.CarbonApplication;

/**
 * Created by jpaput on 06/02/2017.
 */

public class PreferenceManager {

    public static final String PREFERENCE_GENERAL = "PREFERENCE_GENERAL";

    public static final String STATION_RADIUS = "STATION_RADIUS";
    public static final String TIME_INTERVAL = "TIME_INTERVAL";

    private static PreferenceManager mInstance;

    private SharedPreferences mGeneralPreference;

    private PreferenceManager() {
        mGeneralPreference = CarbonApplication.getInstance().getSharedPreferences(PREFERENCE_GENERAL, 0);
    }

    public static PreferenceManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new PreferenceManager();
        }
        return mInstance;
    }

    public void setDataString(String key, String value){
        SharedPreferences.Editor editor = mGeneralPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getDataString(String key){
        return mGeneralPreference.getString(key, null);
    }

    public void setStationRadius(int value){
        SharedPreferences.Editor editor = mGeneralPreference.edit();
        editor.putInt(STATION_RADIUS, value);
        editor.apply();
    }

    public int getStationRadius(){
        return mGeneralPreference.getInt(STATION_RADIUS, 50);
    }


    public void setTimeFrequency(int value){
        SharedPreferences.Editor editor = mGeneralPreference.edit();
        editor.putInt(TIME_INTERVAL, value);
        editor.apply();
    }

    public int getTimeFrequency(){
        return mGeneralPreference.getInt(TIME_INTERVAL, 20);
    }

    public void clear(){
        mGeneralPreference.edit().clear().commit();
    }
}

package com.mobithink.carbon.services.weatherdata;

import org.json.JSONObject;

/**
 * Created by mplaton on 20/02/2017.
 */

public class Units implements JSONPopulator {

    private String temperature;

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }

    public String getTemperature() {
        return temperature;
    }
}

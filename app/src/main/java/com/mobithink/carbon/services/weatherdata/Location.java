package com.mobithink.carbon.services.weatherdata;

import org.json.JSONObject;

/**
 * Created by mplaton on 20/02/2017.
 */

public class Location implements JSONPopulator {
    private String city, country, region;

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
        region = data.optString("region");
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }
}

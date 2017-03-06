package com.mobithink.carbon.services.weatherdata;

import org.json.JSONObject;

/**
 * Created by mplaton on 20/02/2017.
 */

public class Item implements JSONPopulator {
    private Condition condition;

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }

    public Condition getCondition() {
        return condition;
    }
}

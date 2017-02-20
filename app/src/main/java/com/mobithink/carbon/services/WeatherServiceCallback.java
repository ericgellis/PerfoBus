package com.mobithink.carbon.services;

import com.mobithink.carbon.services.weatherdata.Channel;

/**
 * Created by mplaton on 20/02/2017.
 */

public interface WeatherServiceCallback {
    void ServiceSuccess(Channel channel);
    void ServiceFailure(Exception exception);
}

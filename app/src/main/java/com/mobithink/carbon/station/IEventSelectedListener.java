package com.mobithink.carbon.station;


import com.mobithink.carbon.database.model.EventDTO;

/**
 * Created by mplaton on 15/02/2017.
 */

public interface IEventSelectedListener {

    void onEventSelected(EventDTO event);
}

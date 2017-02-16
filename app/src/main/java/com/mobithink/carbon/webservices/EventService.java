package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.EventDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mplaton on 10/02/2017.
 */

public interface EventService {

    @POST("/mobithink/event/create")
    Call<EventDTO> register(@Body EventDTO eventDTO);
}

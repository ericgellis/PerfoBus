package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.StationDTO;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mplaton on 16/02/2017.
 */

public interface StationService {

    @POST("/mobithink/trip/create")
    Call<Void> register(@Body StationDTO stationDTO);
}

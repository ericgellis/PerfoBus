package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.TripDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jpaput on 14/02/2017.
 */

public interface TripService {

    @POST("/mobithink/trip/create")
    Call<Void> register(@Body TripDTO tripDTO);
}

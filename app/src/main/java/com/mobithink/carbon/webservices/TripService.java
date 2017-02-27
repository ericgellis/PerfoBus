package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.TripDTO;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jpaput on 14/02/2017.
 */

public interface TripService {

    @POST("/mobithink/trip/create")
    Call<Void> register(@Body TripDTO tripDTO);

    @GET("/mobithink/trip/findTripListByBusLineId/{busLineId}")
    Call<Collection<TripDTO>> showTripList(@Path("busLineId") Long busLineId);
}

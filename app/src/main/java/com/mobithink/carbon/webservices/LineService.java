package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jpaput on 07/02/2017.
 */

public interface LineService {

    @GET("/mobithink/city/findAll")
    Call<List<CityDTO>> getCities();

    @GET("/mobithink/busline/find/{cityName}")
    Call<List<BusLineDTO>> getCityLines(@Path("cityName") String cityName);

    @GET("/mobithink/station/find/{busLineId}")
    Call<List<StationDTO>> getLineStations(@Path("busLineId") Long lineID);

    @POST("/mobithink/busline/create")
    Call<Void> register(@Body BusLineDTO busLineDTO);
}

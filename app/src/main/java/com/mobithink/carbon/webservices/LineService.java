package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by jpaput on 07/02/2017.
 */

public interface LineService {

    @GET("/mobithink/city/findAll")
    Call<List<City>> getCities();
}

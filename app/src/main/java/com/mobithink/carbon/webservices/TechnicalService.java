package com.mobithink.carbon.webservices;

import com.mobithink.carbon.database.model.CityDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jpaput on 10/02/2017.
 */

public interface TechnicalService {

    @GET("/mobithink/server/wakeup")
    Call<Void> checkStatus();
}

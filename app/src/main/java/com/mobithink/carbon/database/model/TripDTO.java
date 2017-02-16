package com.mobithink.carbon.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jpaput on 07/02/2017.
 */

public class TripDTO implements Serializable {

    public Long id;

    public String tripName;

    public Long startTime;

    public Long endTime;

    public int atmo;

    public int temperature;

    public String weather;

    public int vehiculeCapacity;

    public Long busLineid;

    public List<StationDataDTO> stationDataDTOList;

    public List<RollingPointDTO> rollingPointDTOList;

    public List<EventDTO> eventDTOList;

    public TripDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getAtmo() {
        return atmo;
    }

    public void setAtmo(int atmo) {
        this.atmo = atmo;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getVehiculeCapacity() {
        return vehiculeCapacity;
    }

    public void setVehiculeCapacity(int vehiculeCapacity) {
        this.vehiculeCapacity = vehiculeCapacity;
    }

    public Long getBusLineId() {
        return busLineid;
    }

    public void setBusLineId(Long busLineDTO) {
        this.busLineid = busLineDTO;
    }


}

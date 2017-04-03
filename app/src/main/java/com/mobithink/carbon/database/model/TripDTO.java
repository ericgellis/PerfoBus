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

    public String temperature;

    public String weather;

    public int vehicleCapacity;

    public Long busLineid;

    public String cityName;

    public String lineName;

    public String direction;

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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Long getBusLineId() {
        return busLineid;
    }

    public void setBusLineId(Long busLineDTO) {
        this.busLineid = busLineDTO;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public List<StationDataDTO> getStationDataDTOList() {
        return stationDataDTOList;
    }

    public void setStationDataDTOList(List<StationDataDTO> stationDataDTOList) {
        this.stationDataDTOList = stationDataDTOList;
    }

    public List<RollingPointDTO> getRollingPointDTOList() {
        return rollingPointDTOList;
    }

    public void setRollingPointDTOList(List<RollingPointDTO> rollingPointDTOList) {
        this.rollingPointDTOList = rollingPointDTOList;
    }

    public List<EventDTO> getEventDTOList() {
        return eventDTOList;
    }

    public void setEventDTOList(List<EventDTO> eventDTOList) {
        this.eventDTOList = eventDTOList;
    }

    public Long getBusLineid() {
        return busLineid;
    }

    public void setBusLineid(Long busLineid) {
        this.busLineid = busLineid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

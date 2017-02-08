package com.mobithink.carbon.database.model;

import java.util.List;

/**
 * Created by jpaput on 07/02/2017.
 */

public class StationDataDTO {
    private Long id;
    private int numberOfComeIn;
    private int numberOfGoOut;
    private Long startTime;
    private Long endTime;
    private int stationStep;
    private String stationName;
    private Long gpsLat;
    private Long gpsLong;
    private List<Long> pictureIdList;

    public StationDataDTO() {
    }

    public StationDataDTO(Long id, int numberOfComeIn, int numberOfGoOut, Long startTime, Long endTime, int stationStep, String stationName, Long gpsLat, Long gpsLong) {
        this.id = id;
        this.numberOfComeIn = numberOfComeIn;
        this.numberOfGoOut = numberOfGoOut;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stationStep = stationStep;
        this.stationName = stationName;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
    }

    public List<Long> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(List<Long> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfComeIn() {
        return numberOfComeIn;
    }

    public void setNumberOfComeIn(int numberOfComeIn) {
        this.numberOfComeIn = numberOfComeIn;
    }

    public int getNumberOfGoOut() {
        return numberOfGoOut;
    }

    public void setNumberOfGoOut(int numberOfGoOut) {
        this.numberOfGoOut = numberOfGoOut;
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

    public int getStationStep() {
        return stationStep;
    }

    public void setStationStep(int stationStep) {
        this.stationStep = stationStep;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Long getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Long gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Long getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(Long gpsLong) {
        this.gpsLong = gpsLong;
    }
}

package com.mobithink.carbon.database.model;

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
    private Double gpsLat;
    private Double gpsLong;

    public StationDataDTO() {
    }

    public StationDataDTO(Long id, int numberOfComeIn, int numberOfGoOut, Long startTime, Long endTime, int stationStep, String stationName, Double gpsLat, Double gpsLong) {
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

    public Double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Double getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(Double gpsLong) {
        this.gpsLong = gpsLong;
    }

    @Override
    public String toString() {
        return "StationDataDTO{" +
                "id=" + id +
                ", numberOfComeIn=" + numberOfComeIn +
                ", numberOfGoOut=" + numberOfGoOut +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", stationStep=" + stationStep +
                ", stationName='" + stationName + '\'' +
                ", gpsLat=" + gpsLat +
                ", gpsLong=" + gpsLong +
                '}';
    }
}

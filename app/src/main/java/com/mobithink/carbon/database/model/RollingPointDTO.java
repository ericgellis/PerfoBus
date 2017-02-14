package com.mobithink.carbon.database.model;

import java.io.Serializable;

/**
 * Created by jpaput on 07/02/2017.
 */

public class RollingPointDTO implements Serializable {

    private Long id;
    private Long tripId;
    private Long pointTime;
    private Double gpsLat;
    private Double gpsLong;
    private int trafficIndex;


    public RollingPointDTO() {
    }

    public RollingPointDTO(Long id, Long tripId, Long pointTime, Double gpsLat, Double gpsLong, int trafficIndex) {
        this.id = id;
        this.tripId = tripId;
        this.pointTime = pointTime;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
        this.trafficIndex = trafficIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getPointTime() {
        return pointTime;
    }

    public void setPointTime(Long pointTime) {
        this.pointTime = pointTime;
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

    public int getTrafficIndex() {
        return trafficIndex;
    }

    public void setTrafficIndex(int trafficIndex) {
        this.trafficIndex = trafficIndex;
    }
}

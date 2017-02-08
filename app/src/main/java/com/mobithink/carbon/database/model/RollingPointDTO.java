package com.mobithink.carbon.database.model;

/**
 * Created by jpaput on 07/02/2017.
 */

public class RollingPointDTO {

    private Long id;
    private Long pointTime;
    private Long gpsLat;
    private Long gpsLong;
    private int trafficIndex;


    public RollingPointDTO() {
    }

    public RollingPointDTO(Long id, Long pointTime, Long gpsLat, Long gpsLong, int trafficIndex) {
        this.id = id;
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

    public Long getPointTime() {
        return pointTime;
    }

    public void setPointTime(Long pointTime) {
        this.pointTime = pointTime;
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

    public int getTrafficIndex() {
        return trafficIndex;
    }

    public void setTrafficIndex(int trafficIndex) {
        this.trafficIndex = trafficIndex;
    }
}

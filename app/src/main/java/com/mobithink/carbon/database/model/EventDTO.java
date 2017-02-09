package com.mobithink.carbon.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jpaput on 07/02/2017.
 */

public class EventDTO implements Serializable {

    private Long id;
    private String eventName;
    private Long startTime;
    private Long endTime;
    private Long gpsLat;
    private Long gpsLong;
    private String stationName;
    private List<Long> pictureIdList;

    public EventDTO(Long id, String eventName, Long startTime, Long endTime, Long gpsLat, Long gpsLong, String stationName) {
        this.id = id;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
        this.stationName = stationName;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}

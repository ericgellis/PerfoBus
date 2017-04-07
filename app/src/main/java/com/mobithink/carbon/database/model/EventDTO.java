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
    private Double gpsLat;
    private Double gpsLong;
    private Double gpsEndLat;
    private Double gpsEndLong;
    private String stationName;
    private Long timeSaving;
    private String picture;
    private String voiceMemo;

    public EventDTO() {

    }

    public EventDTO(Long id, String eventName, Long startTime, Long endTime, Double gpsLat, Double gpsLong, Double gpsEndLat, Double gpsEndLong, String stationName, Long timeSaving, String picture, String voiceMemo) {
        this.id = id;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
        this.gpsEndLat = gpsEndLat;
        this.gpsEndLong = gpsEndLong;
        this.stationName = stationName;
        this.timeSaving = timeSaving;
        this.voiceMemo = voiceMemo;
        this.picture = picture;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getGpsEndLat() {return gpsEndLat;}

    public void setGpsEndLat(Double gpsEndLat) {this.gpsEndLat = gpsEndLat;}

    public Double getGpsEndLong() {return gpsEndLong;}

    public void setGpsEndLong(Double gpsEndLong) {this.gpsEndLong = gpsEndLong;}

    public Long getTimeSaving() {
        return timeSaving;
    }

    public void setTimeSaving(Long timeSaving) {
        this.timeSaving = timeSaving;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVoiceMemo() {
        return voiceMemo;
    }

    public void setVoiceMemo(String voiceMemo) {
        this.voiceMemo = voiceMemo;
    }
}

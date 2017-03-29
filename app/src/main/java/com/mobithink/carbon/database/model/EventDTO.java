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
    private Long gpsEndLat;
    private Long gpsEndLong;
    private String stationName;
    private Long timeSaving;
    private List<String> pictureNameList;
    private List<String> voiceMemoNameList;

    public EventDTO() {

    }

    public EventDTO(Long id, String eventName, Long startTime, Long endTime, Long gpsLat, Long gpsLong, Long gpsEndLat, Long gpsEndLong, String stationName, Long timeSaving) {
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

    public Long getGpsEndLat() {return gpsEndLat;}

    public void setGpsEndLat(Long gpsEndLat) {this.gpsEndLat = gpsEndLat;}

    public Long getGpsEndLong() {return gpsEndLong;}

    public void setGpsEndLong(Long gpsEndLong) {this.gpsEndLong = gpsEndLong;}

    public Long getTimeSaving() {
        return timeSaving;
    }

    public void setTimeSaving(Long timeSaving) {
        this.timeSaving = timeSaving;
    }

    public List<String> getPictureNameList() {
        return pictureNameList;
    }

    public void setPictureNameList(List<String> pictureNameList) {
        this.pictureNameList = pictureNameList;
    }

    public List<String> getVoiceMemoNameList() {
        return voiceMemoNameList;
    }

    public void setVoiceMemoNameList(List<String> voiceNameList) {
        this.voiceMemoNameList = voiceNameList;
    }
}

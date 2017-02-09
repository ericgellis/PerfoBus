package com.mobithink.carbon.database.model;

/**
 * Created by jpaput on 07/02/2017.
 */

public class StationDTO {

    private Long id;
    private String stationName;
    private int step;

    public StationDTO(String stationName, int step){
      this.stationName = stationName;
        this.step = step;
    }


    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return stationName;
    }
}

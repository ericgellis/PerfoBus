package com.mobithink.carbon.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jpaput on 07/02/2017.
 */

public class BusLineDTO implements Serializable {

    private Long id;
    private String name;
    private Long dateOfCreation;
    private CityDTO cityDto;
    private List<StationDTO> stationDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public CityDTO getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDTO city) {

        cityDto = city;
    }

    public List<StationDTO> getStationDTOList() {
        return stationDTOList;
    }

    public void setStationDTOList(List<StationDTO> stationDTOList) {
        this.stationDTOList = stationDTOList;
    }

    @Override
    public String toString() {
        return name;
    }
}

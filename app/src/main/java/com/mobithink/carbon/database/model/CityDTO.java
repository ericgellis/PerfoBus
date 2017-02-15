package com.mobithink.carbon.database.model;

import java.io.Serializable;

/**
 * Created by jpaput on 07/02/2017.
 */

public class CityDTO implements Serializable {

    private Long id;
    private String name;

    public CityDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String city){
        name = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}

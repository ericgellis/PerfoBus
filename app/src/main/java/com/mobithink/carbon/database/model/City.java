package com.mobithink.carbon.database.model;

/**
 * Created by jpaput on 07/02/2017.
 */

public class City {

    public City() {
    }

    private long id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String city){
        name = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package com.mobithink.carbon.station.model;

import android.widget.ImageView;

/**
 * Created by mplaton on 01/02/2017.
 */

public class EventType {

    private String Name;
    private ImageView Icon;

    public EventType(String name, ImageView icon) {
        Name = name;
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ImageView getIcon() {
        return Icon;
    }

    public void setIcon(ImageView icon) {
        Icon = icon;
    }
}

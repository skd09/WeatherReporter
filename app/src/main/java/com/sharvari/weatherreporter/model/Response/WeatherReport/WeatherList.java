package com.sharvari.weatherreporter.model.Response.WeatherReport;

/**
 * Created by sharvari on 20-Jun-18.
 */

public class WeatherList {


    String id;
    String main;
    String description;
    String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

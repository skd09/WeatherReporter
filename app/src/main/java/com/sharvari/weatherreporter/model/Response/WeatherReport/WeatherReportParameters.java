package com.sharvari.weatherreporter.model.Response.WeatherReport;

import java.util.ArrayList;

/**
 * Created by sharvari on 20-Jun-18.
 */

public class WeatherReportParameters {

    String dt;
    String dt_txt;
    ArrayList<WeatherList> weather;
    Clouds clouds;
    Main main;
    Wind wind;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public ArrayList<WeatherList> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherList> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}

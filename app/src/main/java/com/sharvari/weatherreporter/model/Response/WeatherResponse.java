package com.sharvari.weatherreporter.model.Response;

import com.sharvari.weatherreporter.model.Response.CityReport.CityReportParameters;
import com.sharvari.weatherreporter.model.Response.WeatherReport.WeatherReportParameters;

/**
 * Created by sharvari on 20-Jun-18.
 */

public class WeatherResponse {
    String ReturnMsg;
    String ReturnCode;
    WeatherReportParameters WeatherReport;
    CityReportParameters CityReport;

    public String getReturnMsg() {
        return ReturnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        ReturnMsg = returnMsg;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        ReturnCode = returnCode;
    }

    public WeatherReportParameters getWeatherReport() {
        return WeatherReport;
    }

    public void setWeatherReport(WeatherReportParameters weatherReport) {
        WeatherReport = weatherReport;
    }

    public CityReportParameters getCityReport() {
        return CityReport;
    }

    public void setCityReport(CityReportParameters cityReport) {
        CityReport = cityReport;
    }
}

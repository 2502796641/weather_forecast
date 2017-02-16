package com.example.acer.weather_forecast.entity;

/**
 * Created by ACER on 2017/1/22.
 */

public class WeatherFromJuhe {
    private String resultcode;
    private String reason;
    private Results result;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Results getResult() {
        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WeatherFromJuhe{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

}

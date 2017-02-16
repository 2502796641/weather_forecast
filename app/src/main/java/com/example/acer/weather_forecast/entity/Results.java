package com.example.acer.weather_forecast.entity;

import java.util.List;

/**
 * Created by ACER on 2017/1/22.
 */

public class Results {
    private Sk sk;
    private Today today;
    private List<Futures> future;

    public Sk getSk() {
        return sk;
    }

    public void setSk(Sk sk) {
        this.sk = sk;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }

    public List<Futures> getFuture() {
        return future;
    }

    public void setFuture(List<Futures> future) {
        this.future = future;
    }
}

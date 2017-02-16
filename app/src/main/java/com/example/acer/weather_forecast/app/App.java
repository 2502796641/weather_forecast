package com.example.acer.weather_forecast.app;

import android.app.Application;

/**
 * Created by ACER on 2017/1/22.
 */

public class App extends Application{
    public static final String BASE_URL = "http://v.juhe.cn/weather/index?format=2&cityname=";
    public static final String APP_KEY = "&key=b9af4b4e7ffedeadb8df895a490e58fc";
    public static final String GUANGZHOU = "广州";


    public static final int LOAD_SUC = 0x16;
    public static final int LOAD_FAL = 0x18;
}

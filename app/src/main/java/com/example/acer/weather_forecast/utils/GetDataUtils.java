package com.example.acer.weather_forecast.utils;

import android.os.Handler;
import android.os.Message;

import com.example.acer.weather_forecast.app.App;
import com.example.acer.weather_forecast.entity.Sk;
import com.example.acer.weather_forecast.entity.WeatherFromJuhe;
import com.google.gson.Gson;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ACER on 2017/1/22.
 */

public class GetDataUtils {

    public static void getResult(String url, final Handler handler){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            //连接失败
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(App.LOAD_FAL);
            }
            //连接成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                WeatherFromJuhe wfj = gson.fromJson(result, WeatherFromJuhe.class);
                Message m = handler.obtainMessage();
                m.what = App.LOAD_SUC;
                m.obj = wfj;
                handler.sendMessage(m);
            }
        });
    }
}

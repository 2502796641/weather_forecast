package com.example.acer.weather_forecast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.acer.weather_forecast.R;
import com.example.acer.weather_forecast.adapter.AddcityAdapter;
import com.example.acer.weather_forecast.adapter.FirecastAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 2017/1/26.
 */

public class SelectCityActitvity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private ImageView iv;
    private List<String> data = new ArrayList<>();
    private AddcityAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initUI();
        initData();

    }

    private void initData() {
        data.add("广州");
        data.add("汕头");
        data.add("东莞");
        data.add("潮州");
        data.add("深圳");
        data.add("上海");
        data.add("北京");
        data.add("海口");
        data.add("杭州");
        data.add("苏州");
        data.add("重庆");
        adapter = new AddcityAdapter(data,this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) lv.getItemAtPosition(position);
                Intent i = new Intent();
                i.putExtra("address",text);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    private void initUI() {
        lv = (ListView) findViewById(R.id.select_addcity_lv);
        iv = (ImageView) findViewById(R.id.select_addcity_iv);
        iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_addcity_iv:
                finish();
                break;
        }
    }
}

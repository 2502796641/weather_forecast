package com.example.acer.weather_forecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.acer.weather_forecast.R;
import com.example.acer.weather_forecast.adapter.FirecastAdapter;

/**
 * Created by ACER on 2017/1/31.
 */

public class ListFragment extends Fragment{
    private ListView lv;
    private FirecastAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview,container,false);
        lv = (ListView) v.findViewById(R.id.forecast_lv);
        lv.setFocusable(false);
        return v;
    }
}

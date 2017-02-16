package com.example.acer.weather_forecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acer.weather_forecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 2017/1/29.
 */

public class AddcityAdapter extends BaseAdapter{
    private List<String> data = new ArrayList<>();
    private Context context;

    public AddcityAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.selecity_lv_item,null);
            holder.tv = (TextView) convertView.findViewById(R.id.selectcity_lv_item_tv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv.setText(data.get(position));
        return convertView;
    }

    class Holder{
        TextView tv;
    }
}

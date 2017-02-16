package com.example.acer.weather_forecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acer.weather_forecast.R;
import com.example.acer.weather_forecast.entity.Futures;

import java.util.ArrayList;
import java.util.List;

import static com.example.acer.weather_forecast.R.id.forecast_lv_item_weather_iv;

/**
 * Created by ACER on 2017/1/26.
 */
public class FirecastAdapter extends BaseAdapter{
    private List<Futures> data = new ArrayList<>();
    private Context context;

    public FirecastAdapter(List<Futures> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Futures getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.forecast_lv_item,null);
            holder.tv_monthdate = (TextView) convertView.findViewById(R.id.forecast_lv_item_monthdate_tv);
            holder.tv_weekdate = (TextView) convertView.findViewById(R.id.forecast_lv_item_weekdate_tv);
            holder.tv_temp = (TextView) convertView.findViewById(R.id.forecast_lv_item_temp_tv);
            holder.iv_weather = (ImageView) convertView.findViewById(forecast_lv_item_weather_iv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_monthdate.setText(getItem(position).getDate());
        holder.tv_weekdate.setText(getItem(position).getWeek());
        holder.tv_temp.setText(getItem(position).getTemperature());
        setSrc(getItem(position).getWeather(),holder.iv_weather);
        return convertView;
    }
    class Holder{
        TextView tv_monthdate;
        TextView tv_weekdate;
        ImageView iv_weather;
        TextView tv_temp;
    }

    private void setSrc(String weather,ImageView img){
        switch (weather){
            case "晴":
                img.setImageResource(R.drawable.icon_sunny);
                break;
            case "多云":
                img.setImageResource(R.drawable.icon_cloudy_day);
                break;
            case "小雨":
                img.setImageResource(R.drawable.icon_rainy_light);
                break;
            case "中雨":
                img.setImageResource(R.drawable.icon_rainy_middle);
                break;
            case "大雨":
                img.setImageResource(R.drawable.icon_rainy_heavy);
                break;
            case "雾":
                img.setImageResource(R.drawable.icon_fog);
                break;
            case "雨加雪":
                img.setImageResource(R.drawable.icon_rainy_snow);
                break;
            case "雷阵雨":
                img.setImageResource(R.drawable.icon_rainy_thunder);
                break;
            case "小雪":
                img.setImageResource(R.drawable.icon_snow_light);
                break;
            case "中雪":
                img.setImageResource(R.drawable.icon_snow_middle);
                break;
            case "大雪":
                img.setImageResource(R.drawable.icon_snow_heavy);
                break;
        }
    }
}

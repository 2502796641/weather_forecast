package com.example.acer.weather_forecast.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 2017/1/29.
 */

public class DBManager {

    private MyHelper helper;
    private SQLiteDatabase database;


    public DBManager(Context context) {
        helper = new MyHelper(context);
    }

    public void initDB(){
        database = helper.getReadableDatabase();
    }

    public void add(String str){
        initDB();
        boolean b = isRepeat(str);
        if(!b){
            ContentValues values = new ContentValues();
            values.put("name",str);
            database.insert("Citys",null,values);
        }
        database.close();

    }

    public List<String> select(){
        initDB();
        List<String> list = new ArrayList<>();
        Cursor c = database.query("Citys",null,null,null,null,null,null);
        while(c.moveToNext()){
            String s = c.getString(c.getColumnIndex("name"));
            list.add(s);
        }
        database.close();
        return list;
    }

    public void delete(String str){
        initDB();
        database.delete("Citys","name=?",new String[]{str});
        database.close();
    }
    public boolean isRepeat(String str){
        initDB();
        Cursor c = database.query("Citys",null,"name=?",new String[]{str},null,null,null);
        if(c.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }
}

package com.example.acer.weather_forecast.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ACER on 2017/1/29.
 */

public class MyHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "db.db";
    private static final String CREATE_TABLE = "CREATE TABLE Citys (name text)";
    public MyHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

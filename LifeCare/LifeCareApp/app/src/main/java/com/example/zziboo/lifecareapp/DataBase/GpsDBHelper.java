package com.example.zziboo.lifecareapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zziboo on 2017-06-07.
 */

public class GpsDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "GpsDB.db";

    public GpsDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table gpsTable" + "(title text primary key, location text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS gpsTable");
        onCreate(db);
    }

    public boolean insertGps(String title, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("location", location);
        contentValues.put("title", title);

        long affected=db.insert("gpsTable", null, contentValues);
        return affected>0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from gpsTable", null);
        return res;
    }

    public Integer deleteGps(String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("gpsTable", "title = ? ", new String[]{phoneNumber});
    }
}

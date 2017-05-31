package com.example.zziboo.lifecareapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by zziboo on 2017-05-24.
 */

public class MessageDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MsgDB.db";

    public MessageDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table messageTable" + "(phoneNumber text primary key, name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS messageTable");
        onCreate(db);
    }

    public boolean insertURL(String name, String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("phoneNumber", phoneNumber);

        long affected=db.insert("messageTable", null, contentValues);
        return affected>0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from urltable", null);
        return res;
    }

    public int numberofRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "messageTable");
        return numRows;
    }

    public Integer deleteUrl(String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("messageTable", "phoneNumber = ? ", new String[]{phoneNumber});
    }

    public ArrayList getAllUrl(){
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from messageTable", null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("phoneNumber")));
            res.moveToNext();
        }
        return array_list;
    }
}

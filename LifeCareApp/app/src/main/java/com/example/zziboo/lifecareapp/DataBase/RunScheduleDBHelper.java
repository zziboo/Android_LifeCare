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

public class RunScheduleDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "RunDB.db";

    public RunScheduleDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table runTable" + "(title text primary key, time text, day text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS runTable");
        onCreate(db);
    }

    public boolean insertURL(String title, String url, String day){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("time", url);
        contentValues.put("day", day);

        long affected=db.insert("runTable", null, contentValues);
        return affected>0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from runTable", null);
        return res;
    }

    public int numberofRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "runTable");
        return numRows;
    }

    public Integer deleteUrl(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("runTable", "title = ? ", new String[]{title});
    }

    public ArrayList getAllUrl(){
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from runTable", null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("title")));
            res.moveToNext();
        }
        return array_list;
    }
}

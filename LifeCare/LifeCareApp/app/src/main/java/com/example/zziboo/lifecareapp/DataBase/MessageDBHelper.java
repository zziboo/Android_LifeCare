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

    public boolean insertPhone(String name, String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("name", name);

        long affected=db.insert("messageTable", null, contentValues);
        return affected>0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from messageTable", null);
        return res;
    }

    public Integer deletePhone(String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("messageTable", "phoneNumber = ? ", new String[]{phoneNumber});
    }
}

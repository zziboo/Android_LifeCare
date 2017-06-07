package com.example.zziboo.lifecareapp.DataSet;

/**
 * Created by zziboo on 2017-06-07.
 */

public class GpsDataSet {
    String title;
    String location;

    public GpsDataSet(String title, String location){
        this.title = title;
        this.location = location;
    }

    public String getTitle(){
        return title;
    }

    public String getLocation(){
        return location;
    }
}

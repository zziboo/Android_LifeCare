package com.example.zziboo.lifecareapp.DataSet;

/**
 * Created by zziboo on 2017-05-29.
 */

public class MessageDataSet {
    String name;
    String phoneNumber;

    public MessageDataSet(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}

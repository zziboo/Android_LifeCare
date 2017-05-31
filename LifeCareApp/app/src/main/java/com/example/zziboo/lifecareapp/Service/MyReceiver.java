package com.example.zziboo.lifecareapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zziboo on 2017-05-31.
 */

public class MyReceiver extends BroadcastReceiver{
    static final String LOG_TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String measurement = intent.getStringExtra("measurement");
        Log.d(LOG_TAG, "onReceive" + measurement);
    }
}

package com.example.zziboo.lifecareapp.Service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zziboo on 2017-05-25.
 */
public class SensorService extends Service implements SensorEventListener {
    public static final String MY_ACTION = "com.example.zziboo.lifecareapp.Service.SensorService.MY_ACTION";
    private SensorManager mgr;
    private List<Sensor> sensorList;
    static final String LOG_TAG = "SensorService";
    Intent intent = new Intent("com.example.zziboo.lifecareapp.Service.SensorService.MY_ACTION");

    private float previousY, currentY;
    private int steps;
    private double calcul, distance;
    int threshold;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onStartCommand");

        currentY = previousY = steps = 0;
        threshold = 10;

        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = mgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for(Sensor sensor : sensorList){
            mgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        steps = 0;
        distance = 0;
        Log.d("STEP_RESET", "steps : " + steps);
        mgr.unregisterListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(LOG_TAG, "onSensorChanged");

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        currentY = y;

        if(Math.abs(currentY - previousY) > threshold){
            steps++;
            calcul = steps;
            distance = (calcul * 30)/100; // 30cm 보폭
            Log.d("STEP : ", steps+"");
            Log.d("STEP_DISTANCE : ", distance + "");
        }

        previousY = y;

        intent.putExtra("STEP", steps+"");
        intent.putExtra("DISTANCE", distance+"");
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

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
    final static String MY_ACTION = "com.example.zziboo.lifecareapp.Service.SensorService.MY_ACTION";
    private TextView output;
    private String reading;
    private SensorManager mgr;
    private List<Sensor> sensorList;
    static final String LOG_TAG = "SensorService";
    Intent intent = new Intent("com.example.zziboo.lifecareapp.Service.SensorService.MY_ACTION");

    private float acceleration;
    private float previousY, currentY;
    private int steps;

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
            Log.d("STEP : ", steps+"");
        }

        previousY = y;

        StringBuilder builder = new StringBuilder();

        for(int i=0; i<event.values.length; ++i){
            builder.append(" [");
            builder.append(i);
            builder.append("] = ");
            builder.append(event.values[i]);
            builder.append("\n");
        }

        reading = builder.toString();

        intent.putExtra("measurenment", steps);
        sendBroadcast(intent);
    }

    public void resetSteps(){
        steps = 0;
        Log.d("Reset Steps", "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

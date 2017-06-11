package com.example.zziboo.lifecareapp.Service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.DataBase.GpsDBHelper;
import com.example.zziboo.lifecareapp.DataBase.MessageDBHelper;
import com.example.zziboo.lifecareapp.SetActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zziboo on 2017-05-25.
 */

public class GpsService extends Service implements LocationListener{
    private final Context mContext;
    public static final String BROADCAST_ACTION = "com.example.zziboo.lifecareapp.Service.GpsService";

    public int timeset;
    public String alarmtxt = "";

    MessageDBHelper myMsgdb;
    GpsDBHelper myGpsdb;

    ArrayList messageNumber;

    private List<LatLng> latLngList;

    boolean checkGPS = false;
    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;

    Intent intent;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            sendSMS();
            timer();
        }
    };

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getSharedPreferences(SetActivity.MESSAGE_DATA, Activity.MODE_PRIVATE);
        editor = pref.edit();
        timeset = pref.getInt("TIMESET", 30);
        alarmtxt = pref.getString("MSG",  "다음과 같은 장소에서 현재 움직임이 없습니다.");

        timer();
        getLocation();
        Log.d("Start Loc : ", getLongitude() + " " + getLatitude());
        runnable.run();
    }

    private void timer(){
        handler.postDelayed(runnable, timeset * 60000); // 1초 = 1000
    }

    public GpsService(){
        mContext = this;
        Log.d("Start Loc : ", "GpsService() " + getLongitude() + " " + getLatitude());
    }

    public GpsService(Context mContext) {
        this.mContext = mContext;
        Log.d("Start Loc : ", "GpsService(context) " + getLongitude() + " " + getLatitude());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        getLocation();
    }

    @Override
    public boolean stopService(Intent name) {
        handler.removeCallbacks(runnable);
        return super.stopService(name);
    }

    private Location getLocation() {
        Log.d("GPS_SERVICE", "getLocation");
        //latLngList.add(new LatLng(getLatitude(), getLongitude()));
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider Available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (checkNetwork) {
                    Toast.makeText(this, "Network", Toast.LENGTH_SHORT).show();

                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }

                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                    catch(SecurityException e){
                        Log.e("error",e.getMessage()+"");
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                Toast.makeText(mContext,"GPS",Toast.LENGTH_SHORT).show();
                if (loc == null) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendLocationBroadcast(intent);
        return loc;
    }

    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS Not Enabled");
        alertDialog.setMessage("Do you wants to turn On GPS");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsService.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.loc = location;
        timer();
        getLatitude();
        getLongitude();
        Log.d("Loc Change : ", getLongitude() + " " + getLatitude());
        sendLocationBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void sendSMS(){
        myMsgdb = new MessageDBHelper(this);
        myGpsdb = new GpsDBHelper(this);

        messageNumber = new ArrayList();
        Cursor res = myMsgdb.getData();

        //Cursor 받아와 Message 보내는 함수
        while(res.moveToNext()){
            messageNumber.add(res.getString(0));
        };

        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        for(int i=0; i<messageNumber.size(); ++i) {
            Log.e("SEND MSG : ", messageNumber.get(i).toString());
            String locationTitle;
            if(myGpsdb.selectGps(loc.getLatitude() + "/" + loc.getLongitude()) != null){
                locationTitle = myGpsdb.selectGps(loc.getLatitude() + "/" + loc.getLongitude());
            }else{
                locationTitle = "Latitude : " + loc.getLatitude() + "/Longitude : " + loc.getLongitude();
            }
            Log.e("MSG : ", locationTitle + " " + alarmtxt);
            mSmsManager.sendTextMessage(messageNumber.get(i).toString(), null, locationTitle + alarmtxt, sentIntent, deliveredIntent);
        }
        if(messageNumber.size() <= 0){
            Toast.makeText(getApplicationContext(), "저장된 번호가 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLocationBroadcast(Intent intent){
        intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("latitude", latitude+"");
        intent.putExtra("longitude", longitude+"");
        //intent.putStringArrayListExtra("GoogleLine", latLngList);
        sendBroadcast(intent);
    }
}
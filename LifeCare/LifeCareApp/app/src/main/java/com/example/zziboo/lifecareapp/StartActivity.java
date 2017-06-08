package com.example.zziboo.lifecareapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.Service.GpsService;
import com.example.zziboo.lifecareapp.Service.SensorService;

import org.w3c.dom.Text;


public class StartActivity extends AppCompatActivity {
    TextView gpsText; // gps 출력 Text
    TextView senText; // 만보계 출력 Text
    TextView distanceText; // 이동거리 출력 Text

    Button gpsPositionButton; // Position 에 대한 Google Map을 확인하는 버튼이다.
    Button resetStepbtn; // 이동 걸음 수 와 이동 거리에 대해 reset 시켜준다.

    Intent gpsService; // gps 서비스에 대한 intent
    Intent sensorService; // 만보계 서비스에 대한 intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // receiver 실행
        registerReceiver(getGpsReceiver, new IntentFilter(GpsService.BROADCAST_ACTION));
        registerReceiver(getSensorReceiver, new IntentFilter(SensorService.MY_ACTION));

        // 필요 view 선언
        gpsText = (TextView) findViewById(R.id.gpstxt);
        senText = (TextView) findViewById(R.id.sentxt);
        distanceText = (TextView) findViewById(R.id.distancetxt);

        //service intent 선언
        gpsService = new Intent(this, GpsService.class);
        sensorService = new Intent(this, SensorService.class);

        //각 service 실행
        startService(gpsService);
        startService(sensorService);

        //google map 실행 버튼
        gpsPositionButton = (Button)findViewById(R.id.gpsmapbtn);
        gpsPositionButton.setOnClickListener(new View.OnClickListener() { // Gps Map Č­ļé ŋŽ°á ļŪ―šģĘ
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        //만보계 리셋버튼 실행
        resetStepbtn = (Button)findViewById(R.id.resetStepbtn);
        resetStepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(sensorService);
                startService(sensorService);
            }
        });

        // service가 실행중인지 확인하는 구문
        if(isMyServiceRunning(SensorService.class)){
            Toast.makeText(this, "Step Service is Running..", Toast.LENGTH_SHORT).show();

        }
        // service가 실행중인지 확인하는 구문
        if(isMyServiceRunning(GpsService.class)){
            Toast.makeText(this, "GPS Service is Running..", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //gps 서비스에대한 브로드캐스트 리시버
    private BroadcastReceiver getGpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String lat = intent.getStringExtra("latitude");
            String log = intent.getStringExtra("longitude");
            Log.d("Gps", "lon : " + log + ", lat : " + lat);

            gpsText.setText("Longitude : " + log + "\n Latitude : " + lat);
        }
    };

    //만보계 서비스에대한 브로드캐스트 리시버
    private BroadcastReceiver getSensorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String step = intent.getStringExtra("STEP");
            String distance = intent.getStringExtra("DISTANCE");
            senText.setText(step);
            distanceText.setText(distance + " (m)");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //********************************************//
    //*****************옵션 메뉴 *****************//
    //********************************************//
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ // 옵션메뉴 Create
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){ // 각 DB정보에 대해 추가하거나 제외하는 셋팅 메뉴
        Intent intent;
        switch(item.getItemId()){
            case R.id.gpsmenu:
                intent = new Intent(this, GpsLocationActivity.class);
                startActivity(intent);
                return true;
            case R.id.msgmenu:
                intent = new Intent(this, MessageNumberActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Service가 진행중인지 상태를 확인하는 method
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
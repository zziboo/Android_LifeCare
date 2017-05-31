package com.example.zziboo.lifecareapp;

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
import com.example.zziboo.lifecareapp.Service.MyReceiver;
import com.example.zziboo.lifecareapp.Service.SensorService;


public class StartActivity extends AppCompatActivity {
    static final String LOG_TAG = "ServiceActivity";
    MyReceiver myReceiver = null;

    TextView gpsText; // gps 값 출력 Text
    TextView senText; // 가속도 센서 출력 Text

    Button gpsPositionButton; // Position 출력하는 Google Map Activity 실행 Button

    Intent senserService; // 가속도 service 연결 변수
    BroadcastReceiver sensorReceiver; // broadcast로 가속도 값 받아옴
    Intent gpsService;

    BroadcastReceiver gpsReveiver; // gps broadcast
    double latitude, longitude;

    String serviceData; // service에 대한 데이터 값 받아오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        gpsText = (TextView) findViewById(R.id.gpstxt);
        senText = (TextView) findViewById(R.id.sentxt);

        senserService = new Intent(this, SensorService.class);
        startService(senserService);

        gpsService = new Intent(this, GpsService.class);
        startService(gpsService);

        sensorReceiver = new SensorReceiver();
        senserService = new Intent(this, SensorService.class);

        gpsReveiver = new GPSReceiver();


        gpsPositionButton = (Button)findViewById(R.id.gpsmapbtn);
        gpsPositionButton.setOnClickListener(new View.OnClickListener() { // Gps Map 화면 연결 리스너
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        //setGps(); // gps 값 확인
        //setSen(); // 가속도 값 확인
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume/registering receiver");
        myReceiver = new MyReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.zziboo.lifecareapp.Service.SensorService.MY_ACTION");
        registerReceiver(myReceiver, filter);
    }

    public void setSen(){
        try{
            IntentFilter mainFilter = new IntentFilter("com.androday.test.step");
            registerReceiver(sensorReceiver, mainFilter);
            startService(senserService);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setGps(){
        IntentFilter gpsFilter = new IntentFilter();
        registerReceiver(gpsReveiver, gpsFilter);
        startService(gpsService);
    }


    //********************************************//
    //*****************옵션 메뉴 *****************//
    //********************************************//
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ // 옵션메뉴사용
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){ // 옵션메뉴 버튼 클릭 사용
        Intent intent;
        switch(item.getItemId()){
            case R.id.runsetmenu:
                intent = new Intent(this, ScheduleActivity.class);
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



    //********************************************//
    //**************센서 브로드캐스트*************//
    //********************************************//
    class SensorReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceData = intent.getStringExtra("serviceData");
            senText.setText(serviceData);
        }
    }

    class GPSReceiver extends  BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = Double.valueOf(intent.getStringExtra("Latitude"));
            longitude = Double.valueOf(intent.getStringExtra("Longitude"));

            gpsText.setText("Latitude : " + latitude + ", Longitude : " + longitude);
        }
    }
}

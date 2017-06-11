package com.example.zziboo.lifecareapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.Service.GpsService;
import com.example.zziboo.lifecareapp.Service.SensorService;


public class SetActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String MESSAGE_DATA = "MESSAGE_DATA";
    Switch stepSwitch, msgSwitch;
    Intent sensorService;
    Intent gpsService;
    Button saveButton;
    EditText timetxt, alarmMsgtxt;
    public int timeset;
    public String message;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        // service의 on/off를 위한 Intent
        sensorService = new Intent(this, SensorService.class);
        gpsService = new Intent(this, GpsService.class);

        // 필요한 switch button 선언
        stepSwitch  = (Switch) findViewById(R.id.stepSwitch);
        msgSwitch  = (Switch) findViewById(R.id.msgSwitch);

        // switch에 대한 리스너 등록
        stepSwitch.setOnCheckedChangeListener(this);
        msgSwitch.setOnCheckedChangeListener(this);

        // 필요한 text와 button에 대한 선언
        timetxt = (EditText) findViewById(R.id.alarmtxt);
        alarmMsgtxt = (EditText) findViewById(R.id.alarmMsgtxt);
        saveButton = (Button) findViewById(R.id.savebtn);


        // SharedPreference를 사용하여 timeset 값과 Message String 값 저장
        pref = getSharedPreferences(MESSAGE_DATA, MODE_PRIVATE);
        editor = pref.edit();
        timetxt.setText(pref.getInt("TIMESET", 30)+"");
        alarmMsgtxt.setText(pref.getString("MSG", "다음과 같은 장소에서 현재 움직임이 없습니다."));

        // service가 실행중인지 확인하는 구문
        if(isMyServiceRunning(SensorService.class)){
            Toast.makeText(this, "Step Service is Running..", Toast.LENGTH_SHORT).show();
            stepSwitch.setChecked(true);
        }
        // service가 실행중인지 확인하는 구문
        if(isMyServiceRunning(GpsService.class)){
            Toast.makeText(this, "GPS Service is Running..", Toast.LENGTH_SHORT).show();
            msgSwitch.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked == false){
            //v의 service 종료
            switch (buttonView.getId()){
                //msg service start
                case R.id.msgSwitch:
                    stopService(gpsService);
                    Toast.makeText(this, buttonView.getText() + " off", Toast.LENGTH_SHORT).show();
                    break;
                //step service start
                case R.id.stepSwitch:
                    stopService(sensorService);
                    Toast.makeText(this, buttonView.getText() + " off", Toast.LENGTH_SHORT).show();
                    break;
            }
            return ;
        }

        switch (buttonView.getId()){
            //msg service start
            case R.id.msgSwitch:
                startService(gpsService);
                Toast.makeText(this, buttonView.getText() + " on", Toast.LENGTH_SHORT).show();
                break;
            //step service start
            case R.id.stepSwitch:
                startService(sensorService);
                Toast.makeText(this, buttonView.getText() + " on", Toast.LENGTH_SHORT).show();
                break;
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

    // Activity를 종료하기위한 리스너
    public void closeClick(View view){
        finish();
    }

    // Message 송신을 위한 시간과 메세지 내용을 저장하기 위한 버튼 리스너
    public void saveOnClick(View view){
        timeset = Integer.parseInt(timetxt.getText().toString());
        message = alarmMsgtxt.getText().toString();
        editor.putInt("TIMESET", timeset);
        editor.putString("MSG", message);
        editor.commit();
        Toast.makeText(getApplicationContext(), timeset + "분 이후 " + message  + "가 송신됩니다.", Toast.LENGTH_SHORT).show();
    }
}

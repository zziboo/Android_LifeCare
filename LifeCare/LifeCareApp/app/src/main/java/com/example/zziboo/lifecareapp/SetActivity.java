package com.example.zziboo.lifecareapp;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.Service.SensorService;


public class SetActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch stepSwitch, msgSwitch;
    Intent sensorService;
    Button saveButton;
    EditText timetxt, alarmMsgtxt;
    public int timeset;
    public String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        // service의 on/off를 위한 Intent
        sensorService = new Intent(this, SensorService.class);

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

        // service가 실행중인지 확인하는 구문
        if(isMyServiceRunning(SensorService.class)){
            Toast.makeText(this, "Step Service is Running..", Toast.LENGTH_SHORT).show();
            stepSwitch.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked == false){
            Toast.makeText(getApplicationContext(), buttonView.getText() + " off", Toast.LENGTH_SHORT).show();
            //v의 service 종료
            return ;
        }

        switch (buttonView.getId()){
            //msg service start
            case R.id.msgSwitch:
                Toast.makeText(this, buttonView.getText() + " on", Toast.LENGTH_SHORT).show();
                break;
            //step service start
            case R.id.stepSwitch:
                stopService(sensorService);
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
        Toast.makeText(getApplicationContext(), timeset + "시간 이후 " + message  + "가 송신됩니다.", Toast.LENGTH_SHORT).show();
    }
}

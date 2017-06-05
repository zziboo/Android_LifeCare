package com.example.zziboo.lifecareapp.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.R;
import com.example.zziboo.lifecareapp.Service.GpsService;
import com.example.zziboo.lifecareapp.SetActivity;

import org.w3c.dom.Text;

/**
 * Created by zziboo on 2017-05-24.
 */

public class OnOffFragment extends Fragment{
    Intent intent;
    TextView settext;
    SetActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.onoff_fragment, container, false);
    }

    public void onOffSetting(CompoundButton v, boolean isChecked){
        if(isChecked == false){
            Toast.makeText(activity, v.getText() + " off", Toast.LENGTH_SHORT).show();
            //v의 service 종료
            return ;
        }

        switch (v.getId()){
            case R.id.msgSwitch:
                Toast.makeText(activity, v.getText() + " on", Toast.LENGTH_SHORT).show();
                //msg service start
                break;
            case R.id.gpsSwitch:
                Toast.makeText(activity, v.getText() + " on", Toast.LENGTH_SHORT).show();
                //gps service start
                break;
            case R.id.alarmSwitch:
                Toast.makeText(activity, v.getText() + " on", Toast.LENGTH_SHORT).show();
                //alarm service start
                break;
        }
    }
}

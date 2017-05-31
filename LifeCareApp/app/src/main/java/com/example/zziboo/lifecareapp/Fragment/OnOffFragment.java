package com.example.zziboo.lifecareapp.Fragment;

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

import org.w3c.dom.Text;

/**
 * Created by zziboo on 2017-05-24.
 */

public class OnOffFragment extends Fragment{
    Intent intent;
    TextView settext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.onoff_fragment, container, false);
    }

    public void onOffSetting(CompoundButton v, boolean isChecked){
        if(isChecked == false){

            //v의 service 종료
            return ;
        }

        switch (v.getId()){
            case R.id.msgSwitch:
                //msg service start
                break;
            case R.id.gpsSwitch:
                //gps service start
                break;
            case R.id.alarmSwitch:
                //alarm service start
                break;
        }
    }
}

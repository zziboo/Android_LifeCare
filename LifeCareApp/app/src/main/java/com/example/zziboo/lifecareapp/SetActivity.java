package com.example.zziboo.lifecareapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zziboo.lifecareapp.Fragment.GpsFragment;
import com.example.zziboo.lifecareapp.Fragment.MessageFragment;
import com.example.zziboo.lifecareapp.Fragment.OnOffFragment;
import com.example.zziboo.lifecareapp.Fragment.RunFragment;

public class SetActivity extends AppCompatActivity {
    Button gpsSettingButton, runningSettingButton, messageSettingButton, onoffSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        gpsSettingButton = (Button) findViewById(R.id.gpssetbtn);
        runningSettingButton = (Button) findViewById(R.id.runsetbtn);
        messageSettingButton = (Button) findViewById(R.id.msgsetbtn);
        onoffSettingButton = (Button) findViewById(R.id.onsetbtn);

        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            OnOffFragment firstFragment = new OnOffFragment();
            firstFragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onSetButtonClick(View view){
        Fragment fr = null;

        switch (view.getId()){
            case R.id.onsetbtn:
                fr = new OnOffFragment();

                break;
            case R.id.runsetbtn:
                fr = new RunFragment();
                break;
            case R.id.msgsetbtn:
                fr = new MessageFragment();
                break;
            case R.id.gpssetbtn:
                fr = new GpsFragment();
                break;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

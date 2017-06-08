package com.example.zziboo.lifecareapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button startButton, setButton, closeButton;

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private String mProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startbtn);
        setButton = (Button) findViewById(R.id.setbtn);
        closeButton = (Button) findViewById(R.id.exitbtn);

        // Gps 권한에 대한 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
    }

    public void onClick(View view){ // 첫 화면에 대한 Button 처리 Event
        Intent intent;

        switch(view.getId()){
            case R.id.startbtn:
                intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                break;
            case R.id.setbtn:
                intent = new Intent(this, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.exitbtn:
                Toast.makeText(getApplicationContext(), "어플리케이션을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    // Gps 권한요청에 대한 reauest
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "권한이 없으면 앱을 사용하실수 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    Toast.makeText(getApplicationContext(), "권한이 없으면 앱을 사용하실수 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }

        }
    }
}
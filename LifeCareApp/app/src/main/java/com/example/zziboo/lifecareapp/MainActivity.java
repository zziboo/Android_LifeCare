package com.example.zziboo.lifecareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button startButton, setButton, closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startbtn);
        setButton = (Button) findViewById(R.id.setbtn);
        closeButton = (Button) findViewById(R.id.exitbtn);
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
}

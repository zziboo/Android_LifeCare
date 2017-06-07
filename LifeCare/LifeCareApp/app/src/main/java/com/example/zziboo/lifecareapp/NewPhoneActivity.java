package com.example.zziboo.lifecareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.DataBase.MessageDBHelper;
import com.example.zziboo.lifecareapp.Service.GpsService;

public class NewPhoneActivity extends AppCompatActivity {
    MessageDBHelper msgDB;
    Button addButton;
    EditText nametxt, phonetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phone);

        msgDB = new MessageDBHelper(this);
        addButton = (Button) findViewById(R.id.addPhone);

        nametxt = (EditText) findViewById(R.id.nametxt);
        phonetxt = (EditText) findViewById(R.id.phoneNumbertxt);

        //데이터 저장 클릭시 실행되는 리스너
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit Text가 채워져있는지 확인하는 구문
                if(nametxt.getText().toString().length() <= 0 || phonetxt.getText().toString().length() <= 0){
                    Toast.makeText(getApplicationContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DB에 추가하는 구문
                if(msgDB.insertPhone(nametxt.getText().toString(), phonetxt.getText().toString())){
                    Toast.makeText(getApplicationContext(), nametxt.getText() + "의 번호 " + phonetxt.getText() + "를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    nametxt.setText("");
                    phonetxt.setText("");
                    nametxt.requestFocus();

                    //추가 이후 Custom List를 초기화 시켜주기위해 결과 값 보내기
                    Intent intent = new Intent();
                    intent.putExtra("result", phonetxt.getText());
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    //같은 번호가 존재할 때의 실행 구문
                    Toast.makeText(getApplicationContext(), nametxt.getText() + "의 " + phonetxt.getText() + "번호가 이미 존재합니다. 재설정하십시오.", Toast.LENGTH_SHORT).show();
                    phonetxt.setText("");
                    phonetxt.requestFocus();
                }
            }
        });
    }
}

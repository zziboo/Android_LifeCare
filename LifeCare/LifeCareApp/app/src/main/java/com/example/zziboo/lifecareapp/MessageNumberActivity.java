package com.example.zziboo.lifecareapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.DataBase.MessageDBHelper;
import com.example.zziboo.lifecareapp.DataSet.MessageDataSet;

import java.util.Vector;

public class MessageNumberActivity extends AppCompatActivity {
    ListView msgListView;
    MessageDBHelper myMsgdb;
    Vector<MessageDataSet> msgDatasetVector;
    String selectPhoneNumber;
    CustomList adapter;
    Button addNumber, delNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_number);

        myMsgdb = new MessageDBHelper(this);

        Cursor res = myMsgdb.getData();
        msgDatasetVector = new Vector<>();

        //Cursor 받아와 DataSet에 저장하는 함수
        while(res.moveToNext()){
            msgDatasetVector.add(new MessageDataSet(res.getString(0), res.getString(1)));
        }

        addNumber = (Button) findViewById(R.id.msgaddbtn);
        delNumber = (Button) findViewById(R.id.msgdelbtn);

        //추가 버튼 클릭시 엑티비티 실행하는 리스너
        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPhoneActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        msgListView = (ListView) findViewById(R.id.messageList);
        adapter = new CustomList(MessageNumberActivity.this);

        //CustomList 어뎁터 붙이기
        msgListView.setAdapter(adapter);
        //ListView 선택시 호출되는 리스너
        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.phoneNumberList);
                selectPhoneNumber = txt.getText().toString();
            }
        });
    }

    //Custom List View 만드는 함수
    public class CustomList extends BaseAdapter {
        private final Activity context;
        public CustomList(Activity context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return msgDatasetVector.size();
        }

        @Override
        public Object getItem(int position) {
            return msgDatasetVector.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){ //콜백메소드
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item, null, true);
            TextView nameItem = (TextView) rowView.findViewById(R.id.nameList);
            TextView phoneNumberItem = (TextView) rowView.findViewById(R.id.phoneNumberList);
            nameItem.setText(msgDatasetVector.get(position).getName());
            phoneNumberItem.setText(msgDatasetVector.get(position).getPhoneNumber());
            return rowView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //삭제된 결과를 새롭게 Custom List로 설정
    public void deleteClick(View view){
        if(selectPhoneNumber == "") {
            Toast.makeText(getApplicationContext(), "목록을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        myMsgdb.deletePhone(selectPhoneNumber);
        Toast.makeText(getApplicationContext(), selectPhoneNumber + "을 삭제하였습니다.", Toast.LENGTH_SHORT).show();

        msgDatasetVector.clear();
        Cursor tmp = myMsgdb.getData();

        while(tmp.moveToNext()) {
            msgDatasetVector.add(new MessageDataSet(tmp.getString(0), tmp.getString(1)));
        }

        msgListView.setAdapter(adapter);
    }

    //추가된 결과 값 받아 새로 Custom List 설정
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                msgDatasetVector.clear();
                Cursor tmp = myMsgdb.getData();
                while(tmp.moveToNext()) {
                    msgDatasetVector.add(new MessageDataSet(tmp.getString(0), tmp.getString(1)));
                }
                msgListView.setAdapter(adapter);
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }
}

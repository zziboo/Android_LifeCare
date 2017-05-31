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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_number);

        myMsgdb = new MessageDBHelper(this);

        Cursor res = myMsgdb.getData();
        msgDatasetVector = new Vector<>();

        while(res.moveToNext()){
            msgDatasetVector.add(new MessageDataSet(res.getString(0), res.getString(1)));
        }

        msgListView = (ListView) findViewById(R.id.messageList);

        adapter = new CustomList(MessageNumberActivity.this);

        msgListView.setAdapter(adapter);

        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.phoneNumberList);
                selectPhoneNumber = txt.getText().toString();
            }
        });
    }

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
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item, null, true);
            TextView nameItem = (TextView) rowView.findViewById(R.id.nameList);
            TextView phoneNumberItem = (TextView) rowView.findViewById(R.id.phoneNumberList);
            nameItem.setText(msgDatasetVector.get(position).getName());
            phoneNumberItem.setText(msgDatasetVector.get(position).getPhoneNumber());
            return rowView;
        }
    }

    public void onClick(View view){
        if(selectPhoneNumber == ""){
            Toast.makeText(getApplicationContext(), "목록을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()){
            case R.id.msgdelbtn:
                if(selectPhoneNumber == "") {
                    Toast.makeText(getApplicationContext(), "목록을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), selectPhoneNumber + "을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                myMsgdb.deleteUrl(selectPhoneNumber);

                msgDatasetVector.clear();
                Cursor tmp = myMsgdb.getData();

                while(tmp.moveToNext()) {
                    msgDatasetVector.add(new MessageDataSet(tmp.getString(0), tmp.getString(1)));
                }

                msgListView.setAdapter(adapter);
                break;
            case R.id.msgaddbtn:
                break;
        }

        Intent intent = new Intent();
        intent.putExtra("INPUT_TEXT", selectPhoneNumber);
        setResult(RESULT_OK, intent);
        finish();
    }
}

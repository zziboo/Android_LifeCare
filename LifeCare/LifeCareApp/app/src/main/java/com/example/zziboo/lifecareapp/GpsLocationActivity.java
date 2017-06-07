package com.example.zziboo.lifecareapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.DataBase.GpsDBHelper;
import com.example.zziboo.lifecareapp.DataSet.GpsDataSet;

import java.util.Vector;

public class GpsLocationActivity extends AppCompatActivity {
    GpsDBHelper myGpsdb;
    Button addGpsButton, deleteGpsButton;
    Vector<GpsDataSet> gpsDatasetVector;
    ListView GpsListView;
    CustomList adapter;
    String selectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        myGpsdb = new GpsDBHelper(this);

        Cursor res = myGpsdb.getData();
        gpsDatasetVector = new Vector<>();

        //Cursor 받아와 DataSet에 저장하는 함수
        while(res.moveToNext()){
            gpsDatasetVector.add(new GpsDataSet(res.getString(0), res.getString(1)));
        }

        addGpsButton = (Button) findViewById(R.id.gpsaddbtn);
        deleteGpsButton = (Button) findViewById(R.id.gpsdelbtn);

        addGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewGpsActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        GpsListView = (ListView) findViewById(R.id.gpsList);
        adapter = new CustomList(GpsLocationActivity.this);

        //CustomList 어뎁터 붙이기
        GpsListView.setAdapter(adapter);
        //ListView 선택시 호출되는 리스너
        GpsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.nameList);
                selectTitle = txt.getText().toString();
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
            return gpsDatasetVector.size();
        }

        @Override
        public Object getItem(int position) {
            return gpsDatasetVector.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){ //콜백메소드
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item, null, true);
            TextView titleItem = (TextView) rowView.findViewById(R.id.nameList);
            TextView locationItem = (TextView) rowView.findViewById(R.id.phoneNumberList);
            titleItem.setText(gpsDatasetVector.get(position).getTitle());
            locationItem.setText(gpsDatasetVector.get(position).getLocation());
            return rowView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //삭제된 결과를 새롭게 Custom List로 설정
    public void deleteClick(View view){
        if(selectTitle == "") {
            Toast.makeText(getApplicationContext(), "목록을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        myGpsdb.deleteGps(selectTitle);
        Toast.makeText(getApplicationContext(), selectTitle + "을 삭제하였습니다.", Toast.LENGTH_SHORT).show();

        gpsDatasetVector.clear();
        Cursor tmp = myGpsdb.getData();

        while(tmp.moveToNext()) {
            gpsDatasetVector.add(new GpsDataSet(tmp.getString(0), tmp.getString(1)));
        }

        GpsListView.setAdapter(adapter);
    }

    //추가된 결과 값 받아 새로 Custom List 설정
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                gpsDatasetVector.clear();
                Cursor tmp = myGpsdb.getData();
                while(tmp.moveToNext()) {
                    gpsDatasetVector.add(new GpsDataSet(tmp.getString(0), tmp.getString(1)));
                }
                GpsListView.setAdapter(adapter);
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }
}

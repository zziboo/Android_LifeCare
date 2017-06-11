package com.example.zziboo.lifecareapp;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zziboo.lifecareapp.DataBase.GpsDBHelper;
import com.example.zziboo.lifecareapp.Service.GpsService;

public class NewGpsActivity extends AppCompatActivity implements LocationListener{
    EditText titletxt, locationtxt;
    Button addButton;
    GpsDBHelper myGpsdb;
    String myLocation;
    double latitude;
    double longitude;

    boolean checkGPS = false;
    boolean checkNetwork = false;

    boolean canGetLocation = false;

    protected LocationManager locationManager;
    Location loc;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gps);

        titletxt = (EditText) findViewById(R.id.titletxt);
        locationtxt = (EditText) findViewById(R.id.locationtxt);

        addButton = (Button) findViewById(R.id.addLocation);
        myGpsdb = new GpsDBHelper(this);

        getLocation();
        myLocation = latitude + "/" + longitude;

        locationtxt.setText(myLocation);

        //데이터 저장 클릭시 실행되는 리스너
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit Text가 채워져있는지 확인하는 구문
                if(titletxt.getText().toString().length() <= 0 || locationtxt.getText().toString().length() <= 0){
                    Toast.makeText(getApplicationContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DB에 추가하는 구문
                if(myGpsdb.insertGps(titletxt.getText().toString(), locationtxt.getText().toString())){
                    Toast.makeText(getApplicationContext(), titletxt.getText() + "로 " + locationtxt.getText() + "를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    titletxt.setText("");
                    locationtxt.setText("");
                    titletxt.requestFocus();

                    //추가 이후 Custom List를 초기화 시켜주기위해 결과 값 보내기
                    Intent intent = new Intent();
                    intent.putExtra("result", locationtxt.getText());
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    //같은 위치가 존재할 때의 실행 구문
                    Toast.makeText(getApplicationContext(), titletxt.getText() + "의 " + locationtxt.getText() + "번호가 이미 존재합니다. 재설정하십시오.", Toast.LENGTH_SHORT).show();
                    locationtxt.setText("");
                    locationtxt.requestFocus();
                }
            }
        });
    }


    public void getLocation(){
        Log.d("GPS_MAPS", "getLocation");
        //latLngList.add(new LatLng(getLatitude(), getLongitude()));
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider Available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (checkNetwork) {
                    Toast.makeText(this, "Network", Toast.LENGTH_SHORT).show();

                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }

                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                    catch(SecurityException e){
                        Log.e("error",e.getMessage()+"");
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                Toast.makeText(this,"GPS",Toast.LENGTH_SHORT).show();
                if (loc == null) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location.getLatitude() + "/" + location.getLongitude(); // lati/longi
        locationtxt.setText(myLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

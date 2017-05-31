package com.example.zziboo.lifecareapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zziboo.lifecareapp.R;

/**
 * Created by zziboo on 2017-05-24.
 */

public class GpsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.gps_fragment, container, false);
    }
}

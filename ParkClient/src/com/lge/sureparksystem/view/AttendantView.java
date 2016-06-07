package com.lge.sureparksystem.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lge.sureparksystem.parkclient.R;

public class AttendantView  extends Fragment implements BaseView {
    private static final String TAG = "AttendantView";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.attendant_layout, container, false);
        return result;
    }
}

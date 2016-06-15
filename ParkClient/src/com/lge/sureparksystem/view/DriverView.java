package com.lge.sureparksystem.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

public class DriverView extends Fragment implements BaseView {

    private static final String TAG = "DriverView";
    private Spinner mParkIDSppiner;
    private Spinner mReservationTimeSppiner;
    private LinearLayout mArrivedTimelayout;
    private TextView mReservedTime;
    private Button mButton;
    private boolean mIsReservedUser = false; // Check if reserved info exist

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater
                .inflate(R.layout.driver_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        mReservationTimeSppiner = (Spinner)result
                .findViewById(R.id.reservation_time);
        mArrivedTimelayout = (LinearLayout)result
                .findViewById(R.id.arrived_time);
        mReservedTime = (TextView)result.findViewById(R.id.reserved_time);
        mButton = (Button) result.findViewById(R.id.btn_ok);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        CharSequence[] parkId = { "Pittsburgh", "Chicago" };
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, parkId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParkIDSppiner.setAdapter(adapter);
        mParkIDSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Utils.showToast(getActivity(), "position : " + position
                        + ", id : " + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showToast(getActivity(), "unselected");
            }
        });

        CharSequence[] reservationTime = { "After 1 hour", "After 2 hours",
                "After 3 hours" };
        ArrayAdapter<CharSequence> adapterReserve = new ArrayAdapter<CharSequence>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, reservationTime);
        adapterReserve
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReservationTimeSppiner.setAdapter(adapterReserve);
        mReservationTimeSppiner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        Utils.showToast(getActivity(), "position : " + position
                                + ", id : " + id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Utils.showToast(getActivity(), "unselected");
                    }
                });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mIsReservedUser) {
            mReservedTime.setVisibility(View.VISIBLE);
            mArrivedTimelayout.setVisibility(View.GONE);
            mButton.setText(R.string.cancel);
        } else {
            mReservedTime.setVisibility(View.GONE);
            mArrivedTimelayout.setVisibility(View.VISIBLE);
            mButton.setText(R.string.ok);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}

package com.lge.sureparksystem.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

public class OwnerView extends BaseFragment {
    private static final String TAG = "OwnerView";
    private Spinner mParkIDSppiner;
    private Spinner mStatisticalDuration;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater
                .inflate(R.layout.owner_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        mStatisticalDuration = (Spinner)result.findViewById(R.id.period_time);
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

        CharSequence[] durationTime = { "1 week", "2 weeks", "3 weeks",  "1 month", "2 months"};
        ArrayAdapter<CharSequence> adapterReserve = new ArrayAdapter<CharSequence>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, durationTime);
        adapterReserve
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatisticalDuration.setAdapter(adapterReserve);
        mStatisticalDuration
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

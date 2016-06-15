package com.lge.sureparksystem.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

public class AttendantView  extends Fragment implements BaseView {
    private static final String TAG = "AttendantView";
    private Spinner mParkIDSppiner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.attendant_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        CharSequence[] parkId = {"Pittsburgh", "Chicago"};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, parkId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParkIDSppiner.setAdapter(adapter);
        mParkIDSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Utils.showToast(getActivity(), "position : " + position + ", id : " + id);
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

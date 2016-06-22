package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lge.sureparksystem.model.AttendantModel;
import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.parkclient.R;

public class AttendantView extends BaseFragment implements BaseView {
    private static final String TAG = "AttendantView";
    private Spinner mParkIDSppiner;
    private AttendantModel mAttendantModel;
    private LinearLayout mTableLayout;
    private LayoutInflater mLayoutInflater;
    private boolean mNoficationComming = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.attendant_layout, container, false);
        mTableLayout = (LinearLayout)result.findViewById(R.id.table_layout);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mLayoutInflater = getActivity().getLayoutInflater();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mNoficationComming) {
//            if (true) {
            if (mAttendantModel != null && mAttendantModel.mNotification != null) {
                AlertDialog.Builder notiAlert = new AlertDialog.Builder(getActivity());
                notiAlert.setIcon(android.R.drawable.ic_dialog_alert);
                notiAlert.setTitle(R.string.notification);
                notiAlert.setMessage(mAttendantModel.mNotification.type);
//                notiAlert.setMessage("Reallocation");
                notiAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
    
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
    
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mNoficationComming = false;
                    }
                }).create().show();
            }
        } else {
            // if (true) {
            if (mAttendantModel != null && mAttendantModel.mParkinglotStatus != null) {
                View tableView = mLayoutInflater.inflate(R.layout.attent_status_layout, null);
                mTableLayout.addView(tableView);
                int count = mAttendantModel.mParkinglotStatus.slot_count;
                String[] staute = mAttendantModel.mParkinglotStatus.slot_status;
                String[] driver = mAttendantModel.mParkinglotStatus.slot_driverid;
                String[] often = mAttendantModel.mParkinglotStatus.driver_often;
                String[] time = mAttendantModel.mParkinglotStatus.slot_time;
                // int count = 5;
                // String[] staute = {"q", "w", "e", "r", "t"};
                // String[] driver = {"d1", "s2", "d3", "d4", "d5"};
                // String[] often = {"1", "2", "3", "4", "5"};
                // String[] time = {"t1", "t2", "t3", "t4", "t5"};
                for (int i = 0; i < count; i++) {
                    tableView = mLayoutInflater.inflate(R.layout.attent_status_layout, null);
                    TextView tv1 = (TextView)tableView.findViewById(R.id.first_column);
                    tv1.setText(String.valueOf(i + 1));
                    TextView tv2 = (TextView)tableView.findViewById(R.id.second_column);
                    tv2.setText(staute[i]);
                    TextView tv3 = (TextView)tableView.findViewById(R.id.third_column);
                    tv3.setText(driver[i]);
                    TextView tv4 = (TextView)tableView.findViewById(R.id.fourth_column);
                    tv4.setText(often[i]);
                    TextView tv5 = (TextView)tableView.findViewById(R.id.fifth_column);
                    tv5.setText(time[i]);
                    mTableLayout.addView(tableView);
                }
                mTableLayout.setVisibility(View.VISIBLE);
            } else {
                mTableLayout.setVisibility(View.GONE);
            }
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

    @Override
    public void updateFlag(boolean value) {
        mNoficationComming = value;
        super.updateFlag(value);
    }

    @Override
    public void setBaseModel(BaseModel baseModel) {
        mAttendantModel = null;
        mAttendantModel = (AttendantModel)baseModel;
    }

    @Override
    public void requsetServer(RequestData rd, Bundle bundle) {
        // TODO Auto-generated method stub

    }
}

package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.DriverModel;
import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DriverView extends BaseFragment implements OnClickListener {

    private static final String TAG = "DriverView";
    private Spinner mParkIDSppiner;
    private Spinner mReservationTimeSppiner;
    private LinearLayout mArrivedTimelayout;
    private TextView mReservedTime;
    private ImageView mQRImage;
    private Button mButton;
    private EditText mConfirmationText;
    private boolean mIsReservedUser = true; // Check if reserved info exist
    private DriverModel mDriverModel;
    private int mSelectedTime = 0;
    private int mSelectedParkId = 0;
    private String mSelectedConfirmationNum;
    private ImageView mSelectedWQImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.driver_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        mReservationTimeSppiner = (Spinner)result.findViewById(R.id.reservation_time);
        mArrivedTimelayout = (LinearLayout)result.findViewById(R.id.arrived_time);
        mReservedTime = (TextView)result.findViewById(R.id.reserved_time);
        mConfirmationText = (EditText)result.findViewById(R.id.confirmation_number);
        mQRImage = (ImageView)result.findViewById(R.id.confirmation_iamge);
        mButton = (Button)result.findViewById(R.id.btn_ok);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        CharSequence[] parkId = { "Pittsburgh", "Chicago" };
        if (mDriverModel != null && mDriverModel.mParkinglot_List != null) {
            parkId = mDriverModel.mParkinglot_List.parkinglot_id;
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, parkId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParkIDSppiner.setAdapter(adapter);
        mParkIDSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedParkId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showToast(getActivity(), "unselected");
            }
        });

        CharSequence[] reservationTime = { "After 1 hour", "After 2 hours", "After 3 hours" };
        ArrayAdapter<CharSequence> adapterReserve = new ArrayAdapter<CharSequence>(getActivity()
                .getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, reservationTime);
        adapterReserve.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReservationTimeSppiner.setAdapter(adapterReserve);
        mReservationTimeSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTime = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showToast(getActivity(), "unselected");
            }
        });

        mConfirmationText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSelectedConfirmationNum = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        mQRImage.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater lf = getActivity().getLayoutInflater();
                View layout = lf.inflate(R.layout.qr_image_dialog, null);
                dialog.setView(layout).setTitle(R.string.qr_dialog_title);
                dialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.create().show();
            }
        });
        mButton.setOnClickListener(this);
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
            mQRImage.setVisibility(View.VISIBLE);
        } else {
            mReservedTime.setVisibility(View.GONE);
            mArrivedTimelayout.setVisibility(View.VISIBLE);
            mButton.setText(R.string.new_reservation);
            mQRImage.setVisibility(View.GONE);
        }
        
        if (mDriverModel != null && mDriverModel.mReservation_Information != null) {
            
        }
        super.onResume();
    }

    @Override
    public void updateFlag(boolean value) {
        mIsReservedUser = value;
    }

    @Override
    public void setBaseModel(BaseModel baseModel) {
        mDriverModel = null;
        mDriverModel = (DriverModel)baseModel;
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
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
        case R.id.btn_ok:
            Bundle bundle = new Bundle();
            if (mIsReservedUser) {
                if (mDriverModel == null || mDriverModel.mReservation_Information == null) {
                    Utils.showToast(getActivity(), "mDriverModel null");
                    return;
                }
                bundle.putString("reservation_id", mDriverModel.mReservation_Information.reservation_id);
                mCallback.requsetServer(RequestData.CANCEL_REQUEST, bundle);
            } else {
                if (mDriverModel == null || mDriverModel.mParkinglot_List == null) {
                    Utils.showToast(getActivity(), "mDriverModel null");
                    return;
                }
                if (mSelectedConfirmationNum == null) {
                    Utils.showToast(getActivity(), "mSelectedConfirmationNum null");
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, (mSelectedTime + 1));
                String dTime = formatter.format(cal.getTime());
                bundle.putString("parkinglot_id", mDriverModel.mParkinglot_List.parkinglot_id[mSelectedParkId]);
                bundle.putString("reservation_time", dTime);
                bundle.putString("paymentinfo", mSelectedConfirmationNum);
                mCallback.requsetServer(RequestData.RESERVATION_REQUEST, bundle);
            }
            break;

        default:
            break;
        }
    }

}

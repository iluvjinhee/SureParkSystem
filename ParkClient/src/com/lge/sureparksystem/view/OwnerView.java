package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

public class OwnerView extends BaseFragment implements OnClickListener {
    private static final String TAG = "OwnerView";
    private Spinner mParkIDSppiner;
    private Spinner mStatisticalDuration;
    private Button mConfigurationBtn;
    private LayoutInflater mLayoutInflater;
    private static final int CHANGE_PARKING_FEE = 0;
    private static final int CHANGE_GRACE_PERIOD = 1;
    private static final int ADD_PARKING_LOT = 2;
    private static final int DELETE_PARKING_LOT = 3;
    private Dialog mFirstDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.owner_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        mStatisticalDuration = (Spinner)result.findViewById(R.id.period_time);
        mConfigurationBtn = (Button)result.findViewById(R.id.btn_configuration);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mLayoutInflater = getActivity().getLayoutInflater();

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

        CharSequence[] durationTime = { "1 week", "2 weeks", "3 weeks",
                "1 month", "2 months" };
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

        mConfigurationBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btn_configuration:
            AlertDialog.Builder createDialog = new AlertDialog.Builder(getActivity());
            createDialog.setTitle(R.string.configuration_settings);
            final AlertDialog.Builder mSecondDialog = new AlertDialog.Builder(getActivity());
            final View dialogView = mLayoutInflater.inflate(R.layout.dialog_configuration_change, null);
            createDialog.setSingleChoiceItems(R.array.owner_configuration_settings, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean bValued = false;
                    switch (which) {
                    case CHANGE_PARKING_FEE :
                        bValued = true;
                        mFirstDialog.dismiss();
                        mSecondDialog.setTitle(R.string.change_parking_fee);
                        mSecondDialog.setView(dialogView);
                        break;
                    case CHANGE_GRACE_PERIOD :
                        bValued = true;
                        mFirstDialog.dismiss();
                        mSecondDialog.setTitle(R.string.change_grace_period);
                        mSecondDialog.setView(dialogView);
                        break;
                    case ADD_PARKING_LOT :
                        bValued = true;
                        mFirstDialog.dismiss();
                        mSecondDialog.setTitle(R.string.add_parking_lot);
                        mSecondDialog.setView(dialogView);
                        break;
                    case DELETE_PARKING_LOT :
                        bValued = true;
                        mFirstDialog.dismiss();
                        CharSequence[] cs = {"Parking 1", "Parking 2", "Parking 3"};
                        mSecondDialog.setTitle(R.string.delete_parking_lot);
                        mSecondDialog.setSingleChoiceItems(cs, -1, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        });
                        break;
                    default:
                        break;
                    }

                    if (bValued) {
                        mSecondDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                        mSecondDialog.create().show();
                    }
                }
            });
            mFirstDialog = createDialog.create();
            mFirstDialog.show();
            break;

        default:
            break;
        }
    }
}

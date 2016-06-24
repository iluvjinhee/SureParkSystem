package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Spinner;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.DriverModel;
import com.lge.sureparksystem.model.OwnerModel;
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
    private static final int CREATE_ATTENDANT = 2;
    private static final int ADD_PARKING_LOT = 3;
    private static final int DELETE_PARKING_LOT = 4;
    private static final int DELETE_ATTENDANT = 5;
    private Dialog mFirstDialog;
    private int mCurrentParkingId = 0;
    private int mCurrentDurationTime = 0;
    private String mChangedValue;
    private OwnerModel mOwnerModel;
    private String mCreateIdText;
    private String mCreatePwdText;
    private String mCreateNameText;
    private String mParkingID;
    private String mid_viewEditText;
    private String mpwd_editEditText;
    private String maddress_editEditText;
    private String mparkingfee_editEditText;
    private String mgraceperiod_editEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        CharSequence[] parkId = { "Pittsburgh", "Chicago" };
        if (mOwnerModel != null && mOwnerModel.mParkinglot_List != null) {
            parkId = mOwnerModel.mParkinglot_List.parkinglot_id_list;
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, parkId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParkIDSppiner.setAdapter(adapter);
        mParkIDSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentParkingId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showToast(getActivity(), "unselected");
            }
        });

        // day, week, month, year
        CharSequence[] durationTime = { "Day", "Week", "Month", "Year" };
        ArrayAdapter<CharSequence> adapterReserve = new ArrayAdapter<CharSequence>(getActivity()
                .getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, durationTime);
        adapterReserve.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatisticalDuration.setAdapter(adapterReserve);
        mStatisticalDuration.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentDurationTime = position;
                String duration = null;
                switch (position) {
                case 0:
                    duration = "day";
                    break;
                case 1:
                    duration = "week";
                    break;
                case 2:
                    duration = "month";
                    break;
                case 3:
                    duration = "year";
                    break;
                default:
                    break;
                }
                if (duration == null) {
                    Utils.showToast(getActivity(), "Check duration");
                    return;
                }
                if (mOwnerModel != null && mOwnerModel.mParkinglot_List != null) {
                    Bundle subBundle = new Bundle();
                    subBundle.putString("parkinglot_id", mParkingID);
                    subBundle.putString("period", duration);
                    mCallback.requsetServer(RequestData.PARKINGLOT_STATS_REQUEST, subBundle);
                } else {
                    Utils.showToast(getActivity(), "Choose park first what you want");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showToast(getActivity(), "unselected");
            }
        });

        mConfigurationBtn.setOnClickListener(this);
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
            EditText et = (EditText)dialogView.findViewById(R.id.value_edit);
            et.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mChangedValue = s.toString();
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
            createDialog.setSingleChoiceItems(R.array.owner_configuration_settings, -1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case CHANGE_PARKING_FEE:
                                mFirstDialog.dismiss();
                                mSecondDialog.setTitle(R.string.change_parking_fee);
                                mSecondDialog.setView(dialogView);
                                mSecondDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mOwnerModel != null && mOwnerModel.mParkinglot_List != null) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("parkinglot_id",
                                                    mOwnerModel.mParkinglot_List.parkinglot_id_list[mCurrentParkingId]);
                                            bundle.putString("parking_fee", mChangedValue);
                                            mCallback.requsetServer(RequestData.CHANGE_PARKINGFEE, bundle);
                                        } else {
                                            Utils.showToast(getActivity(), "Check mOwnerModel");
                                        }
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
                                break;
                            case CHANGE_GRACE_PERIOD:
                                mFirstDialog.dismiss();
                                mSecondDialog.setTitle(R.string.change_grace_period);
                                mSecondDialog.setView(dialogView);
                                mSecondDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mOwnerModel != null && mOwnerModel.mParkinglot_List != null) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("parkinglot_id",
                                                    mOwnerModel.mParkinglot_List.parkinglot_id_list[mCurrentParkingId]);
                                            bundle.putString("graceperiod", mChangedValue);
                                            mCallback.requsetServer(RequestData.CHANGE_GRACEPERIOD, bundle);
                                        } else {
                                            Utils.showToast(getActivity(), "Check mOwnerModel");
                                        }
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
                                break;
                            case CREATE_ATTENDANT:
                                mFirstDialog.dismiss();
                                mSecondDialog.setTitle(R.string.create_attendant);
                                View dialogLayout = mLayoutInflater.inflate(R.layout.alert_dialog_add_attendant, null);
                                mSecondDialog.setView(dialogLayout);
                                EditText nameEditText = (EditText)dialogLayout.findViewById(R.id.username_edit);
                                nameEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mCreateNameText = s.toString();
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
                                EditText idEditText = (EditText)dialogLayout.findViewById(R.id.email_edit);
                                idEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mCreateIdText = s.toString();
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

                                EditText pwdEditText = (EditText)dialogLayout.findViewById(R.id.password_edit);
                                pwdEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mCreatePwdText = s.toString();
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
                                EditText parkingIdEditText = (EditText)dialogLayout.findViewById(R.id.parkinglot_edit);
                                parkingIdEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mParkingID = s.toString();
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
                                mSecondDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mCreateIdText == null || mCreatePwdText == null || mCreateNameText == null
                                                || mParkingID == null) {
                                            Utils.showToast(getActivity(), "Input all information");
                                            return;
                                        }
                                        Bundle subBundle = new Bundle();
                                        subBundle.putString("id", mCreateIdText);
                                        subBundle.putString("pwd", mCreatePwdText);
                                        subBundle.putString("name", mCreateNameText);
                                        subBundle.putString("parkinglot_id", mParkingID);
                                        mCallback.requsetServer(RequestData.CREATE_ATTENDANT, subBundle);
                                    }
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                break;
                            case DELETE_PARKING_LOT:
                                mFirstDialog.dismiss();
                                CharSequence[] cs = null;
                                if (mOwnerModel != null && mOwnerModel.mParkinglot_List != null) {
                                    cs = mOwnerModel.mParkinglot_List.parkinglot_id_list;
                                } else {
                                    Utils.showToast(getActivity(), "Check mOwnerModel");
                                    return;
                                }
                                mSecondDialog.setTitle(R.string.delete_parking_lot);
                                mSecondDialog.setSingleChoiceItems(cs, -1, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("parkinglot_id",
                                                mOwnerModel.mParkinglot_List.parkinglot_id_list[which]);
                                        mCallback.requsetServer(RequestData.REMOVE_PARKINGLOT, bundle);
                                    }
                                });
                                break;
                            case DELETE_ATTENDANT:
                                // To be continue
                                break;
                            case ADD_PARKING_LOT:
                                mFirstDialog.dismiss();
                                mSecondDialog.setTitle(R.string.add_parking_lot);
                                View dialogLayout1 = mLayoutInflater.inflate(R.layout.alert_dialog_add_parkinglot, null);
                                mSecondDialog.setView(dialogLayout1);
                                EditText id_viewEditText = (EditText)dialogLayout1.findViewById(R.id.id_edit);
                                id_viewEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mid_viewEditText = s.toString();
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
                                EditText pwd_editEditText = (EditText)dialogLayout1.findViewById(R.id.pwd_edit);
                                pwd_editEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mpwd_editEditText = s.toString();
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

                                EditText address_editEditText = (EditText)dialogLayout1.findViewById(R.id.address_edit);
                                address_editEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        maddress_editEditText = s.toString();
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
                                EditText parkingfee_editEditText = (EditText)dialogLayout1
                                        .findViewById(R.id.parkingfee_edit);
                                parkingfee_editEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mparkingfee_editEditText = s.toString();
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
                                EditText graceperiod_editEditText = (EditText)dialogLayout1
                                        .findViewById(R.id.graceperiod_edit);
                                graceperiod_editEditText.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        mgraceperiod_editEditText = s.toString();
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
                                mSecondDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mid_viewEditText == null || mpwd_editEditText == null
                                                || maddress_editEditText == null || mparkingfee_editEditText == null
                                                || mgraceperiod_editEditText == null) {
                                            Utils.showToast(getActivity(), "Input all information");
                                            return;
                                        }
                                        Bundle subBundle = new Bundle();
                                        subBundle.putString("id", mid_viewEditText);
                                        subBundle.putString("pwd", mpwd_editEditText);
                                        subBundle.putString("address", maddress_editEditText);
                                        subBundle.putString("parkingfee", mparkingfee_editEditText);
                                        subBundle.putString("graceperiod", mgraceperiod_editEditText);
                                        mCallback.requsetServer(RequestData.ADD_PARKINGLOT, subBundle);
                                    }
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                break;
                            default:
                                break;
                            }

                            mSecondDialog.create().show();
                        }
                    });
            mFirstDialog = createDialog.create();
            mFirstDialog.show();
            break;

        default:
            break;
        }
    }

    @Override
    public void updateFlag(boolean value) {
        // TODO Auto-generated method stub
        super.updateFlag(value);
    }

    @Override
    public void setBaseModel(BaseModel baseModel) {
        mOwnerModel = null;
        mOwnerModel = (OwnerModel)baseModel;
    }

}

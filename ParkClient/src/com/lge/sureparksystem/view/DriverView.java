package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
    private TextView mPark_fee;
    private TextView mPayment_info;
    private TextView mPark_grace_period;
    private TextView mPark_loacation;

    private ImageView mQRImage;
    private Button mButton;
    private EditText mCard_number;
    private boolean mIsReservedUser = false; // If reserved info exist
    private DriverModel mDriverModel;
    private int mSelectedTime = 0;
    private int mSelectedParkId = 0;
    private String mSelectedConfirmationNum;
    private Bitmap mCurrentQRImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.driver_layout, container, false);
        mParkIDSppiner = (Spinner)result.findViewById(R.id.park_id);
        mReservationTimeSppiner = (Spinner)result.findViewById(R.id.reservation_time);
        mArrivedTimelayout = (LinearLayout)result.findViewById(R.id.arrived_time);
        mReservedTime = (TextView)result.findViewById(R.id.reserved_time);
        mCard_number = (EditText)result.findViewById(R.id.card_number);
        mQRImage = (ImageView)result.findViewById(R.id.qr_iamge);
        mPayment_info = (TextView)result.findViewById(R.id.payment_info);
        mPark_fee = (TextView)result.findViewById(R.id.park_fee);
        mPark_grace_period = (TextView)result.findViewById(R.id.park_grace_period);
        mPark_loacation = (TextView)result.findViewById(R.id.park_loacation);
        mButton = (Button)result.findViewById(R.id.btn_ok);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        CharSequence[] parkId = { "Pittsburgh", "Chicago" };
        if (mIsReservedUser) {
            if (mDriverModel != null && mDriverModel.mReservation_Information != null) {
                String str = mDriverModel.mReservation_Information.parkinglot_id;
                String[] str1 = { str };
                parkId = str1;
            }
        } else {
            if (mDriverModel != null && mDriverModel.mParkinglot_List != null) {
                parkId = mDriverModel.mParkinglot_List.parkinglot_id;
            }
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, parkId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParkIDSppiner.setAdapter(adapter);
        mParkIDSppiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedParkId = position;
                updateTextField();
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

        mCard_number.addTextChangedListener(new TextWatcher() {

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
                if (mCurrentQRImage != null) {
                    ImageView iv = (ImageView)layout.findViewById(R.id.confirmation_iamge);
                    iv.setImageBitmap(mCurrentQRImage);
                }
                dialog.setView(layout).setTitle(R.string.qr_dialog_title);
                dialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.create().show();
            }
        });

        if (mIsReservedUser && mDriverModel != null && mDriverModel.mReservation_Information != null) {
            Point point = new Point(900, 900);
            CreateQRCodeAsyncTask ct = new CreateQRCodeAsyncTask(point);
            ct.execute(mDriverModel.mReservation_Information.confirmationinfo);
        }
        mButton.setOnClickListener(this);
        if (mIsReservedUser) {
            mReservedTime.setVisibility(View.VISIBLE);
            mArrivedTimelayout.setVisibility(View.GONE);
            mButton.setText(R.string.cancel);
            mQRImage.setVisibility(View.VISIBLE);
            mPayment_info.setVisibility(View.VISIBLE);
        } else {
            mReservedTime.setVisibility(View.GONE);
            mArrivedTimelayout.setVisibility(View.VISIBLE);
            mButton.setText(R.string.new_reservation);
            mQRImage.setVisibility(View.GONE);
            mPayment_info.setVisibility(View.GONE);
        }
        updateTextField();

        super.onResume();
    }

    private void updateTextField() {
        if (mIsReservedUser) {
            if (mDriverModel != null && mDriverModel.mReservation_Information != null) {
                mPark_fee.setText(getString(R.string.park_fee) + mDriverModel.mReservation_Information.parkingfee);
                mPark_grace_period.setText(getString(R.string.park_grace_period)
                        + mDriverModel.mReservation_Information.graceperiod);
                mPark_loacation.setText(getString(R.string.parking_address)
                        + mDriverModel.mReservation_Information.parkinglot_location);
                mReservedTime.setText(getString(R.string.reserved_time)
                        + mDriverModel.mReservation_Information.reservation_time);
            }
        } else {
            if (mDriverModel != null && mDriverModel.mParkinglot_List != null) {
                mPark_fee.setText(getString(R.string.park_fee)
                        + mDriverModel.mParkinglot_List.parkingfee[mSelectedParkId]);
                mPark_grace_period.setText(getString(R.string.park_grace_period)
                        + mDriverModel.mParkinglot_List.graceperiod[mSelectedParkId]);
                mPark_loacation.setText(getString(R.string.parking_address)
                        + mDriverModel.mParkinglot_List.parkinglot_location[mSelectedParkId]);
            }
        }
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
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onPause");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
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
                // bundle.putString("reservation_time", dTime);
                bundle.putString("reservation_time", String.valueOf(System.currentTimeMillis()));
                bundle.putString("paymentinfo", mSelectedConfirmationNum);
                mCallback.requsetServer(RequestData.RESERVATION_REQUEST, bundle);
            }
            break;

        default:
            break;
        }
    }

    private class CreateQRCodeAsyncTask extends AsyncTask<String, Void, Bitmap> {
        // QR code 색상
        private static final int WHITE = 0xFFFFFFFF;
        private static final int BLACK = 0xFF000000;

        // QR code bitmap 크기 비율
        private static final float REDUCE_RATIO = 0.8f;

        // 화면 크기
        private Point mDisplaySize;

        public CreateQRCodeAsyncTask(Point displaySize) {
            mDisplaySize = displaySize;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // zxing library class (QR code writer)
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            try {
                // 화면 비율 대비 QR 코드 이미지 크기 조정
                int temp = mDisplaySize.x;

                if (temp > mDisplaySize.y) {
                    temp = mDisplaySize.y;
                }

                int dWidth = (int)((float)temp * REDUCE_RATIO);
                int dHeight = (int)((float)temp * REDUCE_RATIO);

                // QR code로 인코딩해서 비트맵 array 정보를 얻어옴 zxing 자체 class임
                BitMatrix result = qrCodeWriter.encode(params[0], BarcodeFormat.QR_CODE, dWidth, dHeight);
                int width = result.getWidth();
                int height = result.getHeight();

                // Bitmap pixel array
                int[] pixels = new int[width * height];

                // BitMatrix 정보를 바탕으로 bitmap 픽셀 array 에 color 입력
                // All are 0, or black, by default
                for (int y = 0; y < height; y++) {
                    int offset = y * width;
                    for (int x = 0; x < width; x++) {
                        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                    }
                }

                // pixel array 크기의 bitmap 생성
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                // bitmap에 pixel 정보 입력
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

                return bitmap;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mCurrentQRImage = result;
            }
        }
    }
}

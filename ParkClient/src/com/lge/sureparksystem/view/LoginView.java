package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lge.sureparksystem.parkclient.R;

public class LoginView extends BaseFragment implements OnClickListener {

    private static final String TAG = "LoginView";
    RelativeLayout mRootLayout;
    EditText mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.login_layout, container, false);
        mRootLayout = (RelativeLayout)result.findViewById(R.id.root);
        result.findViewById(R.id.btn_ok).setOnClickListener(this);
        result.findViewById(R.id.btn_create).setOnClickListener(this);
        return result;
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
        switch (v.getId()) {
        case R.id.btn_ok:
            mCallback.requsetServer(RequestData.CREATE_NEW_DRIVER, "TEST");
            break;
        case R.id.btn_create:
            AlertDialog.Builder mCreateDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            mCreateDialog.setView(layoutInflater.inflate(R.layout.alert_dialog_text_entry, null));
            mCreateDialog
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setTitle(R.string.ok)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    mCallback.requsetServer(RequestData.LOGIN_DRIVER, "TEST");
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {

                                }
                            }).create().show();
            break;
        default:
            break;
        }
    }

}

package com.lge.sureparksystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
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
    EditText mIDEditText;
    EditText mPWDEditText;
    String mId;
    String mPwd;
    String mCreateNameText;
    String mCreateIdText;
    String mCreatePwdText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View result = inflater.inflate(R.layout.login_layout, container, false);
        mRootLayout = (RelativeLayout)result.findViewById(R.id.root);
        mIDEditText = (EditText)result.findViewById(R.id.edit_id);
        mPWDEditText = (EditText)result.findViewById(R.id.edit_password);
        result.findViewById(R.id.btn_ok).setOnClickListener(this);
        result.findViewById(R.id.btn_create).setOnClickListener(this);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIDEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mId = s.toString();
                Log.d(TAG, "mId : " + mId);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPWDEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPwd = s.toString();
                Log.d(TAG, "mPwd : " + mPwd);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_ok:
            Bundle bundle = new Bundle();
            bundle.putString("email", mId);
            bundle.putString("pwd", mPwd);
            mCallback.requsetServer(RequestData.LOGIN_USER, bundle);
            break;
        case R.id.btn_create:
            AlertDialog.Builder createDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_text_entry, null);
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
            createDialog.setView(dialogLayout);
            createDialog.setTitle(R.string.create_id)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle subBundle = new Bundle();
                            subBundle.putString("name", mCreateNameText);
                            subBundle.putString("email", mCreateIdText);
                            subBundle.putString("pwd", mCreatePwdText);
                            mCallback.requsetServer(RequestData.CREATE_NEW_DRIVER, subBundle);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
            break;
        default:
            break;
        }
    }

}

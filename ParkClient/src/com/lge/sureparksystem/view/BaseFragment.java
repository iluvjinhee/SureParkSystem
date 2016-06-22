package com.lge.sureparksystem.view;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import com.lge.sureparksystem.model.BaseModel;

public class BaseFragment extends Fragment {
    static final String TAG = "BaseFragment";
    public BaseView mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (BaseView)activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + "must implements BaseView");
        }
    }

    public void updateFlag(boolean value) {
        Log.d(TAG, "Not override");
    }

    public void setBaseModel(BaseModel baseModel) {
        Log.d(TAG, "Not override");
    }
}

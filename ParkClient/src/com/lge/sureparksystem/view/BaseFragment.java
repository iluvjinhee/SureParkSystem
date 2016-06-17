package com.lge.sureparksystem.view;

import android.app.Activity;
import android.app.Fragment;

public class BaseFragment extends Fragment {
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

}
